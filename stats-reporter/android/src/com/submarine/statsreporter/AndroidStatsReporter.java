package com.submarine.statsreporter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import com.submarine.statsreporter.vo.StatsRequestVO;
import com.submarine.statsreporter.vo.StatsResponseVO;

/**
 * Created by sargis on 2/6/15.
 */
public class AndroidStatsReporter<U extends StatsRequestVO, V extends StatsResponseVO> extends StatsReporter {
    private final Context context;

    @SuppressWarnings("unchecked")
    public AndroidStatsReporter(Context context, int appId, Class<U> requestType, Class<V> responseType) throws InstantiationException, IllegalAccessException {
        super(appId, requestType, responseType);
        this.context = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StatsRequestVO crateStatsRequestVO() throws IllegalAccessException, InstantiationException {
        U statsReporterVO = (U) requestType.newInstance();
        int versionNumber = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionNumber = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        statsReporterVO.udid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        statsReporterVO.model = Build.MODEL;
        statsReporterVO.version = Integer.toString(versionNumber);
        return statsReporterVO;
    }
}
