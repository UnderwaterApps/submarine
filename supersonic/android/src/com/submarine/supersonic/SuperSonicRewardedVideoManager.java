package com.submarine.supersonic;

import android.app.Activity;
import com.submarine.supersonic.utils.NetworkConnectivityListener;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.model.RewardedVideoPlacement;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.Supersonic;

/**
 * Created by Gev on 3/4/2016.
 */
public class SuperSonicRewardedVideoManager implements SuperSonicRewardedVideo{
    public static final String TAG = SuperSonicRewardedVideoManager.class.getSimpleName();
    private final String mAppKey;
    private final String mUserId;

    private Supersonic mMediationAgent;
    private Activity activity;
    private SuperSonicRewardedVideoListener rewardedVideoListener;
    private NetworkConnectivityListener listener;

    public SuperSonicRewardedVideoManager(Activity activity, Supersonic mMediationAgent, String mUserId, String mAppKey) {
        this.mAppKey = mAppKey;
        this.mUserId = mUserId;
        this.activity = activity;
        this.mMediationAgent = mMediationAgent;

        this.mMediationAgent.setRewardedVideoListener(mRewardedVideoListener);
        //Init Rewarded Video
        this.mMediationAgent.initRewardedVideo(this.activity, this.mAppKey, this.mUserId);
    }

    RewardedVideoListener mRewardedVideoListener = new RewardedVideoListener() {

        @Override
        public void onRewardedVideoInitSuccess() {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoInitSuccess();
        }

        @Override
        public void onRewardedVideoInitFail(SupersonicError supersonicError) {
            startNetworkMonitor();
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoInitFail(supersonicError.getErrorMessage());
        }

        @Override
        public void onRewardedVideoAdOpened() {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoAdOpened();
        }

        @Override
        public void onRewardedVideoAdClosed() {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoAdClosed();
        }

        @Override
        public void onVideoAvailabilityChanged(boolean b) {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onVideoAvailabilityChanged(b);
        }

        @Override
        public void onVideoStart() {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onVideoStart();
        }

        @Override
        public void onVideoEnd() {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onVideoEnd();
        }

        @Override
        public void onRewardedVideoAdRewarded(RewardedVideoPlacement rewardedVideoPlacement) {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoAdRewarded(rewardedVideoPlacement.getPlacementName());
        }

        @Override
        public void onRewardedVideoShowFail(SupersonicError supersonicError) {
            if(rewardedVideoListener == null) return;
            rewardedVideoListener.onRewardedVideoShowFail(supersonicError.getErrorMessage());
        }
    };

    @Override
    public void setRewardedVideoListener(SuperSonicRewardedVideoListener listener){
        this.rewardedVideoListener = listener;
    }

    @Override
    public boolean isRewardedVideoAvailable() {
        return mMediationAgent.isRewardedVideoAvailable();
    }

    @Override
    public void showRewardedVideo() {
        mMediationAgent.showRewardedVideo();
    }

    @Override
    public void showRewardedVideo(String placementName) {
        mMediationAgent.showRewardedVideo(placementName);
    }

    private void startNetworkMonitor() {
        listener = new NetworkConnectivityListener();
        listener.addListener(new NetworkConnectivityListener.ConnectivityChangeListener() {
            @Override
            public void onConnectivityChanged(NetworkConnectivityListener.State mState) {
                if(mState.equals(NetworkConnectivityListener.State.CONNECTED)){
                    mMediationAgent.initRewardedVideo(activity, mAppKey, mUserId);
                    listener.removeListener();
                    listener.stopListening();
                    listener = null;
                }
            }
        });
        listener.startListening(activity);
    }
}
