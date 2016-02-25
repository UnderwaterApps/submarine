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

    public abstract boolean hasInterstitial(String locationName);

    public abstract void showInterstisial(String locationName);

    public abstract void cacheInterstisial(String locationName);

    public abstract boolean hasMoreApps();

    public abstract void showMoreApps();

    public abstract void cacheMoreApps();

    public abstract void didPassAgeGate(boolean pass);

    public void addCustomListener(AgeGateListener listener) {
        customListeners.add(listener);
    }

    public void removeCustomListener(AgeGateListener listener) {
        customListeners.remove(listener);
    }
}
