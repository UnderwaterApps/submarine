package com.submarine.gameservices;

public interface GameServices {
    void submitScore(long score);

    void showLeaderBoard(String string);

    boolean isSignedIn();
}
