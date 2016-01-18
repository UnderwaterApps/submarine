package com.submarine.chartboost;

import org.robovm.pods.chartboost.CBLocation;
import org.robovm.pods.chartboost.Chartboost;
import org.robovm.pods.chartboost.ChartboostDelegate;

import java.util.ArrayList;

/**
 * Created by mariam on 1/11/16.
 */
public class IOSChartBoost implements ChartBoostListener {

    private ChartboostDelegate delegate;

    public IOSChartBoost(ChartboostDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {

        Chartboost.start(appId, appSignature, delegate);
        Chartboost.setShouldRequestInterstitialsInFirstSession(false);

        for (String location : locations) {
            System.out.println("name: "+location);
            Chartboost.cacheInterstitial(location);
        }
        System.out.println("cache more apps!");
        Chartboost.cacheMoreApps(CBLocation.Default);
    }

    @Override
    public boolean hasInterstitial(String locationName) {
        return Chartboost.hasInterstitial(locationName);
    }

    @Override
    public void showInterstisial(String locationName) {
        System.out.println("show "+locationName+" interstitial");
        Chartboost.showInterstitial(locationName);
    }

    @Override
    public void cacheInterstisial(String locationName) {
        Chartboost.cacheInterstitial(locationName);
    }

    @Override
    public void showMoreApps() {
        System.out.println("show more apps");
        Chartboost.showMoreApps(CBLocation.Default);
    }

    @Override
    public void cacheMoreApps() {
        Chartboost.cacheMoreApps(CBLocation.Default);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
