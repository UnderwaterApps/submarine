package com.submarine.gameservices;

/**
 * Created by sargis on 2/25/15.
 */
public class DesktopGameServices implements GameServices {
    @Override
    public void login() {
        
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
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void savedGamesLoad(String snapshotName, boolean createIfMissing) {

    }

    @Override
    public void savedGamesUpdate(String snapshotName, byte[] data, boolean createIfMissing) {

    }
}
