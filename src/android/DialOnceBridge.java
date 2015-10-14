package com.dialonce.sdk;

import org.apache.cordova.CordovaPlugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.dialonce.sdk.DialOnce;


public class DialOnceBridge extends CordovaPlugin {

    @Override protected void pluginInitialize() {
        this.initDialOnce();
    }

    @Override public void onStart() {
        //We also initialize Dial Once here just in case it has died. If Dial Once is already set up, this won't do anything.
        this.initDialOnce();
    }

    @Override public void onNewIntent(Intent intent) {
        cordova.getActivity().setIntent(intent);
    }

    private void initDialOnce() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                try {
                    Context context = cordova.getActivity().getApplicationContext();
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

                    DialOnce.init(context, applicationInfo.metaData.getString("DIALONCE_API_KEY"));
                } catch (Exception exception) {
                    System.err.println("[dialonce-cordova] Something went wrong when initializing DialOnce: " + exception.getMessage());
                    System.err.println("[dialonce-cordova] Have you set your ANDROID_API_KEY ?");
                }
            }
        });
    }
}