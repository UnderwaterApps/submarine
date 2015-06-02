package com.submarine.adMob;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.*;

/**
 * Created by sargis on 1/30/15.
 */
public class AndroidAdMobNetwork implements AdNetwork {
    private final AndroidApplication androidApplication;
    private final String adViewUnitId;
    private final String interstitialAdUnitId;
    private final String testDevice;
    private AdView adView;
    private InterstitialAd interstitial;

    public AndroidAdMobNetwork(AndroidApplication androidApplication, String adViewUnitId, String interstitialAdUnitId) {
        this(androidApplication, adViewUnitId, interstitialAdUnitId, null);
    }

    public AndroidAdMobNetwork(AndroidApplication androidApplication, String adViewUnitId, String interstitialAdUnitId, String testDevice) {
        this.androidApplication = androidApplication;
        this.adViewUnitId = adViewUnitId;
        this.interstitialAdUnitId = interstitialAdUnitId;
        this.testDevice = testDevice;
        initInterstitial();
        initBanner();
    }

    private void initInterstitial() {
        interstitial = new InterstitialAd(androidApplication);
        interstitial.setAdUnitId(interstitialAdUnitId);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });
    }

    private void initBanner() {
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
                // Create ad request.
                AdRequest adRequest = getInterstitialAdRequest();
                // Begin loading your interstitial.
                interstitial.loadAd(adRequest);
            }
        });
    }

    @Override
    public void takeToApp(String packageName) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        androidApplication.startActivity(browserIntent);
    }

    private AdRequest getInterstitialAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        addTestDevice(builder);
        return builder.build();
    }

    public AdView getAdView() {
        return adView;
    }
}
