package com.mycompany.plugins.example;

import android.content.Context;
import android.util.Log;

import androidx.activity.ComponentActivity;

import com.paysafe.android.core.domain.exception.PaysafeException;
import com.paysafe.android.core.domain.exception.PaysafeRuntimeError;


public class PaysafePlugin {
    private final PaysafeSDKService paysafeSDKService = new PaysafeSDKService();

    public void setupSDK() {
        paysafeSDKService.setupSDK();
    }

    public void initializeVenmo(ComponentActivity activity, VenmoInitializationCallback completion) {
        paysafeSDKService.initializeVenmo(activity, completion);
    }

    // Method to tokenize with a consumerId and a completion callback
    public void tokenize(Context context, String consumerId, VenmoTokenizationCallback completion) {
        paysafeSDKService.tokenize(context, consumerId, completion);
    }
}

