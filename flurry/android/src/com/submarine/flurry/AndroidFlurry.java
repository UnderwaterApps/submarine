package com.submarine.flurry;

import android.app.Activity;
import com.flurry.android.FlurryAgent;

import java.util.HashMap;

/**
 * Created by mariam on 1/11/16.
 */
public class AndroidFlurry implements FlurryManager {

    private Activity activity;
    private String FLURRY_APP_ID;

    public AndroidFlurry(Activity activity, String appId) {
        this.activity = activity;
        FLURRY_APP_ID = appId;

        init();
    }

    @Override
    public void init() {
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.init(activity, FLURRY_APP_ID);
    }

    @Override
    public void logEvent(String eventName) {
        FlurryAgent.logEvent(eventName);
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters) {
        FlurryAgent.logEvent(eventName, parameters);
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters, boolean isTimedEvent) {
        FlurryAgent.logEvent(eventName, parameters, isTimedEvent);
    }

    @Override
    public void endTimedEvent(String eventName) {
        FlurryAgent.endTimedEvent(eventName);
    }

    @Override
    public void endTimedEvent(String eventName, HashMap<String, String> parameters) {
        FlurryAgent.endTimedEvent(eventName, parameters);
    }
}
