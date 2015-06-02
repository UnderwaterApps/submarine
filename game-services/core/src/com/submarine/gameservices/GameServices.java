package com.submarine.gameservices;

public interface GameServices {
    void login();

    void logout();

    void submitEvent(String eventId, int count);

    void showEvents();

    void submitScore(String leaderBoardId, long score);

    void showLeaderBoard(String identifier);

    void showLeaderBoards();

    void unlockAchievement(String achievementId);

    void incrementAchievement(String achievementId, int incrementAmount);

    void showAchievements();

    boolean isSignedIn();

    void savedGamesLoad(String snapshotName, boolean createIfMissing);

    void savedGamesUpdate(String snapshotName, byte[] data, boolean createIfMissing);

    void setListener(GameServicesListener gameServicesListener);

    boolean isSavedGamesLoadDone();

    void loadUserInfo();
}
