package com.submarine.adcolony;

import org.robovm.apple.foundation.NSArray;
import org.robovm.bindings.adcolony.AdColony;
import org.robovm.bindings.adcolony.AdColonyAdDelegateAdapter;
import org.robovm.bindings.adcolony.AdColonyDelegateAdapter;

/**
 * Created by mariam on 3/9/15.
 */
public class IOSAdColonyNetwork implements AdColonyNetwork {
    private final AdColonyRewardListener adColonyRewardListener;
    private final IOSAdColonyDelegate adColonyDelegate;
    private final IOSAdColonyAdDelegate adColonyAdDelegate;

    public IOSAdColonyNetwork(String appId, String[] zoneIds, AdColonyRewardListener adColonyRewardListener) {
        this.adColonyRewardListener = adColonyRewardListener;
        adColonyDelegate = new IOSAdColonyDelegate();
        adColonyAdDelegate = new IOSAdColonyAdDelegate();
        configure(appId, zoneIds);
    }

    private void configure(String appId, String[] zoneIds) {
        AdColony.configure(appId, NSArray.fromStrings(zoneIds), adColonyDelegate, true);
    }

    @Override
    public void showVideoAd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showVideoAd(String zoneId) {
        AdColony.playVideoAd(zoneId, null);
    }

    @Override
    public void showV4VCAd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showV4VCAd(String zoneId) {
        AdColony.playVideoAd(zoneId, null, false, false);
    }

    @Override
    public void showV4VCAd(String zoneId, AdColonyAdListener adColonyAdListener) {
        adColonyAdDelegate.adColonyAdListener = adColonyAdListener;
        AdColony.playVideoAd(zoneId, adColonyAdDelegate, false, false);
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup) {
        AdColony.playVideoAd(zoneId, null, showPrePopup, showPostPopup);
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup, AdColonyAdListener adColonyAdListener) {
        adColonyAdDelegate.adColonyAdListener = adColonyAdListener;
        AdColony.playVideoAd(zoneId, adColonyAdDelegate, showPrePopup, showPostPopup);
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup) {
        AdColony.playVideoAd(zoneId, null, showPrePopup, false);
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, AdColonyAdListener adColonyAdListener) {
        adColonyAdDelegate.adColonyAdListener = adColonyAdListener;
        AdColony.playVideoAd(zoneId, adColonyAdDelegate, showPrePopup, false);
    }

    @Override
    public void setAdColonyRewardListener(AdColonyRewardListener adColonyRewardListener) {

    }

    @Override
    public void setAdColonyLoadingListener(AdColonyAdLoadingListener adColonyAdLoadingListener) {

    }

    private class IOSAdColonyDelegate extends AdColonyDelegateAdapter {

        @Override
        public void onAdAvailabilityChange(boolean available, String zoneID) {

        }

        @Override
        public void onV4VCReward(boolean success, String currencyName, int amount, String zoneID) {
            adColonyRewardListener.reward(success, currencyName, amount);
        }
    }

    private class IOSAdColonyAdDelegate extends AdColonyAdDelegateAdapter {
        private AdColonyAdListener adColonyAdListener;

        @Override
        public void onAdStartedInZone(String zoneID) {
            adColonyAdListener.onAdStarted(zoneID);
        }

        @Override
        public void onAdAttemptFinished(boolean shown, String zoneID) {
            adColonyAdListener.onAdAttemptFinished(shown, zoneID);
        }
    }
}
