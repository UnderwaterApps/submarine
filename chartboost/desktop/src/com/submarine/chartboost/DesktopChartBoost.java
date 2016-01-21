package com.submarine.chartboost;

import java.util.ArrayList;

/**
 * Created by mariam on 1/11/16.
 */
public class DesktopChartBoost implements ChartBoostListener {

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {

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

    @Override
    public boolean hasInterstitial(String locationName) {
        return false;
    }

    @Override
    public void showInterstisial(String locationName) {
        System.out.println("Location name = "+locationName);
    }

    @Override
    public void cacheInterstisial(String locationName) {

    }

    @Override
    public void showMoreApps() {
        System.out.println("show more apps");
    }

    @Override
    public void cacheMoreApps() {

    }

    @Override
    public void didPassAgeGate(boolean pass) {

    }
}
