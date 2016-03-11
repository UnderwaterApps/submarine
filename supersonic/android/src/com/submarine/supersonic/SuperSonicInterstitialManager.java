package com.submarine.supersonic;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.submarine.supersonic.utils.NetworkConnectivityListener;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.Supersonic;

/**
 * Created by Gev on 3/4/2016.
 */
public class SuperSonicInterstitialManager implements SuperSonicInterstitial{
    public static final String TAG = SuperSonicInterstitialManager.class.getSimpleName();
    private final String mAppKey;
    private final String mUserId;

    private Supersonic mMediationAgent;
    private Activity activity;

    private SuperSonicInterstitialAdListener adListener;
    private NetworkConnectivityListener listener;

    private boolean initialized = false;

    public SuperSonicInterstitialManager(Activity activity, Supersonic mMediationAgent, String mUserId, String mAppKey) {
        this.mAppKey = mAppKey;
        this.mUserId = mUserId;
        this.activity = activity;
        this.mMediationAgent = mMediationAgent;
        this.mMediationAgent.setInterstitialListener(mInterstitialListener);
        this.mMediationAgent.initInterstitial(this.activity, mAppKey, mUserId);
    }

    InterstitialListener mInterstitialListener = new InterstitialListener()
    {

        @Override
        public void onInterstitialInitSuccess() {
            mMediationAgent.loadInterstitial();
            initialized = true;
            if(adListener == null) return;
            adListener.onInterstitialInitSuccess();
        }

        @Override
        public void onInterstitialInitFailed(SupersonicError supersonicError) {
            initialized = false;
            startNetworkMonitor();
            if(adListener == null) return;
            adListener.onInterstitialInitFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialReady() {
            if(adListener == null) return;
            adListener.onInterstitialReady();
        }

        @Override
        public void onInterstitialLoadFailed(SupersonicError supersonicError) {
            if(!isConnectedToInternet()){
                startNetworkMonitor();
            }
            if(adListener == null) return;
            adListener.onInterstitialLoadFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialOpen() {
            if(adListener == null) return;
            adListener.onInterstitialOpen();
        }

        @Override
        public void onInterstitialClose() {
            if(adListener == null) return;
            adListener.onInterstitialClose();
        }

        @Override
        public void onInterstitialShowSuccess() {
            if(adListener == null) return;
            adListener.onInterstitialShowSuccess();
        }

        @Override
        public void onInterstitialShowFailed(SupersonicError supersonicError) {
            if(!isConnectedToInternet()){
                startNetworkMonitor();
            }
            if(adListener == null) return;
            adListener.onInterstitialShowFailed(supersonicError.getErrorMessage());
        }

        @Override
        public void onInterstitialClick() {
            if(adListener == null) return;
            adListener.onInterstitialClick();
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

    private void startNetworkMonitor() {
        if(listener != null){
            return;
        }
        listener = new NetworkConnectivityListener();
        listener.addListener(new NetworkConnectivityListener.ConnectivityChangeListener() {
            @Override
            public void onConnectivityChanged(NetworkConnectivityListener.State mState) {
                if(mState.equals(NetworkConnectivityListener.State.CONNECTED)){
                    if(initialized){
                        mMediationAgent.loadInterstitial();
                    }else {
                        mMediationAgent.initInterstitial(activity, mAppKey, mUserId);
                    }
                    listener.removeListener();
                    listener.stopListening();
                    listener = null;
                }
            }
        });
        listener.startListening(activity);
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
