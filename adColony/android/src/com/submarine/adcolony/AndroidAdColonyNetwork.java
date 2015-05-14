package com.submarine.adcolony;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jirbo.adcolony.*;

/**
 * Created by mariam on 3/9/15.
 */
public class AndroidAdColonyNetwork implements AdColonyNetwork {

    private final AndroidApplication androidApplication;
    private AdColonyRewardListener adColonyRewardListener;
    private final AndroidAdColonyAdListener internalAdColonyAdListener;


    public AndroidAdColonyNetwork(AndroidApplication androidApplication, String clientOptions, String appId, String[] zoneIds) {
        this.androidApplication = androidApplication;
        this.internalAdColonyAdListener = new AndroidAdColonyAdListener();
        configure(clientOptions, appId, zoneIds);
    }

    private void configure(String clientOptions, String appId, String[] zoneIds) {
        AdColony.configure(androidApplication, clientOptions, appId, zoneIds);
        AdColony.addV4VCListener(new AndroidAdColonyV4VCListener());
    }

    public void pause() {
        AdColony.pause();
    }

    public void resume() {
        AdColony.resume(androidApplication);
    }

    @Override
    public void showVideoAd() {
        AdColonyVideoAd ad = new AdColonyVideoAd();
        ad.show();
    }

    @Override
    public void showVideoAd(String zoneId) {
        AdColonyVideoAd ad = new AdColonyVideoAd(zoneId);
        ad.show();
    }

    @Override
    public void showV4VCAd() {
        AdColonyV4VCAd ad = new AdColonyV4VCAd();
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId, AdColonyAdListener adColonyAdListener) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        internalAdColonyAdListener.adColonyAdListener = adColonyAdListener;
        ad.withListener(internalAdColonyAdListener);
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        if (showPrePopup) {
            ad.withConfirmationDialog();
        }
        if (showPostPopup) {
            ad.withResultsDialog();
        }
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, boolean showPostPopup, AdColonyAdListener adColonyAdListener) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        if (showPrePopup) {
            ad.withConfirmationDialog();
        }
        if (showPostPopup) {
            ad.withResultsDialog();
        }
        internalAdColonyAdListener.adColonyAdListener = adColonyAdListener;
        ad.withListener(internalAdColonyAdListener);
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        if (showPrePopup) {
            ad.withConfirmationDialog();
        }
        ad.show();
    }

    @Override
    public void showV4VCAd(String zoneId, boolean showPrePopup, AdColonyAdListener adColonyAdListener) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(zoneId);
        if (showPrePopup) {
            ad.withConfirmationDialog();
        }
        internalAdColonyAdListener.adColonyAdListener = adColonyAdListener;
        ad.withListener(internalAdColonyAdListener);
        ad.show();
    }

    @Override
    public void setAdColonyRewardListener(AdColonyRewardListener adColonyRewardListener) {
        this.adColonyRewardListener = adColonyRewardListener;
    }


    private class AndroidAdColonyV4VCListener implements AdColonyV4VCListener {
        @Override
        public void onAdColonyV4VCReward(AdColonyV4VCReward adColonyV4VCReward) {
            adColonyRewardListener.reward(adColonyV4VCReward.success(), adColonyV4VCReward.name(), adColonyV4VCReward.amount());
        }
    }

    private class AndroidAdColonyAdListener implements com.jirbo.adcolony.AdColonyAdListener {

        private AdColonyAdListener adColonyAdListener;

        @Override
        public void onAdColonyAdAttemptFinished(AdColonyAd adColonyAd) {
            adColonyAdListener.onAdAttemptFinished(adColonyAd.shown(), null);
        }

        @Override
        public void onAdColonyAdStarted(AdColonyAd adColonyAd) {
            adColonyAdListener.onAdStarted(null);
        }
    }
}
