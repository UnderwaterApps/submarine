package com.submarine.chartboost;

import android.app.Activity;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Libraries.CBLogging;
import com.chartboost.sdk.Model.CBError;

import java.util.ArrayList;

/**
 * Created by mariam on 1/11/16.
 */
public class AndroidChartBoost extends ChartBoostManager {

    private Activity activity;
    private ChartboostDelegate delegate;
//    private long beforeTime;


    public AndroidChartBoost(final Activity activity) {
        this.activity = activity;

        delegate = new ChartboostDelegate() {
            //Override the Chartboost delegate callbacks you wi.sh to track and control

            @Override
            public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
                super.didFailToLoadInterstitial(location, error);

                System.out.println("Failed to load interstitial: "+location+" "+error.toString());
            }

            @Override
            public void didPauseClickForConfirmation(Activity activity) {
                super.didPauseClickForConfirmation(activity);

//                System.out.println("did pause click for confirm");
                for (AgeGateListener listener : customListeners) {
                    listener.startCheck();
                }
                Chartboost.closeImpression();
            }

            //            @Override
//            public void didDisplayInterstitial(String location) {
//                super.didDisplayInterstitial(location);
//
//                long after = TimeUtils.millis();
//                System.out.println("Display interstitial on location: "+location+" "+ "Time: "+(after - beforeTime));
//            }
//
//            @Override
//            public void didCacheInterstitial(String location) {
//                super.didCacheInterstitial(location);
//
//                long after = TimeUtils.millis();
//                System.out.println("Cache interstitial on location: "+location+" "+ "Time: "+(after - beforeTime));
//            }
        };
    }

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {
        Chartboost.startWithAppId(activity, appId, appSignature);

        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
        Chartboost.setDelegate(delegate);
        Chartboost.setShouldPauseClickForConfirmation(true);

        Chartboost.onCreate(activity);

//        beforeTime = TimeUtils.millis();
        for (String location : locations) {
            Chartboost.cacheInterstitial(location);
        }
        cacheMoreApps();
    }

    public void onStart() {
        Chartboost.onStart(activity);
    }

    public void onResume() {
        Chartboost.onResume(activity);
    }

    public void onPause() {
        Chartboost.onPause(activity);
    }

    public void onStop() {
        Chartboost.onStop(activity);
    }

    public void onDestroy() {
        Chartboost.onDestroy(activity);
    }

    public boolean onBackPressed() {
        return Chartboost.onBackPressed();
    }

    @Override
    public boolean hasInterstitial(String locationName) {
        return Chartboost.hasInterstitial(locationName);
    }

    @Override
    public void showInterstisial(String locationName) {
        Chartboost.showInterstitial(locationName);
    }

    @Override
    public void cacheInterstisial(String locationName) {
//        beforeTime = TimeUtils.millis();
        Chartboost.cacheInterstitial(locationName);
    }

    @Override
    public boolean hasMoreApps() {
        return Chartboost.hasMoreApps(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public void showMoreApps() {
        Chartboost.showMoreApps(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public void cacheMoreApps() {
        Chartboost.cacheMoreApps(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public void didPassAgeGate(boolean pass) {
        Chartboost.didPassAgeGate(pass);
    }
}
