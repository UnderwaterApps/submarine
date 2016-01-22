package com.submarine.chartboost;

import java.util.ArrayList;

/**
 * Created by mariam on 1/8/16.
 */
public abstract class ChartBoostManager {

    public ArrayList<AgeGateListener> customListeners;

    public ChartBoostManager() {
        customListeners = new ArrayList<AgeGateListener>();
    }

    public abstract void onCreate(String appId, String appSignature, ArrayList<String> locations);

    public abstract void onStart();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestroy();

    public abstract boolean onBackPressed();

    public abstract boolean hasInterstitial(String locationName);

    public abstract void showInterstisial(String locationName);

    public abstract void cacheInterstisial(String locationName);

    public abstract void showMoreApps();

    public abstract void cacheMoreApps();

    public abstract void didPassAgeGate(boolean pass);

    public void addCustomListener(AgeGateListener listener) {
        customListeners.add(listener);
        System.out.println("Size: "+ customListeners.size());
    }

    public void removeCustomListener(AgeGateListener listener) {
        customListeners.remove(listener);
    }
}
