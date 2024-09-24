package com.mycompany.plugins.example;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "PaysafePlugin")
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
        implementation.initializeVenmo(getActivity(), new VenmoInitializationCallback() {
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

    @PluginMethod
    public void startVenmo(PluginCall call) {
        String consumerId = call.getString("consumerId");

        implementation.tokenize(getContext(), consumerId, new VenmoTokenizationCallback() {
            @Override
            public void onTokenized(String result) {
                JSObject ret = new JSObject();
                ret.put("paymentToken", result);
                call.resolve(ret);
            }
        });
    }
}
