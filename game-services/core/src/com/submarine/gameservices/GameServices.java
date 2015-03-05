package com.submarine.gameservices;

public interface GameServices {
    void login();

    void logout();

    void submitScore(String leaderBoardId, long score);

    void showLeaderBoard(String identifier);

    void unlockStandardAchievement(String achievementId);

    void showAchievements();

    boolean isSignedIn();
}
