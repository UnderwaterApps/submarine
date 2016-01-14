package com.submarine.chartboost;

import java.util.ArrayList;

/**
 * Created by mariam on 1/8/16.
 */
public interface ChartBoostListener {

    public void onCreate(String appId, String appSignature, ArrayList<String> locations);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public boolean onBackPressed();

    public void showInterstisial(String locationName);

    public void cacheInterstisial(String locationName);

    public void showMoreApps();

    public void cacheMoreApps();
}
