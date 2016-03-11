package com.submarine.supersonic;

/**
 * Created by Gev on 3/4/2016.
 */
public interface SuperSonicInterstitial {

    void setAdlistener(SuperSonicInterstitialAdListener listener);

    void loadInterstitial();

    void showInterstitial();

    void showInterstitial(String placementName);

}
