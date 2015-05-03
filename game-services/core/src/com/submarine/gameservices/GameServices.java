package com.submarine.gameservices;

import com.submarine.gameservices.events.LoadedEventListener;

public interface GameServices {
    void login();

    void logout();

    void submitScore(String leaderBoardId, long score);

    void showLeaderBoard(String identifier);

    void showLeaderBoards();

    void unlockAchievement(String achievementId);

    void incrementAchievement(String achievementId, int incrementAmount);

    void showAchievements();

    void submitEvent(String eventId, int incrementAmount);

    void loadEvents(LoadedEventListener listener);

    void loadEventsByIds(LoadedEventListener listener, String... eventIds);

    boolean isSignedIn();

    void savedGamesLoad(String snapshotName, boolean createIfMissing);

    void savedGamesUpdate(String snapshotName, byte[] data, boolean createIfMissing);

    void setListener(GameServicesListener gameServicesListener);

    boolean isSavedGamesLoadDone();
}
