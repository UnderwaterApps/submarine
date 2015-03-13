package com.submarine.adcolony.sample;

import com.submarine.adcolony.AdColonyAdListener;
import com.submarine.adcolony.AdColonyListener;
import com.submarine.adcolony.IOSAdColonyNetwork;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.*;

/**
 * Sample usage of the AdColony SDK.
 */
public class Sample extends UIApplicationDelegateAdapter {
    public static final String INTERSTITIAL_ZONE_ID = "vz405b99ff07454edda1";
    public static final String V4VC_ZONE_ID = "vze2d7baba362a4f00bd";
    private static final String APP_ID = "appc50217a2dea14fce89";
    //
    private UIViewController viewController;
    private UIWindow window;
    private IOSAdColonyNetwork adColonyNetwork;

    public static void main(String[] argv) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(argv, null,
                    Sample.class);
        }
    }

    private void initView() {
        CGRect bounds = UIScreen.getMainScreen().getBounds();
        int btnMargin = 60;
        int btnInterstitialWidth = 300;
        int btnInterstitialHeight = 50;
        UIButton btnInterstitial = new UIButton(new CGRect((bounds.getWidth() - btnInterstitialWidth) * .5, (bounds.getHeight() - btnInterstitialHeight) * .5 - btnMargin, btnInterstitialWidth, btnInterstitialHeight));
        btnInterstitial.setBackgroundColor(new UIColor(1, 0, 0, 0.5f));
        btnInterstitial.setTitle("Show \"interstitial\"", UIControlState.Normal);
        btnInterstitial.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside(UIControl control, UIEvent event) {
                //showPromotion("interstitial");
                adColonyNetwork.showVideoAd(INTERSTITIAL_ZONE_ID);
            }
        });
        int btnGridWidth = 300;
        int btnGridHeight = 50;
        UIButton btnGrid = new UIButton(new CGRect((bounds.getWidth() - btnGridWidth) * .5, (bounds.getHeight() - btnGridHeight) * .5 + btnMargin, btnGridWidth, btnGridHeight));
        btnGrid.setBackgroundColor(new UIColor(1, 0, 0, 0.5f));
        btnGrid.setTitle("Show  \"V4VC\"", UIControlState.Normal);
        btnGrid.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
            @Override
            public void onTouchUpInside(UIControl control, UIEvent event) {
                //showPromotion("grid");
                adColonyNetwork.showV4VCAd(V4VC_ZONE_ID, new AdColonyAdListener() {
                    @Override
                    public void onAdStarted(String zoneID) {
                        System.out.println("com.submarine.adcolony.sample.Sample : onAdStarted");
                    }

                    @Override
                    public void onAdAttemptFinished(boolean shown, String zoneID) {
                        System.out.println("com.submarine.adcolony.sample.Sample : onAdAttemptFinished");
                    }
                });
            }
        });

        window.addSubview(btnInterstitial);
        window.addSubview(btnGrid);
    }

    @Override
    public void didFinishLaunching(UIApplication application) {
        viewController = new UIViewController();

        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        window.setRootViewController(viewController);
        window.makeKeyAndVisible();
        adColonyNetwork = new IOSAdColonyNetwork(APP_ID, new String[]{INTERSTITIAL_ZONE_ID, V4VC_ZONE_ID}, new AdColonyListener() {
            @Override
            public void reward(boolean success, String currencyName, int amount) {
                System.out.println("com.submarine.adcolony.sample.Sample : reward");
            }
        });

        initView();
    }
}
