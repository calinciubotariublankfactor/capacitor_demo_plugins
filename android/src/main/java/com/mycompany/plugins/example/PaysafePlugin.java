package com.mycompany.plugins.example;

import android.util.Log;

public class PaysafePlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
