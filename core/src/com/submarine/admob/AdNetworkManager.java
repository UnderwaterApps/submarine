package com.submarine.admob;

import com.badlogic.gdx.Gdx;

/**
 * Created by Gev on 2/24/2016.
 */
public class AdNetworkManager implements AdNetwork {
    public static final String TAG = AdNetworkManager.class.getSimpleName();
    private static AdNetworkManager instance = null;

    protected AdNetworkManager() {

    }

    public synchronized static AdNetworkManager getInstance() {
        if(instance == null) {
            instance = new AdNetworkManager();
        }
        return instance;
    }

    private AdNetwork adNetwork;

    public void setAdNetwork(AdNetwork adNetwork){
        this.adNetwork = adNetwork;
    }

    @Override
    public void showBanner() {
        if(adNetwork == null) {Gdx.app.log(TAG,"No AdNetwork is set");return;}
        adNetwork.showBanner();
    }

    @Override
    public void hideBanner() {
        if(adNetwork == null) {Gdx.app.log(TAG,"No AdNetwork is set");return;}
        adNetwork.hideBanner();
    }

    @Override
    public void showInterstitial() {
        if(adNetwork == null) {Gdx.app.log(TAG,"No AdNetwork is set");return;}
        adNetwork.showInterstitial();
    }

    @Override
    public void setInterstitialAdUnitId(String interstitialAdUnitId) {
        if(adNetwork == null) {Gdx.app.log(TAG,"No AdNetwork is set");return;}
        adNetwork.setInterstitialAdUnitId(interstitialAdUnitId);
    }

    @Override
    public void setAdViewUnitId(String adViewUnitId) {
        if(adNetwork == null) {Gdx.app.log(TAG,"No AdNetwork is set");return;}
        adNetwork.setAdViewUnitId(adViewUnitId);
    }
}
