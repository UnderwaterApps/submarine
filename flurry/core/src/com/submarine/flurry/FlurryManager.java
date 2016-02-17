package com.submarine.flurry;

import java.util.HashMap;

/**
 * Created by mariam on 1/8/16.
 */
public interface FlurryManager {

    public void init();

    public void logEvent(String eventName);

    public void logEvent(String eventName, HashMap<String, String> parameters);

    public void logEvent(String eventName, HashMap<String, String> parameters, boolean isTimedEvent);

    public void endTimedEvent(String eventName);

    public void endTimedEvent(String eventName, HashMap<String, String> parameters);
}
