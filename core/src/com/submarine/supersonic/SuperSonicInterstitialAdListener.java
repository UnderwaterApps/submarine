package com.submarine.supersonic;

/**
 * Created by Gev on 3/3/2016.
 */
public interface SuperSonicInterstitialAdListener {
    void onInterstitialInitSuccess();

    void onInterstitialInitFailed(String error);

    void onInterstitialReady();

    void onInterstitialLoadFailed(String error);

    void onInterstitialOpen();

    void onInterstitialClose();

    void onInterstitialShowSuccess();

    void onInterstitialShowFailed(String error);

    void onInterstitialClick();
}
