package com.submarine.supersonic;

/**
 * Created by sargis on 1/30/15.
 */
public class DesktopSuperSonicNetwork implements SuperSonicNetwork{

 private SuperSonicInterstitial interstitialManager;
 private SuperSonicRewardedVideo rewardedVideoManager;

    public DesktopSuperSonicNetwork() {
        initInterstitialManager();
        initRewardedVideoManager();
    }

    @Override
    public void initInterstitialManager() {
     interstitialManager = new SuperSonicInterstitial(){

        @Override
        public void setAdlistener(SuperSonicInterstitialAdListener listener) {

        }

        @Override
        public void loadInterstitial() {

        }

        @Override
        public void showInterstitial() {

        }

        @Override
        public void showInterstitial(String placementName) {

        }
       };
    }

    @Override
    public void initRewardedVideoManager() {
     rewardedVideoManager = new SuperSonicRewardedVideo() {
      @Override
      public boolean isRewardedVideoAvailable() {
       return false;
      }

      @Override
      public void showRewardedVideo() {

      }

      @Override
      public void showRewardedVideo(String placementName) {

      }

         @Override
         public void setRewardedVideoListener(SuperSonicRewardedVideoListener listener) {

         }
     };
    }

    @Override
    public SuperSonicInterstitial getInterstitialManager() {
        return interstitialManager;
    }

    @Override
    public SuperSonicRewardedVideo getRewardedVideoManager() {
        return rewardedVideoManager;
    }
}
