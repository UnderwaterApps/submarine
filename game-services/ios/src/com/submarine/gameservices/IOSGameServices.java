package com.submarine.gameservices;

import com.badlogic.gdx.Gdx;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.gamekit.GKAchievement;
import org.robovm.apple.gamekit.GKLeaderboard;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.bindings.gamecenter.GameCenterListener;
import org.robovm.bindings.gamecenter.GameCenterManager;

import java.util.ArrayList;

/**
 * Created by sargis on 2/26/15.
 */
public class IOSGameServices implements GameServices, GameCenterListener {
    private static final String TAG = "com.submarine.gameservices.IOSGameServices";
    private GameCenterManager gcManager;
    private boolean isSignedIn;

    public IOSGameServices() {
        isSignedIn = false;
    }


    @Override
    public void login() {
        if (isSignedIn) {
            return;
        }
        if (gcManager == null) {
            gcManager = new GameCenterManager(UIApplication.getSharedApplication().getKeyWindow(), this);
        }
        gcManager.login();
    }

    @Override
    public void logout() {

    }

    @Override
    public void submitScore(String leaderBoardId, long score) {
        gcManager.reportScore(leaderBoardId, score);
    }

    @Override
    public void showLeaderBoard(String identifier) {
        gcManager.showLeaderboardView(identifier);
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
        return isSignedIn;
    }

    @Override
    public void playerLoginCompleted() {
        Gdx.app.log(TAG, "Sing in success");
        isSignedIn = true;
    }

    @Override
    public void playerLoginFailed(NSError nsError) {
        Gdx.app.log(TAG, "Sing in Fail");
        isSignedIn = false;
    }

    @Override
    public void achievementReportCompleted() {

    }

    @Override
    public void achievementReportFailed(NSError nsError) {

    }

    @Override
    public void achievementsLoadCompleted(ArrayList<GKAchievement> arrayList) {

    }

    @Override
    public void achievementsLoadFailed(NSError nsError) {

    }

    @Override
    public void achievementsResetCompleted() {

    }

    @Override
    public void achievementsResetFailed(NSError nsError) {

    }

    @Override
    public void scoreReportCompleted() {

    }

    @Override
    public void scoreReportFailed(NSError nsError) {

    }

    @Override
    public void leaderboardsLoadCompleted(ArrayList<GKLeaderboard> arrayList) {

    }

    @Override
    public void leaderboardsLoadFailed(NSError nsError) {

    }

    @Override
    public void leaderboardViewDismissed() {

    }

    @Override
    public void achievementViewDismissed() {

    }
}
