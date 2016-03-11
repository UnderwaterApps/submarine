package com.submarine.supersonic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.supersonic.mediationsdk.logger.LogListener;
import com.supersonic.mediationsdk.logger.SupersonicLogger;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;

import java.util.UUID;

/**
 * Created by Gev on 3/2/2016.
 */
public class AndroidSuperSonicNetwork implements SuperSonicNetwork {
    public static final String TAG = AndroidSuperSonicNetwork.class.getSimpleName();
    private final String PREFS_NAME = "SuperSonicPrefs";
    private final String UUID_KEY =  "UUID";
    //Declare the Supersonic Mediation Agent
    private Supersonic mMediationAgent;
    private Activity activity;
    private String mUserId = "";
    private String mAppKey = "";
    private SuperSonicInterstitialManager superSonicInterstitialManager;
    private SuperSonicRewardedVideoManager superSonicRewardedVideoManager;

    public AndroidSuperSonicNetwork(Activity activity) {

        this.activity = activity;
        //Get the mediation publisher instance
        mMediationAgent = SupersonicFactory.getInstance();

        //Load/Save or Generate Universally unique identifier for Super Sonic
        this.mUserId = getUUID();

        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(this.activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            this.mAppKey = bundle.getString("supersonic_app_key");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

    }

    public void initInterstitialManager(){
        superSonicInterstitialManager = new SuperSonicInterstitialManager(activity, mMediationAgent, mUserId, mAppKey);
    }

    public void initRewardedVideoManager(){
        superSonicRewardedVideoManager = new SuperSonicRewardedVideoManager(activity, mMediationAgent, mUserId, mAppKey);
    }

    @Override
    public SuperSonicInterstitial getInterstitialManager() {
        return superSonicInterstitialManager;
    }

    @Override
    public SuperSonicRewardedVideo getRewardedVideoManager() {
        return superSonicRewardedVideoManager;
    }

    //Universally unique identifier
    private String getUUID(){
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        String uuid = prefs.getString(UUID_KEY, null);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString() + Long.toHexString(System.currentTimeMillis());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(UUID_KEY, uuid);
        }
        return uuid;
    }

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
