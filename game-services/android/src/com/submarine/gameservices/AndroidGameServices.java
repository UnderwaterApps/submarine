package com.submarine.gameservices;

import android.app.Activity;
import android.content.Intent;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.appstate.AppStateManager;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidGameServices implements GameHelper.GameHelperListener, GameServices {
    // The AppState slot we are editing.  For simplicity this sample only manipulates a single
    // Cloud Save slot and a corresponding Snapshot entry,  This could be changed to any integer
    // 0-3 without changing functionality (Cloud Save has four slots, numbered 0-3).
    private static final int APP_STATE_KEY = 0;
    private static final String TAG = "com.submarine.gameservices.AndroidGameServices";
    private Activity activity;
    private GameHelper mHelper;


    public AndroidGameServices(Activity activity) {
        this.activity = activity;
        mHelper = new GameHelper(this.activity, (GameHelper.CLIENT_GAMES | GameHelper.CLIENT_SNAPSHOT));
        mHelper.setup(this);
        mHelper.enableDebugLog(true);

    }

    public void onActivityResult(int request, int response, Intent data) {
        mHelper.onActivityResult(request, response, data);
    }


    // // ************** GOOGLE PART ***************\\\\\\\

    @Override
    public void onSignInFailed() {
        Gdx.app.log(TAG, "Sing in Fail");
        mHelper.showFailureDialog();
    }

    @Override
    public void onSignInSucceeded() {
        Gdx.app.log(TAG, "Sign in success");
    }

    // // ************** END GOOGLE PART ***************\\\\\\\

    @Override
    public boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    @Override
    public void login() {
        if (mHelper.isConnecting()) {
            return;
        }
        mHelper.onStart(activity);
    }

    @Override
    public void logout() {
        mHelper.onStop();
    }

    @Override
    public void submitScore(final String leaderBoardId, final long score) {
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Games.Leaderboards.submitScore(mHelper.getApiClient(), leaderBoardId, score);
                }
            });
        }
    }

    @Override
    public void showLeaderBoard(final String leaderBoardId) {
        Gdx.app.log(TAG, "Show Leaderboard : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Gdx.app.log(TAG, "Show Leaderboard");
                    activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), leaderBoardId), 2);
                }
            });
        }
    }


    /**
     * Async load AppState from Cloud Save.  This will load using stateKey APP_STATE_KEY.  After load,
     * the AppState data and metadata will be displayed.
     */
    private void cloudSaveLoad() {
        PendingResult<AppStateManager.StateResult> pendingResult = AppStateManager.load(mHelper.getApiClient(), APP_STATE_KEY);
        ResultCallback<AppStateManager.StateResult> callback =
                new ResultCallback<AppStateManager.StateResult>() {
                    @Override
                    public void onResult(AppStateManager.StateResult stateResult) {
                        if (stateResult.getStatus().isSuccess()) {
                            // Successfully loaded data from App State
                            byte[] data = stateResult.getLoadedResult().getLocalData();
                        } else {
                            // Failed to load data from App State
                        }

                    }
                };
        pendingResult.setResultCallback(callback);
    }

    /**
     * Async update AppState data in Cloud Save.  This will use stateKey APP_STATE_KEY. After save,
     * the UI will be cleared and the data will be available to load from Cloud Save.
     */
    private void cloudSaveUpdate(byte[] data) {
        // Use updateImmediate to update the AppState data.  This is used for diagnostic purposes
        // so that the app can display the result of the update, however it is generally recommended
        // to use AppStateManager.update(...) in order to reduce performance and battery impact.
        PendingResult<AppStateManager.StateResult> pendingResult = AppStateManager.updateImmediate(
                mHelper.getApiClient(), APP_STATE_KEY, data);

        ResultCallback<AppStateManager.StateResult> callback =
                new ResultCallback<AppStateManager.StateResult>() {
                    @Override
                    public void onResult(AppStateManager.StateResult stateResult) {
                        if (stateResult.getStatus().isSuccess()) {
                        } else {
                        }
                    }
                };
        pendingResult.setResultCallback(callback);
    }
}
