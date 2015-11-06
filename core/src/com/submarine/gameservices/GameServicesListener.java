package com.submarine.gameservices;

/**
 * Created by sargis on 3/6/15.
 */
public interface GameServicesListener<T> {
    void onSignInFailed();

    void onSignInSucceeded();

    void savedGamesLoadFailed(T result);

    void savedGamesLoadSucceeded(T result);

    void savedGamesLoadConflicted(T result, int retryCount);

    void savedGamesUpdateSucceeded();

    void savedGamesUpdateFailed();

    void savedGamesLoadDone();
}
