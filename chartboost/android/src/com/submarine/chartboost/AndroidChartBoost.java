package com.submarine.chartboost;

import android.app.Activity;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Libraries.CBLogging;
import com.chartboost.sdk.Model.CBError;

import java.util.ArrayList;

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

            @Override
            public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
                super.didFailToLoadInterstitial(location, error);

                System.out.println("Failed to load interstitial: "+location+" "+error.toString());
            }
        };
    }

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {
        Chartboost.startWithAppId(activity, appId, appSignature);

        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
        Chartboost.setDelegate(delegate);

        Chartboost.onCreate(activity);

        for (String location : locations) {
            Chartboost.cacheInterstitial(location);
        }
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
