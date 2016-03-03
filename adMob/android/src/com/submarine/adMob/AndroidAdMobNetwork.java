package com.submarine.admob;

import android.provider.Settings;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sargis on 1/30/15.
 */
public class AndroidAdMobNetwork implements AdNetwork {
    private final AndroidApplication androidApplication;
    private String adViewUnitId;
    private String interstitialAdUnitId;
    private String testDevice;
    private AdView adView;
    private InterstitialAd interstitial;
    private InterstitialAdListener interstitialAdListener;

    public AndroidAdMobNetwork(AndroidApplication androidApplication) {
        this(androidApplication, null, null, true);
    }

    public AndroidAdMobNetwork(AndroidApplication androidApplication, String adViewUnitId, String interstitialAdUnitId, boolean test) {
        this.androidApplication = androidApplication;
        this.adViewUnitId = adViewUnitId;
        this.interstitialAdUnitId = interstitialAdUnitId;
        //TODO this is temporary and will be changed ask Gevorg if you have ideas I have them too :D
        if(test){
            enableTestingMode();
        }
        initInterstitial();
        initBanner();
    }

    private void initInterstitial() {
        if(interstitialAdUnitId == null || interstitialAdUnitId.isEmpty()){
            return;
        }
        interstitial = new InterstitialAd(androidApplication);
        interstitial.setAdUnitId(interstitialAdUnitId);
        interstitial.setAdListener(adListener);
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            if(interstitialAdListener !=null) interstitialAdListener.onAdClosed();
            requestNewInterstitial();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            if(interstitialAdListener !=null) interstitialAdListener.onAdFailedToLoad(errorCode);
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            if(interstitialAdListener !=null) interstitialAdListener.onAdLeftApplication();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            if(interstitialAdListener !=null) interstitialAdListener.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            if(interstitialAdListener !=null) interstitialAdListener.onAdLoaded();
        }
    };

    private void initBanner() {
        if(adViewUnitId == null || adViewUnitId.isEmpty()){
            return;
        }
        androidApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView = new AdView(androidApplication);
                adView.setAdSize(AdSize.SMART_BANNER);
                adView.setAdUnitId(adViewUnitId);
                AdRequest adRequest = getBannerAdRequest();
                adView.loadAd(adRequest);
                hideBanner();
            }
        });
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = getInterstitialAdRequest();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
    }

    private AdRequest getInterstitialAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        addTestDevice(builder);
        return builder.build();
    }

    private AdRequest getBannerAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        addTestDevice(builder);
        return builder.build();
    }



    private void addTestDevice(AdRequest.Builder builder) {
        if (testDevice != null && !testDevice.isEmpty()) {
            builder.addTestDevice(testDevice);
        }
    }

    @Override
    public void showBanner() {
        androidApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideBanner() {
        androidApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void showInterstitial() {
        androidApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitial.isLoaded()) interstitial.show();
            }
        });
    }

    public AdView getAdView() {
        return adView;
    }

    public void setInterstitialAdUnitId(String interstitialAdUnitId){
        this.interstitialAdUnitId = interstitialAdUnitId;
        initInterstitial();
    }

    public void setAdViewUnitId(String adViewUnitId){
        this.adViewUnitId = adViewUnitId;
        initBanner();
    }

    public void setAdViewSize(AdSize adSize){
        getAdView().setAdSize(adSize);
    }

    public void enableTestingMode(){
        testDevice = getDeviceID();
    }

    public void setInterstitialAdListener(InterstitialAdListener interstitialAdListener){
        this.interstitialAdListener = interstitialAdListener;
    }

    private String getDeviceID() {
        String deviceId = "";
        String android_id = Settings.Secure.getString(androidApplication.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5(android_id).toUpperCase();

        //Log.v(TAG, "Admob Test Device ? " + deviceId + " " + isTestDevice);
        return deviceId;
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            //Logger.logStackTrace(TAG,e);
        }
        return "";
    }
}
