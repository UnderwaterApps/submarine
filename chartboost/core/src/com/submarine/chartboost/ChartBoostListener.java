package com.submarine.chartboost;

/**
 * Created by mariam on 1/8/16.
 */
public interface ChartBoostListener {

    public void onCreate(String appId, String appSignature);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public boolean onBackPressed();

    public void showInterstisial(String locationName);

    public void cacheInterstisial(String locationName);
}
