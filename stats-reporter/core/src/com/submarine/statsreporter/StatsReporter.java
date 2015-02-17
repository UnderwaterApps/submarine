package com.submarine.statsreporter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.submarine.statsreporter.vo.StatsRequestVO;
import com.submarine.statsreporter.vo.StatsResponseVO;

import java.util.HashMap;
import java.util.Map;

public abstract class StatsReporter<U extends StatsRequestVO, V extends StatsResponseVO> implements AsyncTask<Void> {
    private static final String URL = "http://kiki-fish.com/ios/event.php";
    private static final String TAG = "com.submarine.statsreporter.StatsReporter";
    protected final Class<U> requestType;
    protected final Class<V> responseType;
    private final int appId;
    public V responseVO;
    protected U statsReporterVO;
    private StatsReporterResponseListener<V> statsReporterResponseListener;

    public StatsReporter(int appId, Class<U> requestType, Class<V> responseType) throws IllegalAccessException, InstantiationException {
        this.appId = appId;
        this.requestType = requestType;
        this.responseType = responseType;
        responseVO = responseType.newInstance();
    }

    public void setStatsReporterResponseListener(StatsReporterResponseListener<V> statsReporterResponseListener) {
        this.statsReporterResponseListener = statsReporterResponseListener;
    }

    @Override
    public Void call() throws Exception {
        statsReporterVO = crateStatsRequestVO();
        try {
            Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
            httpRequest.setUrl(URL);
            httpRequest.setContent(HttpParametersUtils.convertHttpParameters(getParameters()));
            Gdx.net.sendHttpRequest(httpRequest, new StatsReporterHttpResponseListener());
            Gdx.app.debug(TAG, "httpRequest : " + httpRequest.getUrl());
        } catch (Exception e) {
            // no connection no cry
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    protected Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("app_id", String.valueOf(appId));
        parameters.put("udid", statsReporterVO.udid);
        parameters.put("model", statsReporterVO.model);
        parameters.put("version", statsReporterVO.version);
        parameters.put("rnd", String.valueOf(Math.random()));
        return parameters;
    }

    protected abstract U crateStatsRequestVO() throws IllegalAccessException, InstantiationException;

    public void report() {
        try {
            AsyncExecutor asyncExecutor = new AsyncExecutor(1);
            asyncExecutor.submit(this);
            asyncExecutor.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handelResponse(String resultAsString) {
        Json json = new Json();
        responseVO = json.fromJson(responseType, resultAsString);
    }


    public static interface StatsReporterResponseListener<T extends StatsResponseVO> {
        public void succeed(T statsResponseVO);

        public void failed(Throwable t);

        public void cancelled();
    }

    private class StatsReporterHttpResponseListener implements Net.HttpResponseListener {
        private static final String TAG = "com.submarine.statsreporter.StatsReporter.StatsReporterHttpResponseListener";

        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse) {
            try {
                handelResponse(httpResponse.getResultAsString());
                if (statsReporterResponseListener != null) {
                    statsReporterResponseListener.succeed(responseVO);
                }
            } catch (Error error) {
                Gdx.app.error(TAG, error.getMessage());
                if (statsReporterResponseListener != null) {
                    statsReporterResponseListener.failed(error);
                }
            }

        }

        @Override
        public void failed(Throwable t) {
            Gdx.app.error(TAG, t.getMessage());
            if (statsReporterResponseListener != null) {
                statsReporterResponseListener.failed(t);
            }
        }

        @Override
        public void cancelled() {
            Gdx.app.log(TAG, "cancelled");
            if (statsReporterResponseListener != null) {
                statsReporterResponseListener.cancelled();
            }
        }
    }
}
