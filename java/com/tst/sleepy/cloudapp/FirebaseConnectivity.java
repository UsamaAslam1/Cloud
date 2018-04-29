package com.tst.sleepy.cloudapp;

import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;

public class FirebaseConnectivity extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
