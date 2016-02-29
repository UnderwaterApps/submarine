package com.submarine.flurry;


import org.robovm.pods.flurry.analytics.Flurry;

import java.util.HashMap;

/**
 * Created by mariam on 1/11/16.
 */
public class IOSFlurry implements FlurryManager {

    private String FLURRY_API_KEY;

    public IOSFlurry(String appId) {
        FLURRY_API_KEY = appId;

        init();
    }

    @Override
    public void init() {
        Flurry.setDebugLogEnabled(true);
        Flurry.startSession(FLURRY_API_KEY);
    }

    @Override
    public void logEvent(String eventName) {
        Flurry.logEvent(eventName);
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters) {
        Flurry.logEvent(eventName, parameters);
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters, boolean isTimedEvent) {
        Flurry.logEvent(eventName, parameters, isTimedEvent);
    }

    @Override
    public void endTimedEvent(String eventName) {
        Flurry.endTimedEvent(eventName);
    }

    @Override
    public void endTimedEvent(String eventName, HashMap<String, String> parameters) {
        Flurry.endTimedEvent(eventName, parameters);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
