package com.submarine.adcolony;

/**
 * Created by mariam on 3/9/15.
 */
public interface AdColonyNetwork {

    void showVideoAd();

    void showVideoAd(String zoneId);

    void showV4VCAd();

    void showV4VCAd(String zoneId);

    void showPrePopups(String v4vcZoneId);

    void showPostPopups(String v4vcZoneId);

    void showBothPopups(String v4vcZoneId);
}
