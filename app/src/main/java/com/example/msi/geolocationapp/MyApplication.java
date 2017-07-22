package com.example.msi.geolocationapp;

import io.skygear.skygear.SkygearApplication;

/**
 * Created by MSI on 2017/7/22.
 */

public class MyApplication extends SkygearApplication {
    @Override
    public String getSkygearEndpoint() {
        return "https://geolocationapp.skygeario.com/";
    }

    @Override
    public String getApiKey() {
        return "3d85024dff4e45fbbfbc0885c0a74882";
    }
}
