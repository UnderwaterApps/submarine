package com.submarine.chartboost;

import java.util.ArrayList;

/**
 * Created by mariam on 1/11/16.
 */
public class DesktopChartBoost extends ChartBoostManager {

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {

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
    public boolean hasMoreApps() {
        return false;
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
