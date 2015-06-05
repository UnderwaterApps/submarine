package com.submarine.gameservices;

import com.submarine.gameservices.events.LoadedEventListener;
import com.submarine.gameservices.quests.LoadedQuestListener;
import com.submarine.gameservices.quests.QuestRewardListener;

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

    void submitEvent(String eventId, int incrementAmount);

    void loadEvents(LoadedEventListener listener);

    void loadEventsByIds(LoadedEventListener listener, String... eventIds);

    void showQuests();

    void loadQuests(LoadedQuestListener listener);

    void loadQuestsByIds(LoadedQuestListener listener, String... questIds);

    void registerQuestUpdate(QuestRewardListener listener);

    boolean isSignedIn();

    void savedGamesLoad(String snapshotName, boolean createIfMissing);

    void savedGamesUpdate(String snapshotName, byte[] data, boolean createIfMissing);

    void setListener(GameServicesListener gameServicesListener);

    boolean isSavedGamesLoadDone();

    void loadUserInfo();
}
