package com.submarine.supersonic;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.supersonic.mediationsdk.logger.LogListener;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.io.IOException;

/**
 * Created by Gev on 3/2/2016.
 */
public class AndroidSuperSonicNetwork {
    public static final String TAG = AndroidSuperSonicNetwork.class.getSimpleName();
    //Declare the Supersonic Mediation Agent
    private Supersonic mMediationAgent;
    private Activity activity;



    public AndroidSuperSonicNetwork(Activity activity) {
        this.activity = activity;
        //Get the mediation publisher instance
        mMediationAgent = SupersonicFactory.getInstance();
        mMediationAgent.setInterstitialListener(mInterstitialListener);
        String mUserId = "";
        try {
            mUserId = AdvertisingIdClient.getAdvertisingIdInfo(activity).getId();
        } catch (IOException e) {
            Log.e(TAG, "Failed to load Client Advertising Id, IOException: " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "Failed to load Client Advertising Id, GooglePlayServicesNotAvailableException: " + e.getMessage());
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "Failed to load Client Advertising Id, GooglePlayServicesRepairableException: " + e.getMessage());
        }

        String mAppKey = "";

        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            mAppKey = bundle.getString("supersonic_app_key");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        mMediationAgent.initInterstitial(activity, mAppKey, mUserId);
    }


    InterstitialListener mInterstitialListener = new InterstitialListener()
    {

        @Override
        public void onInterstitialInitSuccess() {

        }

        @Override
        public void onInterstitialInitFailed(SupersonicError supersonicError) {

        }

        @Override
        public void onInterstitialReady() {

        }

        @Override
        public void onInterstitialLoadFailed(SupersonicError supersonicError) {

        }

        @Override
        public void onInterstitialOpen() {

        }

        @Override
        public void onInterstitialClose() {

        }

        @Override
        public void onInterstitialShowSuccess() {

        }

        @Override
        public void onInterstitialShowFailed(SupersonicError supersonicError) {

        }

        @Override
        public void onInterstitialClick() {

        }
    };

    public void enableLog(){
        mMediationAgent.setLogListener(new LogListener() {
            @Override
            public void onLog(SupersonicLogger.SupersonicTag tag, String message, int logLevel) {
                Gdx.app.log(tag.name(),message);
            }
        });
    }

    //This functions must be called from Activity on it's onResume and onPause
    public void onResume() {
        if (mMediationAgent != null) {
            mMediationAgent.onResume (activity);
        }
    }
    public void onPause() {
        if (mMediationAgent != null) {
            mMediationAgent.onPause(activity);
        }
    }
}
