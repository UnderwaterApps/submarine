package com.submarine.statsreporter;

import com.submarine.statsreporter.vo.StatsRequestVO;
import com.submarine.statsreporter.vo.StatsResponseVO;

/**
 * Created by sargis on 2/6/15.
 */
public class DesktopStatsReporter<U extends StatsRequestVO, V extends StatsResponseVO> extends StatsReporter {
    @SuppressWarnings("unchecked")
    public DesktopStatsReporter(int appId) throws IllegalAccessException, InstantiationException {
        super(appId, StatsRequestVO.class, StatsResponseVO.class);
    }

    @SuppressWarnings("unchecked")
    public DesktopStatsReporter(int appId, Class<U> requestType, Class<V> responseType) throws IllegalAccessException, InstantiationException {
        super(appId, requestType, responseType);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StatsRequestVO crateStatsRequestVO() throws IllegalAccessException, InstantiationException {
        return (U) requestType.newInstance();
    }
}
