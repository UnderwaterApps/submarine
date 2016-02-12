package com.submarine.adcolony;

/**
 * Created by sargis on 3/13/15.
 */
public interface AdColonyAdListener {
    void onAdStarted(String zoneID);

    void onAdAttemptFinished(boolean shown, String zoneID);
}
