package com.submarine.chartboost;

import org.robovm.pods.chartboost.*;

import java.util.ArrayList;

/**
 * Created by mariam on 1/11/16.
 */
public class IOSChartBoost implements ChartBoostListener {

    private ChartboostDelegate delegate;

    @Override
    public void onCreate(String appId, String appSignature, ArrayList<String> locations) {

        initDelegate();

        Chartboost.start(appId, appSignature, delegate);
        Chartboost.setShouldRequestInterstitialsInFirstSession(false);

        for (String location : locations) {
            Chartboost.cacheInterstitial(location);
        }
        Chartboost.cacheMoreApps(CBLocation.Default);
    }

    @Override
    public boolean hasInterstitial(String locationName) {
        return Chartboost.hasInterstitial(locationName);
    }

    @Override
    public void showInterstisial(String locationName) {
        Chartboost.showInterstitial(locationName);
    }

    @Override
    public void cacheInterstisial(String locationName) {
        Chartboost.cacheInterstitial(locationName);
    }

    @Override
    public void showMoreApps() {
        Chartboost.showMoreApps(CBLocation.Default);
    }

    @Override
    public void cacheMoreApps() {
        Chartboost.cacheMoreApps(CBLocation.Default);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    private void initDelegate() {
        delegate = new ChartboostDelegate() {
            @Override
            public boolean shouldRequestInterstitial(String location) {
                return false;
            }

            @Override
            public boolean shouldDisplayInterstitial(String location) {
                return false;
            }

            @Override
            public void didDisplayInterstitial(String location) {

            }

            @Override
            public void didCacheInterstitial(String location) {

            }

            @Override
            public void didFailToLoadInterstitial(String location, CBLoadError error) {

            }

            @Override
            public void didFailToRecordClick(String location, CBClickError error) {

            }

            @Override
            public void didDismissInterstitial(String location) {

            }

            @Override
            public void didCloseInterstitial(String location) {

            }

            @Override
            public void didClickInterstitial(String location) {

            }

            @Override
            public boolean shouldDisplayMoreApps(String location) {
                return false;
            }

            @Override
            public void didDisplayMoreApps(String location) {

            }

            @Override
            public void didCacheMoreApps(String location) {

            }

            @Override
            public void didDismissMoreApps(String location) {

            }

            @Override
            public void didCloseMoreApps(String location) {

            }

            @Override
            public void didClickMoreApps(String location) {

            }

            @Override
            public void didFailToLoadMoreApps(String location, CBLoadError error) {

            }

            @Override
            public void didPrefetchVideos() {

            }

            @Override
            public boolean shouldDisplayRewardedVideo(String location) {
                return false;
            }

            @Override
            public void didDisplayRewardedVideo(String location) {

            }

            @Override
            public void didCacheRewardedVideo(String location) {

            }

            @Override
            public void didFailToLoadRewardedVideo(String location, CBLoadError error) {

            }

            @Override
            public void didDismissRewardedVideo(String location) {

            }

            @Override
            public void didCloseRewardedVideo(String location) {

            }

            @Override
            public void didClickRewardedVideo(String location) {

            }

            @Override
            public void didCompleteRewardedVideo(String location, int reward) {

            }

            @Override
            public void didCacheInPlay(String location) {

            }

            @Override
            public void didFailToLoadInPlay(String location, CBLoadError error) {

            }

            @Override
            public void willDisplayVideo(String location) {

            }

            @Override
            public void didCompleteAppStoreSheetFlow() {

            }

            @Override
            public void didPauseClickForConfirmation() {

            }

            @Override
            public void didFailToLoadMoreApps(CBLoadError error) {

            }
        };
    }
}
