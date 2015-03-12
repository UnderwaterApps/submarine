package com.submarine.adcolony;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jirbo.adcolony.*;

/**
 * Created by mariam on 3/9/15.
 */
public class AndroidAdColonyNetwork implements AdColonyNetwork {

    private final AndroidApplication androidApplication;
    private String clientOptions;
    private String appId;
    private String[] zoneIds;
    private AdColonyListener listener;


    public AndroidAdColonyNetwork(AndroidApplication androidApplication, String clientOptions, String appId, String[] zoneIds, AdColonyListener listener) {
        this.androidApplication = androidApplication;
        this.clientOptions = clientOptions;
        this.appId = appId;
        this.zoneIds = zoneIds;
        this.listener = listener;

        configureAdColony();
    }

    private void configureAdColony() {
        AdColony.configure(androidApplication, clientOptions, appId, zoneIds);


        AdColonyV4VCListener listener = new AdColonyV4VCListener() {
            public void onAdColonyV4VCReward(AdColonyV4VCReward reward) {
                if (reward.success()) {
                    AndroidAdColonyNetwork.this.listener.reward();
                }
            }
        };
        AdColony.addV4VCListener(listener);
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
    public void showPrePopups(String v4vcZoneId) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(v4vcZoneId).withConfirmationDialog();
        ad.show();
    }

    @Override
    public void showPostPopups(String v4vcZoneId) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(v4vcZoneId).withResultsDialog();
        ad.show();
    }

    @Override
    public void showBothPopups(String v4vcZoneId) {
        AdColonyV4VCAd ad = new AdColonyV4VCAd(v4vcZoneId).withConfirmationDialog().withResultsDialog();
        ad.show();
    }
}
