package com.submarine.gameservices;

public interface GameServices {
    void submitScore(long score, final String leaderBoardId);

    void showLeaderBoard(String string);

    boolean isSignedIn();
}
