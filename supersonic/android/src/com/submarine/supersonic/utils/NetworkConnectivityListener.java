package com.submarine.supersonic.utils;

/**
 * Created by Gev on 3/9/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * A wrapper for a broadcast receiver that provides network connectivity
 * state information, independent of network type (mobile, Wi-Fi, etc.).
 * {@hide}
 */
public class NetworkConnectivityListener {
    private static final String TAG = "NetworkListener";
    private static final boolean DBG = false;

    private Context mContext;
    private State mState;
    private boolean mListening;
    private String mReason;
    private boolean mIsFailover;

    /** Network connectivity information */
    private NetworkInfo mNetworkInfo;

    /**
     * In case of a Disconnect, the connectivity manager may have
     * already established, or may be attempting to establish, connectivity
     * with another network. If so, {@code mOtherNetworkInfo} will be non-null.
     */
    private NetworkInfo mOtherNetworkInfo;

    private ConnectivityBroadcastReceiver mReceiver;
    private ConnectivityChangeListener listener;

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) ||
                    mListening == false) {
                Log.w(TAG, "onReceived() called with " + mState.toString() + " and " + intent);
                return;
            }

            boolean noConnectivity =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                mState = State.NOT_CONNECTED;
            } else {
                mState = State.CONNECTED;
            }

            mNetworkInfo = (NetworkInfo)
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            mOtherNetworkInfo = (NetworkInfo)
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            mIsFailover =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            if (DBG) {
                Log.d(TAG, "onReceive(): mNetworkInfo=" + mNetworkInfo +  " mOtherNetworkInfo = "
                        + (mOtherNetworkInfo == null ? "[none]" : mOtherNetworkInfo +
                        " noConn=" + noConnectivity) + " mState=" + mState.toString());
            }

            if(listener != null){
                listener.onConnectivityChanged(mState);
            }
        }
    };

    public enum State {
        UNKNOWN,

        /** This state is returned if there is connectivity to any network **/
        CONNECTED,
        /**
         * This state is returned if there is no connectivity to any network. This is set
         * to true under two circumstances:
         * <ul>
         * <li>When connectivity is lost to one network, and there is no other available
         * network to attempt to switch to.</li>
         * <li>When connectivity is lost to one network, and the attempt to switch to
         * another network fails.</li>
         */
        NOT_CONNECTED
    }

    /**
     * Interface for Connectivity Change Listening.
     */
    public interface ConnectivityChangeListener {
        public void onConnectivityChanged(State mState);
    }

    /**
     * Create a new NetworkConnectivityListener.
     */
    public NetworkConnectivityListener() {
        mState = State.UNKNOWN;
        mReceiver = new ConnectivityBroadcastReceiver();
    }

    /**
     * This method starts listening for network connectivity state changes.
     * @param context
     */
    public synchronized void startListening(Context context) {
        if (!mListening) {
            mContext = context;

            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(mReceiver, filter);
            mListening = true;
        }
    }

    /**
     * This method stops this class from listening for network changes.
     */
    public synchronized void stopListening() {
        if (mListening) {
            mContext.unregisterReceiver(mReceiver);
            mContext = null;
            mNetworkInfo = null;
            mOtherNetworkInfo = null;
            mIsFailover = false;
            mReason = null;
            mListening = false;
        }
    }
    public State getState() {
        return mState;
    }

    /**
     * Return the NetworkInfo associated with the most recent connectivity event.
     * @return {@code NetworkInfo} for the network that had the most recent connectivity event.
     */
    public NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }

    /**
     * If the most recent connectivity event was a DISCONNECT, return
     * any information supplied in the broadcast about an alternate
     * network that might be available. If this returns a non-null
     * value, then another broadcast should follow shortly indicating
     * whether connection to the other network succeeded.
     *
     * @return NetworkInfo
     */
    public NetworkInfo getOtherNetworkInfo() {
        return mOtherNetworkInfo;
    }

    /**
     * Returns true if the most recent event was for an attempt to switch over to
     * a new network following loss of connectivity on another network.
     * @return {@code true} if this was a failover attempt, {@code false} otherwise.
     */
    public boolean isFailover() {
        return mIsFailover;
    }

    /**
     * An optional reason for the connectivity state change may have been supplied.
     * This returns it.
     * @return the reason for the state change, if available, or {@code null}
     * otherwise.
     */
    public String getReason() {
        return mReason;
    }

    /**
     * This methods adds a listener to be called back onto with the specified what code when
     * the network connectivity state changes.
     *
     * @param listener The target handler.
     */
    public void addListener(ConnectivityChangeListener listener) {
        this.listener = listener;
    }

    /**
     * This methods removes the listener.
     */
    public void removeListener() {
        this.listener = null;
    }
}