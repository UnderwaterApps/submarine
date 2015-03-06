package com.submarine.gameservices;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.google.example.games.basegameutils.GameHelper;

import java.io.IOException;

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
     * Load a Snapshot from the Saved Games service based on its unique name.  After load, the UI
     * will update to display the Snapshot data and SnapshotMetadata.
     *
     * @param snapshotName the unique name of the Snapshot.
     */
    @Override
    public void savedGamesLoad(String snapshotName, boolean createIfMissing) {
        PendingResult<Snapshots.OpenSnapshotResult> pendingResult = Games.Snapshots.open(
                mHelper.getApiClient(), snapshotName, createIfMissing);

        ResultCallback<Snapshots.OpenSnapshotResult> callback =
                new ResultCallback<Snapshots.OpenSnapshotResult>() {
                    @Override
                    public void onResult(Snapshots.OpenSnapshotResult openSnapshotResult) {
                        if (openSnapshotResult.getStatus().isSuccess()) {
                            byte[] data = new byte[0];
                            try {
                                data = openSnapshotResult.getSnapshot().getSnapshotContents().readFully();
                            } catch (IOException e) {
                            }
                        } else {
                        }
                    }
                };
        pendingResult.setResultCallback(callback);
    }


    /**
     * Update the Snapshot in the Saved Games service with new data.  Metadata is not affected,
     * however for your own application you will likely want to update metadata such as cover image,
     * played time, and description with each Snapshot update.  After update, the UI will
     * be cleared.
     */
    @Override
    public void savedGamesUpdate(final String snapshotName, final byte[] data, final boolean createIfMissing) {
        AsyncTask<Void, Void, Boolean> updateTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult open = Games.Snapshots.open(
                        mHelper.getApiClient(), snapshotName, createIfMissing).await();

                if (!open.getStatus().isSuccess()) {
                    Log.w(TAG, "Could not open Snapshot for update.");
                    return false;
                }

                // Change data but leave existing metadata
                Snapshot snapshot = open.getSnapshot();
                snapshot.getSnapshotContents().writeBytes(data);

                Snapshots.CommitSnapshotResult commit = Games.Snapshots.commitAndClose(
                        mHelper.getApiClient(), snapshot, SnapshotMetadataChange.EMPTY_CHANGE).await();

                if (!commit.getStatus().isSuccess()) {
                    Log.w(TAG, "Failed to commit Snapshot.");
                    return false;
                }
                // No failures
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                } else {
                }
            }
        };
        updateTask.execute();
    }
}
