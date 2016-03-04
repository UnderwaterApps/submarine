package com.submarine.supersonic;

import android.app.Activity;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.model.RewardedVideoPlacement;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;

/**
 * Created by Gev on 3/4/2016.
 */
public class SuperSonicRewardedVideoManager implements SuperSonicRewardedVideo{
    public static final String TAG = SuperSonicRewardedVideoManager.class.getSimpleName();

    private Supersonic mMediationAgent;
    private Activity activity;

    public SuperSonicRewardedVideoManager(Activity activity, Supersonic mMediationAgent, String mUserId, String mAppKey) {
        this.activity = activity;
        this.mMediationAgent = mMediationAgent;

        this.mMediationAgent.setRewardedVideoListener(mRewardedVideoListener);
        //Init Rewarded Video
        this.mMediationAgent.initRewardedVideo(this.activity, mAppKey, mUserId);
    }

    RewardedVideoListener mRewardedVideoListener = new RewardedVideoListener() {

        @Override
        public void onRewardedVideoInitSuccess() {

        }

        @Override
        public void onRewardedVideoInitFail(SupersonicError supersonicError) {

        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoAdClosed() {

        }

        @Override
        public void onVideoAvailabilityChanged(boolean b) {

        }

        @Override
        public void onVideoStart() {

        }

        @Override
        public void onVideoEnd() {

        }

        @Override
        public void onRewardedVideoAdRewarded(RewardedVideoPlacement rewardedVideoPlacement) {

        }

        @Override
        public void onRewardedVideoShowFail(SupersonicError supersonicError) {

        }
    };

    @Override
    public boolean isRewardedVideoAvailable() {
        return mMediationAgent.isRewardedVideoAvailable();
    }

    //@Override
    public void showRewardedVideo() {
        mMediationAgent.showRewardedVideo();
    }

    @Override
    public void showRewardedVideo(String placementName) {
        mMediationAgent.showRewardedVideo(placementName);
    }
}
