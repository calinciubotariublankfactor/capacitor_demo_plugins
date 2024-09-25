package com.mycompany.plugins.example;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.JSObject;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;

import java.util.concurrent.CompletableFuture;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;

@CapacitorPlugin(name = "SDKPlugin")
public class PaysafePluginPlugin extends Plugin {

    private final PaysafePlugin implementation = new PaysafePlugin();

    @Override
    public void load() {
        super.load();
        implementation.setupSDK();
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();

        AppCompatActivity activity = getActivity();

        implementation.initializeVenmo(activity, new VenmoInitializationCallback() {
            @Override
            public void onInitialized() {
                System.out.println("onInitialized call");
            }
        });
    }

    @PluginMethod
    public void echo(PluginCall call) {
        call.resolve();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @PluginMethod
    public void startVenmo(PluginCall call) {
        String consumerId = call.getString("consumerId");

        CompletableFuture<String> future = new CompletableFuture<>();
        Continuation<Unit> continuation = new Continuation<Unit>() {
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

        implementation.tokenize(getContext(), consumerId, new VenmoTokenizationCallback() {
            @Override
            public void onTokenized(String result) {
                JSObject ret = new JSObject();
                ret.put("paymentToken", result);
                call.resolve(ret);
            }
        }, continuation);
    }
}
