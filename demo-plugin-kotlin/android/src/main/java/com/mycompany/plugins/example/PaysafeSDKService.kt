package com.mycompany.plugins.example

import android.content.Context
import androidx.activity.ComponentActivity
import com.paysafe.android.PaysafeSDK.setup
import com.paysafe.android.core.data.entity.PSCallback
import com.paysafe.android.core.domain.exception.PaysafeException
import com.paysafe.android.core.domain.exception.PaysafeRuntimeError
import com.paysafe.android.core.domain.model.config.PSEnvironment
import com.paysafe.android.tokenization.domain.model.paymentHandle.BillingDetails
import com.paysafe.android.tokenization.domain.model.paymentHandle.MerchantDescriptor
import com.paysafe.android.tokenization.domain.model.paymentHandle.ShippingDetails
import com.paysafe.android.tokenization.domain.model.paymentHandle.ShippingMethod
import com.paysafe.android.tokenization.domain.model.paymentHandle.TransactionType
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.DateOfBirth
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.Gender
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.IdentityDocument
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.Profile
import com.paysafe.android.tokenization.domain.model.paymentHandle.profile.ProfileLocale
import com.paysafe.android.tokenization.domain.model.paymentHandle.venmo.VenmoRequest
import com.paysafe.android.venmo.PSVenmoContext
import com.paysafe.android.venmo.PSVenmoTokenizeCallback
import com.paysafe.android.venmo.domain.model.PSVenmoConfig
import com.paysafe.android.venmo.domain.model.PSVenmoTokenizeOptions
import java.util.UUID

class PaysafeSDKService {
    private val apiKey =
        "c3V0LTM0ODg2MDpCLXFhMi0wLTVkM2VjYjMwLTEtMzAyYzAyMTQyYTM3NjgxMmE2YzJhYzRlNmQxMjI4NTYwNGMwNDAwNGU2NWI1YzI4MDIxNDU1N2EyNGFiNTcxZTJhOWU2MDVlNWQzMjk3MjZjMmIzZWNjNjJkNWY"
    private val venmoAccountId = "1002603510"
    private val currencyCode = "USD"

    private var venmoContext: PSVenmoContext? = null

    // Method to setup SDK
    fun setupSDK() {
        try {
            setup(apiKey, PSEnvironment.TEST)
            println("setup sdk successfully")
        } catch (e: PaysafeException) {
            println("setup sdk failure $e")
            throw RuntimeException(e)
        } catch (e: PaysafeRuntimeError) {
            println("setup sdk failure $e")
            throw RuntimeException(e)
        }
    }

    // Method to initialize Venmo with a completion callback
    fun initializeVenmo(activity: ComponentActivity, completion: () -> Unit) {

        val venmoConfig = PSVenmoConfig(currencyCode, venmoAccountId)

        PSVenmoContext.initialize(activity, venmoConfig, object : PSCallback<PSVenmoContext> {
            override fun onSuccess(value: PSVenmoContext) {
                println("initialize onSuccess $value")
                venmoContext = value
                completion()
            }

            override fun onFailure(e: Exception) {
                print("initialize onFailure: ")
                println(e)
            }
        })
    }

    // Method to tokenize with a consumerId and a completion callback
    suspend fun tokenize(context: Context?, consumerId: String?, completion: (String) -> Unit) {
        val totalPrice = 1
        val tokenizeOptions = PSVenmoTokenizeOptions(
            totalPrice * 100,
            currencyCode,
            TransactionType.PAYMENT,
            UUID.randomUUID().toString(),
            null,
            provideProfile(),
            venmoAccountId,
            provideMerchantDescriptor(),
            provideShippingDetails(),
            VenmoRequest(consumerId!!, "", ""),
            ""
        )

        if (venmoContext == null) {
            println("venmocontext is null.")
            return
        }

        venmoContext!!.tokenize(context!!, tokenizeOptions, object : PSVenmoTokenizeCallback {
            override fun onSuccess(s: String) {
                println("Tokenization finished successfully")
                println("paymentToken: $s")
                completion(s)
            }

            override fun onFailure(e: Exception) {
                println("Tokenization onFailure: $e")
            }

            override fun onCancelled(e: PaysafeException) {
                println("Tokenization onCancelled: $e")
            }
        })
    }

    private fun provideProfile(): Profile {
        return Profile(
            "firstName",
            "lastName",
            ProfileLocale.EN_GB,
            "merchantCustomerId",
            DateOfBirth(1, 1, 1990),
            "email@mail.com",
            "0123456789",
            "0123456789",
            Gender.MALE,
            "nationality",
            listOf(IdentityDocument("SSN123456"))
        )
    }

    private fun provideMerchantDescriptor(): MerchantDescriptor {
        return MerchantDescriptor(
            "dynamicDescriptor",
            "0123456789"
        )
    }

    private fun provideShippingDetails(): ShippingDetails {
        return ShippingDetails(
            ShippingMethod.NEXT_DAY_OR_OVERNIGHT,
            "street",
            "street2",
            "Marbury",
            "AL",
            "US",
            "36051"
        )
    }
}
