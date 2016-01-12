package com.submarine.chartboost;

import android.app.Activity;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

/**
 * Created by mariam on 1/11/16.
 */
public class AndroidChartBoost implements ChartBoostListener {

    private Activity activity;
    private ChartboostDelegate delegate;

    public AndroidChartBoost(Activity activity) {
        this.activity = activity;

        delegate = new ChartboostDelegate() {
            //Override the Chartboost delegate callbacks you wish to track and control
        };
    }

    @Override
    public void onCreate(String appId, String appSignature) {
        Chartboost.startWithAppId(activity, appId, appSignature);
        Chartboost.setDelegate(delegate);
        Chartboost.onCreate(activity);
    }

    @Override
    public void onStart() {
        Chartboost.onStart(activity);
    }

    @Override
    public void onResume() {
        Chartboost.onResume(activity);
    }

    @Override
    public void onPause() {
        Chartboost.onPause(activity);
    }

    @Override
    public void onStop() {
        Chartboost.onStop(activity);
    }

    @Override
    public void onDestroy() {
        Chartboost.onDestroy(activity);
    }

    @Override
    public boolean onBackPressed() {
        return Chartboost.onBackPressed();
    }

    @Override
    public void showInterstisial(String locationName) {
        Chartboost.showInterstitial(locationName);
    }

    @Override
    public void cacheInterstisial(String locationName) {
        Chartboost.cacheInterstitial(locationName);
    }
}
