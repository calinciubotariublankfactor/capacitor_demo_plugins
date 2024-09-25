package com.mycompany.plugins.example

import android.content.Context
import androidx.activity.ComponentActivity
import kotlin.coroutines.Continuation

class PaysafePlugin {
    private val paysafeSDKService = PaysafeSDKService()

    fun setupSDK() {
        paysafeSDKService.setupSDK()
    }

    fun initializeVenmo(activity: ComponentActivity, completion: VenmoInitializationCallback) {
        paysafeSDKService.initializeVenmo(activity) {
            completion.onInitialized()
        }
    }

    // Method to tokenize with a consumerId and a completion callback
    suspend fun tokenize(context: Context?, consumerId: String?, completion: VenmoTokenizationCallback) {
        paysafeSDKService.tokenize(context, consumerId) {
            completion.onTokenized(it)
        }
    }
}

