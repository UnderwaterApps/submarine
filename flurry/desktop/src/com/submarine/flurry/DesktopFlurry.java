package com.submarine.flurry;


import java.util.HashMap;

/**
 * Created by mariam on 1/11/16.
 */
public class DesktopFlurry implements FlurryManager {

    private String LOG = "Flurry: ";

    public DesktopFlurry() {
    }

    @Override
    public void init() {

    }

    @Override
    public void logEvent(String eventName) {
        System.out.println(LOG+eventName);
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters) {
        System.out.println(LOG+eventName+" "+parameters.toString());
    }

    @Override
    public void logEvent(String eventName, HashMap<String, String> parameters, boolean isTimedEvent) {
        System.out.println(LOG+eventName+" "+parameters.toString());
    }

    @Override
    public void endTimedEvent(String eventName) {

    }

    @Override
    public void endTimedEvent(String eventName, HashMap<String, String> parameters) {

    }


}
