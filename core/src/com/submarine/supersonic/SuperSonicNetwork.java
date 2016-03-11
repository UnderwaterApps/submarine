package com.submarine.supersonic;

/**
 * Created by Gev on 3/3/2016.
 */
public interface SuperSonicNetwork {

    void initInterstitialManager();

    void initRewardedVideoManager();

    SuperSonicInterstitial getInterstitialManager();

    SuperSonicRewardedVideo getRewardedVideoManager();
    
}
