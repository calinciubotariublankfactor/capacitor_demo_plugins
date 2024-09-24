package com.mycompany.plugins.example;

import android.content.Context;
import android.os.Build;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.paysafe.android.PaysafeSDK;
import com.paysafe.android.core.data.entity.PSCallback;
import com.paysafe.android.core.domain.exception.PaysafeException;
import com.paysafe.android.core.domain.exception.PaysafeRuntimeError;
import com.paysafe.android.core.domain.model.config.PSEnvironment;
import com.paysafe.android.tokenization.domain.model.paymentHandle.BillingDetails;
import com.paysafe.android.tokenization.domain.model.paymentHandle.MerchantDescriptor;
import com.paysafe.android.tokenization.domain.model.paymentHandle.ShippingDetails;
import com.paysafe.android.tokenization.domain.model.paymentHandle.ShippingMethod;
import com.paysafe.android.tokenization.domain.model.paymentHandle.TransactionType;
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.DateOfBirth;
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.Gender;
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.IdentityDocument;
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.Profile;
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.ProfileLocale;
import com.paysafe.android.tokenization.domain.model.paymentHandle.venmo.VenmoRequest;
import com.paysafe.android.venmo.PSVenmoContext;
import com.paysafe.android.venmo.PSVenmoTokenizeCallback;
import com.paysafe.android.venmo.domain.model.PSVenmoConfig;
import com.paysafe.android.venmo.domain.model.PSVenmoTokenizeOptions;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;

import java.util.Collections;

public class PaysafeSDKService {
    private String apiKey = "c3V0LTM0ODg2MDpCLXFhMi0wLTVkM2VjYjMwLTEtMzAyYzAyMTQyYTM3NjgxMmE2YzJhYzRlNmQxMjI4NTYwNGMwNDAwNGU2NWI1YzI4MDIxNDU1N2EyNGFiNTcxZTJhOWU2MDVlNWQzMjk3MjZjMmIzZWNjNjJkNWY";
    private String venmoAccountId = "1002603510";
    private String currencyCode = "USD";

    private PSVenmoContext venmoContext = null;

    // Method to setup SDK
    public void setupSDK() {
        try {
            PaysafeSDK.INSTANCE.setup(apiKey, PSEnvironment.TEST);
            System.out.println("setup sdk successfully");
        } catch (PaysafeException | PaysafeRuntimeError e) {
            System.out.println("setup sdk failure " + e);
            throw new RuntimeException(e);
        }
    }

    // Method to initialize Venmo with a completion callback
    public void initializeVenmo(ComponentActivity activity, VenmoInitializationCallback completion) {

        PSVenmoConfig venmoConfig = new PSVenmoConfig(currencyCode, venmoAccountId);
        PSVenmoContext.Companion.initialize(activity, venmoConfig, new PSCallback<PSVenmoContext>() {
            @Override
            public void onSuccess(PSVenmoContext psVenmoContext) {
                System.out.println("initialize onSuccess");
                venmoContext = psVenmoContext;
                completion.onInitialized();
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.print("initialize onFailure: ");
                System.out.println(e);
            }
        });
    }

    // Method to tokenize with a consumerId and a completion callback
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void tokenize(Context context, String consumerId, VenmoTokenizationCallback completion) {
        int totalPrice = 1;
        PSVenmoTokenizeOptions tokenizeOptions = new PSVenmoTokenizeOptions(
            totalPrice * 100,
            currencyCode,
            TransactionType.PAYMENT,
            UUID.randomUUID().toString(),
            null,
            provideProfile(),
            venmoAccountId,
            provideMerchantDescriptor(),
            provideShippingDetails(),
            new VenmoRequest(consumerId, "", ""),
            "customScheme"
        );


        Continuation<Unit> continuation = getContinuation();

        venmoContext.tokenize(context, tokenizeOptions, new PSVenmoTokenizeCallback() {
            @Override
            public void onSuccess(@NonNull String s) {
                System.out.println("Tokenization finished successfully");
                System.out.println("paymentToken: " + s);
                completion.onTokenized(s);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Tokenization onFailure: " + e);
            }

            @Override
            public void onCancelled(@NonNull PaysafeException e) {
                System.out.println("Tokenization onCancelled: " + e);
            }
        }, continuation);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private static Continuation<Unit> getContinuation() {
        CompletableFuture<String> future = new CompletableFuture<>();
        return new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                if (o instanceof Result.Failure) {
                    future.completeExceptionally((((Result.Failure) o).exception));
                } else {
                    future.complete((String) o);
                }
            }
        };
    }

    private BillingDetails provideBillingDetails() {
        return new BillingDetails(
                "nickName",
                "street",
                "city",
                "AL",
                "US",
                "12345",
                null,
                null,
                null
        );
    }

    private Profile provideProfile() {
        return new Profile(
                "firstName",
                "lastName",
                ProfileLocale.EN_GB,
                "merchantCustomerId",
                new DateOfBirth(1, 1, 1990),
                "email@mail.com",
                "0123456789",
                "0123456789",
                Gender.MALE,
                "nationality",
                Collections.singletonList(new IdentityDocument("SSN123456"))
        );
    }

    private MerchantDescriptor provideMerchantDescriptor() {
        return new MerchantDescriptor(
                "dynamicDescriptor",
                "0123456789"
        );
    }

    private ShippingDetails provideShippingDetails() {
        return new ShippingDetails(
                ShippingMethod.NEXT_DAY_OR_OVERNIGHT,
                "street",
                "street2",
                "Marbury",
                "AL",
                "US",
                "36051"
        );
    }

}
