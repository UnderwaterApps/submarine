package com.submarine.admob;

/**
 * Created by Gev on 2/24/2016.
 */
public interface InterstitialAdListener {

    void onAdClosed();

    void onAdFailedToLoad(int errorCode);

    void onAdLeftApplication();

    void onAdOpened();

    void onAdLoaded();

}
