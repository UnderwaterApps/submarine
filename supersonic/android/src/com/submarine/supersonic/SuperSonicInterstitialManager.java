package com.submarine.supersonic;

import android.app.Activity;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.Supersonic;

/**
 * Created by Gev on 3/4/2016.
 */
public class SuperSonicInterstitialManager implements SuperSonicInterstitial{
    public static final String TAG = SuperSonicInterstitialManager.class.getSimpleName();

    private Supersonic mMediationAgent;
    private Activity activity;

    private SuperSonicInterstitialAdListener adListener;

    public SuperSonicInterstitialManager(Activity activity, Supersonic mMediationAgent, String mUserId, String mAppKey) {


        this.activity = activity;
        this.mMediationAgent = mMediationAgent;
        this.mMediationAgent.setInterstitialListener(mInterstitialListener);
        this.mMediationAgent.initInterstitial(this.activity, mAppKey, mUserId);

    }

    InterstitialListener mInterstitialListener = new InterstitialListener()
    {

        @Override
        public void onInterstitialInitSuccess() {
            if(adListener != null) adListener.onInterstitialInitSuccess();
        }

        @Override
        public void onInterstitialInitFailed(SupersonicError supersonicError) {
            if(adListener != null) adListener.onInterstitialInitFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialReady() {
            if(adListener != null) adListener.onInterstitialReady();
        }

        @Override
        public void onInterstitialLoadFailed(SupersonicError supersonicError) {
            if(adListener != null) adListener.onInterstitialLoadFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialOpen() {
            if(adListener != null) adListener.onInterstitialOpen();
        }

        @Override
        public void onInterstitialClose() {
            if(adListener != null) adListener.onInterstitialClose();
        }

        @Override
        public void onInterstitialShowSuccess() {
            if(adListener != null) adListener.onInterstitialShowSuccess();
        }

        @Override
        public void onInterstitialShowFailed(SupersonicError supersonicError) {
            if(adListener != null) adListener.onInterstitialShowFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialClick() {
            if(adListener != null) adListener.onInterstitialClick();
        }
    };

    @Override
    public void setAdlistener(SuperSonicInterstitialAdListener listener) {
        this.adListener = listener;
    }

    @Override
    public void loadInterstitial() {
        mMediationAgent.loadInterstitial();
    }

    //@Override
    public void showInterstitial() {
        mMediationAgent.showInterstitial();
    }

    @Override
    public void showInterstitial(String placementName) {
        mMediationAgent.showInterstitial(placementName);
    }
}
