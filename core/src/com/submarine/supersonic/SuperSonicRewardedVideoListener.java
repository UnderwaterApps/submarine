package com.submarine.supersonic;

/**
 * Created by Gev on 3/3/2016.
 */
public interface SuperSonicRewardedVideoListener {


    void onRewardedVideoInitSuccess();

    void onRewardedVideoInitFail(String supersonicError);

    void onRewardedVideoAdOpened();

    void onRewardedVideoAdClosed();

    void onVideoAvailabilityChanged(boolean b);

    void onVideoStart();

    void onVideoEnd();

    void onRewardedVideoAdRewarded(String rewardedVideoPlacement);

    void onRewardedVideoShowFail(String supersonicError);

}
