package com.submarine.adcolony;

/**
 * Created by mariam on 3/9/15.
 */
public interface AdColonyNetwork {

    void showVideoAd();

    void showVideoAd(String zoneId);

    void showV4VCAd();

    void showV4VCAd(String zoneId);

    void showV4VCAd(String zoneId, AdColonyAdListener adColonyAdListener);

    void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup);

    void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup, AdColonyAdListener adColonyAdListener);

    void showV4VCAd(String zoneId, boolean showPrePopup);

    void showV4VCAd(String zoneId, boolean showPrePopup, AdColonyAdListener adColonyAdListener);

}
