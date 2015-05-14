package com.submarine.gameservices;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by sargis on 2/25/15.
 */
public class DesktopGameServices implements GameServices {
    private boolean isSavedGamesLoadDone;
    private GameServicesListener<Object> gameServicesListener;

    public DesktopGameServices() {
        isSavedGamesLoadDone = false;
    }

    @Override
    public void login() {
        gameServicesListener.onSignInSucceeded();
    }

    @Override
    public void logout() {

    }

    @Override
    public void submitScore(String leaderBoardId, long score) {

    }

    @Override
    public void showLeaderBoard(String string) {

    }

    @Override
    public void showLeaderBoards() {

    }

    @Override
    public void unlockAchievement(String achievementId) {

    }

    @Override
    public void incrementAchievement(String achievementId, int incrementAmount) {

    }

    @Override
    public void showAchievements() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void savedGamesLoad(String snapshotName, boolean createIfMissing) {
        int status = MathUtils.random(0, 3);
        switch (status) {
            case 0:
            case 1:
                gameServicesListener.savedGamesLoadSucceeded(new Object());
                break;
            case 2:
                gameServicesListener.savedGamesLoadConflicted(new Object(), 0);
                break;
            default:
                gameServicesListener.savedGamesLoadFailed(new Object());
                break;
        }
        isSavedGamesLoadDone = true;
    }

    @Override
    public void savedGamesUpdate(String snapshotName, byte[] data, boolean createIfMissing) {

    }

    @Override
    public void setListener(GameServicesListener gameServicesListener) {
        this.gameServicesListener = gameServicesListener;
    }

    @Override
    public boolean isSavedGamesLoadDone() {
        return isSavedGamesLoadDone;
    }

    @Override
    public void loadUserInfo() {

    }
}
