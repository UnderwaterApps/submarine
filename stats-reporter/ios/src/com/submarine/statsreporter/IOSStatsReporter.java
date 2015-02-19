package com.submarine.statsreporter;

import com.submarine.statsreporter.vo.StatsRequestVO;
import com.submarine.statsreporter.vo.StatsResponseVO;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.uikit.UIDevice;

/**
 * Created by sargis on 2/6/15.
 */
public class IOSStatsReporter<U extends StatsRequestVO, V extends StatsResponseVO> extends StatsReporter {
    @SuppressWarnings("unchecked")
    public IOSStatsReporter(int appId) throws InstantiationException, IllegalAccessException {
        super(appId, StatsRequestVO.class, StatsResponseVO.class);

    }

    @SuppressWarnings("unchecked")
    public IOSStatsReporter(int appId, Class<U> requestType, Class<V> responseType) throws IllegalAccessException, InstantiationException {
        super(appId, requestType, responseType);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StatsRequestVO crateStatsRequestVO() throws IllegalAccessException, InstantiationException {
        U statsReporterVO = (U) requestType.newInstance();
        statsReporterVO.udid = UIDevice.getCurrentDevice().getIdentifierForVendor().asString();
        statsReporterVO.model = UIDevice.getCurrentDevice().getModel();
        statsReporterVO.version = String.valueOf(NSBundle.getMainBundle().getInfoDictionary().asStringMap().get("CFBundleVersion"));
        System.out.println(statsReporterVO);
        return statsReporterVO;
    }
}
