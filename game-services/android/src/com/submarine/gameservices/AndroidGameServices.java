package com.submarine.gameservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.api.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.event.Events;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.QuestBuffer;
import com.google.android.gms.games.quest.QuestUpdateListener;
import com.google.android.gms.games.quest.Quests;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.example.games.basegameutils.GameHelper;
import com.submarine.gameservices.events.LoadedEventListener;
import com.submarine.gameservices.quests.LoadedQuestListener;
import com.submarine.gameservices.quests.QuestConstants;
import com.submarine.gameservices.quests.QuestRewardListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class AndroidGameServices implements GameHelper.GameHelperListener, GameServices {
    // Client request flags
    public final static int CLIENT_NONE = GameHelper.CLIENT_NONE;
    public final static int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public final static int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    //public final static int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
    public final static int CLIENT_SNAPSHOT = GameHelper.CLIENT_SNAPSHOT;
    public final static int CLIENT_ALL = CLIENT_GAMES | CLIENT_PLUS
             | CLIENT_SNAPSHOT;
    // The AppState slot we are editing.  For simplicity this sample only manipulates a single
    // Cloud Save slot and a corresponding Snapshot entry,  This could be changed to any integer
    // 0-3 without changing functionality (Cloud Save has four slots, numbered 0-3).
    private static final String TAG = "com.submarine.services";
    private Activity activity;
    private GameHelper gameHelper;
    private GameServicesListener<Snapshots.OpenSnapshotResult> gameServicesListener;
//    private boolean isSavedGamesLoadDone;
    private boolean waitingToShowAchievements;
    private boolean waitingToShowLeaderboard;
    private boolean waitingToShowLeaderboards;
    private String waitingToShowLeaderboardId;

    private boolean waitingToLoadEvents;
    private boolean waitingToShowQuests;
    private boolean waitingToLoadQuests;
    private boolean waitingToUpdateQuests;
    private LoadedEventListener eventListener;
    private LoadedQuestListener questListener;
    private QuestRewardListener questRewardListener;
    private ResultCallback<Quests.ClaimMilestoneResult> mClaimMilestoneResultCallback;
    private boolean waitingToGetPlayerInfo;
    private boolean waitingToCloudUpdate;
    private boolean waitingToCloudLoad;
    private CloudUpdateBundle waitingCloudUpdateBundle;
    private CloudLoadBundle waitingCloudLoadBundle;

    public final int MAX_AUTO_SIGN_IN_ATTEMPTS = 1;


    public AndroidGameServices(Activity activity, int clientsToUse) {
        this.activity = activity;
        gameHelper = new GameHelper(this.activity, clientsToUse);
        gameHelper.setMaxAutoSignInAttempts(MAX_AUTO_SIGN_IN_ATTEMPTS);
        gameHelper.setup(this);
        gameHelper.enableDebugLog(true);
//      isSavedGamesLoadDone = false;

        initQuestConstants();

        mClaimMilestoneResultCallback = new ResultCallback<Quests.ClaimMilestoneResult>() {
            @Override
            public void onResult(Quests.ClaimMilestoneResult result) {
                onMilestoneClaimed(result);
            }
        };
    }

    private void initQuestConstants() {
        QuestConstants.STATE_ACCEPTED           = Quest.STATE_ACCEPTED;
        QuestConstants.STATE_COMPLETED          = Quest.STATE_COMPLETED;
        QuestConstants.STATE_EXPIRED            = Quest.STATE_EXPIRED;
        QuestConstants.STATE_FAILED             = Quest.STATE_FAILED;
        QuestConstants.STATE_OPEN               = Quest.STATE_OPEN;
        QuestConstants.STATE_UPCOMING           = Quest.STATE_UPCOMING;
    }

    @Override
    public void checkPlayerStats() {
        PendingResult<Stats.LoadPlayerStatsResult> result =
                Games.Stats.loadPlayerStats(
                        gameHelper.getApiClient(), false /* forceReload */);
        result.setResultCallback(new
                                         ResultCallback<Stats.LoadPlayerStatsResult>() {
                                             public void onResult(Stats.LoadPlayerStatsResult result) {
                                                 Status status = result.getStatus();
                                                 if (status.isSuccess()) {
                                                     PlayerStats stats = result.getPlayerStats();
                                                     if (stats != null) {
                                                         gameServicesListener.playerStatsReceived(stats.getNumberOfPurchases(),stats.getSpendProbability(),stats.getChurnProbability());
                                                     }

                                                 } else {
                                                     System.out.println(TAG +  " Failed to fetch Stats Data status: "
                                                             + status.getStatusMessage());
                                                 }
                                             }
                                         });
    }
    public GoogleApiClient getApiClient() {
        return gameHelper.getApiClient();
    }

    public void onActivityResult(int request, int response, Intent data) {
        gameHelper.onActivityResult(request, response, data);
        if (response == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && gameHelper.isSignedIn()) {
            gameHelper.disconnect();
            setSignInCancelationssToMax();
        }

    }

    private final String GAMEHELPER_SHARED_PREFS = "GAMEHELPER_SHARED_PREFS";
    private final String KEY_SIGN_IN_CANCELLATIONS = "KEY_SIGN_IN_CANCELLATIONS";

    public void setSignInCancelationssToMax(){
        SharedPreferences.Editor editor = activity.getApplicationContext().getSharedPreferences(
                GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SIGN_IN_CANCELLATIONS, MAX_AUTO_SIGN_IN_ATTEMPTS);
        editor.commit();
    }


    // // ************** GOOGLE PART ***************\\\\\\\

    @Override
    public void onSignInFailed() {
        //Gdx.app.log(TAG, "Sing in Fail");
        waitingToShowLeaderboard = false;
        waitingToShowLeaderboards = false;
        waitingToShowAchievements = false;
        waitingToGetPlayerInfo = false;
        waitingToLoadEvents = false;
        waitingToShowQuests = false;
        waitingToLoadQuests = false;
        waitingToUpdateQuests = false;
        gameHelper.showFailureDialog();
        if (gameServicesListener != null) {
            gameServicesListener.onSignInFailed();
        }
    }

    @Override
    public void onSignInSucceeded() {
        //Gdx.app.log(TAG, "Sign in success");
        if (waitingToGetPlayerInfo) {
            loadUserInfo();
        }
        if (gameServicesListener != null) {
            gameServicesListener.onSignInSucceeded();
        }
        if (waitingToShowLeaderboard) {
            showLeaderBoard(waitingToShowLeaderboardId);
        }
        if (waitingToShowLeaderboards) {
            showLeaderBoards();
        }
        if (waitingToShowAchievements) {
            showAchievements();
        }
        if (waitingToLoadEvents) {
            loadEvents(eventListener);
        }
        if (waitingToShowQuests) {
            showQuests();
        }
        if (waitingToLoadQuests) {
            loadQuests(questListener);
        }
        if (waitingToUpdateQuests) {
            registerQuestUpdate(questRewardListener);
        }
        if (waitingToCloudLoad) {
            savedGamesLoad(waitingCloudLoadBundle.snapshotName, waitingCloudLoadBundle.createIfMissing);
        }
        if (waitingToCloudUpdate) {
            savedGamesUpdate(waitingCloudUpdateBundle.snapshotName, waitingCloudUpdateBundle.data, waitingCloudUpdateBundle.createIfMissing);
        }

    }

    // // ************** END GOOGLE PART ***************\\\\\\\

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void login() {
        if (gameHelper.isConnecting()) {
            return;
        }
        gameHelper.onStart(activity);
    }

    @Override
    public void logout() {
        gameHelper.onStop();
    }

    @Override
    public void submitScore(final String leaderBoardId, final long score) {
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderBoardId, score);
                }
            });
        }
    }
    @Override
    public void submitEvent(String eventId, int count) {
        String myEventId = eventId;
        Games.Events.increment(getApiClient(), myEventId, count);
    }
    @Override
    public void showEvents(){
        // EventCallback is a subclass of ResultCallback; use this to handle the
        // query results
        EventCallback ec = new EventCallback();

        // Load all events tracked for your game
        com.google.android.gms.common.api.PendingResult<Events.LoadEventsResult>
                pr = Games.Events.load(getApiClient(), true);
        pr.setResultCallback(ec);
    }
    class EventCallback implements ResultCallback {
        // Handle the results from the events load call
        public void onResult(com.google.android.gms.common.api.Result result) {
            Events.LoadEventsResult r = (Events.LoadEventsResult)result;
            com.google.android.gms.games.event.EventBuffer eb = r.getEvents();

            for (int i=0; i < eb.getCount(); i++) {
                // do something with the events retrieved
                //System.out.println(">>>>>>>>>>>>>>>>>SHOW" + eb.get(i).getValue());
            }
            eb.close();
        }
    }

    @Override
    public void showLeaderBoard(final String leaderBoardId) {
        //Gdx.app.log(TAG, "Show Leaderboard : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Show Leaderboard");
                    waitingToShowLeaderboard = false;
                    activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderBoardId), 2);
                }
            });
        } else {
            gameHelper.beginUserInitiatedSignIn();
            waitingToShowLeaderboard = true;
            waitingToShowLeaderboardId = leaderBoardId;
        }

    }

    @Override
    public void showLeaderBoards() {
        //Gdx.app.log(TAG, "Show Multiple Leaderboards : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Show Multiple Leaderboards");
                    waitingToShowLeaderboards = false;
                    activity.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient()), 3);
                }
            });
        } else {
//            gameHelper.beginUserInitiatedSignIn();
//            waitingToShowLeaderboards = true;
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
        if (isSignedIn()) {
            waitingToCloudLoad = false;
            final int retryCount = 0;
            PendingResult<Snapshots.OpenSnapshotResult> pendingResult = Games.Snapshots.open(
                    gameHelper.getApiClient(), snapshotName, createIfMissing);
//            isSavedGamesLoadDone = false;
            ResultCallback<Snapshots.OpenSnapshotResult> callback =
                    new ResultCallback<Snapshots.OpenSnapshotResult>() {
                        @Override
                        public void onResult(Snapshots.OpenSnapshotResult openSnapshotResult) {
                            processSnapshotOpenResult(openSnapshotResult, retryCount);
                        }
                    };
            pendingResult.setResultCallback(callback);
        } else {
            gameHelper.beginUserInitiatedSignIn();

            waitingToCloudLoad = true;

            waitingCloudLoadBundle = new CloudLoadBundle();
            waitingCloudLoadBundle.snapshotName = snapshotName;
            waitingCloudLoadBundle.createIfMissing = createIfMissing;
        }

    }

    private void processSnapshotOpenResult(Snapshots.OpenSnapshotResult openSnapshotResult, int retryCount) {
        //Gdx.app.log(TAG, "conflictId after resolve= " + openSnapshotResult.getConflictId() );
        int status = openSnapshotResult.getStatus().getStatusCode();
        switch (status) {
            case GamesStatusCodes.STATUS_OK:
                gameServicesListener.savedGamesLoadSucceeded(openSnapshotResult);
                break;
            case GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE:
                gameServicesListener.savedGamesLoadContentsUnavailable(openSnapshotResult);
                break;
            case GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT:
                gameServicesListener.savedGamesLoadConflicted(openSnapshotResult, retryCount);
                break;
            default:
                gameServicesListener.savedGamesLoadFailed(openSnapshotResult);
                break;
        }
        /*if (status == GamesStatusCodes.STATUS_OK*//* || status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE*//*) {
            gameServicesListener.savedGamesLoadDone();
            isSavedGamesLoadDone = true;
        }*/
    }

    @Override
    public void showSavedGamesUI() {
        int maxNumberOfSavedGamesToShow = 5;
        Intent savedGamesIntent = Games.Snapshots.getSelectSnapshotIntent(gameHelper.getApiClient(),
                "See My Saves", true, true, maxNumberOfSavedGamesToShow);
        activity.startActivityForResult(savedGamesIntent, 9009);
    }


    /**
     * Update the Snapshot in the Saved Games service with new data.  Metadata is not affected,
     * however for your own application you will likely want to update metadata such as cover image,
     * played time, and description with each Snapshot update.  After update, the UI will
     * be cleared.
     */
    @Override
    public void savedGamesUpdate(final String snapshotName, final byte[] data, final boolean createIfMissing) {
        if (isSignedIn()) {
            waitingToCloudUpdate = false;
            AsyncTask<Void, Void, Boolean> updateTask = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    Snapshots.OpenSnapshotResult open = Games.Snapshots.open(gameHelper.getApiClient(), snapshotName, createIfMissing).await();

                    if (!open.getStatus().isSuccess()) {
                        //Gdx.app.log(TAG, "Could not open Snapshot for update.");
                        gameServicesListener.savedGamesUpdateFailed();
                        return false;
                    }

                    // Change data but leave existing metadata
                    Snapshot snapshot = open.getSnapshot();
                    snapshot.getSnapshotContents().writeBytes(data);

                    Snapshots.CommitSnapshotResult commit = Games.Snapshots.commitAndClose(
                            gameHelper.getApiClient(), snapshot, SnapshotMetadataChange.EMPTY_CHANGE).await();

                    if (!commit.getStatus().isSuccess()) {
                        //Gdx.app.log(TAG, "Failed to commit Snapshot.");
                        gameServicesListener.savedGamesUpdateFailed();
                        return false;
                    }
                    // No failures
                    //Gdx.app.log(TAG, "No failures for Snapshot update.");
                    gameServicesListener.savedGamesUpdateSucceeded();
                    return true;
                }
            };
            updateTask.execute();
        } else {
            gameHelper.beginUserInitiatedSignIn();

            waitingToCloudUpdate = true;

            waitingCloudUpdateBundle = new CloudUpdateBundle();
            waitingCloudUpdateBundle.snapshotName = snapshotName;
            waitingCloudUpdateBundle.data = data;
            waitingCloudUpdateBundle.createIfMissing = createIfMissing;


        }
    }

    private class CloudUpdateBundle {
        public String snapshotName;
        public byte[] data;
        public boolean createIfMissing;
    }

    private class CloudLoadBundle {
        public String snapshotName;
        public boolean createIfMissing;
    }

    public void resolveSavedGamesConflict(Snapshots.OpenSnapshotResult openResult, final Snapshot resolvedSnapshot, final int retryCount, final int maxSnapshotResolveRetries) {
        final String conflictId = openResult.getConflictId();
        //Gdx.app.log(TAG, "conflictId before resolve= " + conflictId );
        ////System.out.println(TAG + "resolvedSnapshot pre resolve " + resolvedSnapshot.toString());
        AsyncTask<Void, Void, Boolean> updateTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(gameHelper.getApiClient(), conflictId, resolvedSnapshot).await();
                ////System.out.println(TAG + "snapshot post resolve " + resolveResult.getSnapshot().toString());
                ////System.out.println(TAG + "conflict snapshot post resolve " + resolveResult.getConflictingSnapshot().toString());
                if (retryCount < maxSnapshotResolveRetries) {
                    // Recursively attempt again
                    //gameServicesListener.savedGamesLoadConflicted(resolveResult, retryCount + 1);
                    processSnapshotOpenResult(resolveResult, retryCount + 1);

                } else {
                    // Failed, log error and show Toast to the user
                    String message = "Could not resolve snapshot conflicts";
                    Gdx.app.error(TAG, message);
                }
                return true;
            }
        };
        updateTask.execute();
    }

    @Override
    public void setListener(GameServicesListener gameServicesListener) {
        this.gameServicesListener = gameServicesListener;
    }

    /*@Override
    public boolean isSavedGamesLoadDone() {
        return isSavedGamesLoadDone;
    }*/

    @Override
    public void loadUserInfo() {
        //Gdx.app.log(TAG, "Get Player Info : " + isSignedIn());
        if (isSignedIn()) {
            waitingToGetPlayerInfo = false;
            Person person = Plus.PeopleApi.getCurrentPerson(gameHelper.getApiClient());
            if(person != null){
                try {
                    CurrentUser.getInstance().init(person.getDisplayName(), person.getImage().getUrl());
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }
            }

        } else {
            waitingToGetPlayerInfo = true;
            //gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void unlockAchievement(final String achievementId) {
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
                }
            });
        }
    }

    @Override
    public void incrementAchievement(final String achievementId, final int incrementAmount) {
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Games.Achievements.increment(gameHelper.getApiClient(), achievementId, incrementAmount);
                }
            });
        }
    }

    @Override
    public void showAchievements() {
        //Gdx.app.log(TAG, "Show Achievements : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Show Achievements");
                    waitingToShowAchievements = false;
                    activity.startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 1);
                }
            });
        } else {
            waitingToShowAchievements = true;
            gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void loadEvents(LoadedEventListener listener) {
        this.eventListener = listener;

        //Gdx.app.log(TAG, "Load Events : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Load Events");
                    waitingToLoadEvents = false;
                    EventCallback ec = new EventCallback();

                    // Load all events tracked for your game
                    PendingResult<Events.LoadEventsResult> pr = Games.Events.load(gameHelper.getApiClient(), true);
                    pr.setResultCallback(ec);
                }
            });
        } else {
            waitingToLoadEvents = true;
            gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void loadEventsByIds(LoadedEventListener listener, final String... eventIds) {
        this.eventListener = listener;

        //Gdx.app.log(TAG, "Load Events : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Load Events" + eventIds);
                    waitingToLoadEvents = false;
                    EventCallback ec = new EventCallback();

                    // Load all events tracked for your game
                    PendingResult<Events.LoadEventsResult> pr = Games.Events.loadByIds(gameHelper.getApiClient(), true, eventIds);
                    pr.setResultCallback(ec);
                }
            });
        } else {
            waitingToLoadEvents = true;
            gameHelper.beginUserInitiatedSignIn();
        }

    }

    @Override
    public void showQuests() {
        //Gdx.app.log(TAG, "Show Quests : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Show Quests");
                    waitingToShowQuests = false;
                    //filter the shown quests if necessary
                    Intent questsIntent = Games.Quests.getQuestsIntent(gameHelper.getApiClient(), Quests.SELECT_ALL_QUESTS);
                    activity.startActivityForResult(questsIntent, 5);
                }
            });
        } else {
            waitingToShowQuests = true;
            gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void loadQuests(LoadedQuestListener listener) {
        this.questListener = listener;

        //Gdx.app.log(TAG, "Load Quests : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Load quest data");
                    waitingToLoadQuests = false;

                    QuestCallBack qc = new QuestCallBack();

                    // Load all quests
                    // can be sorted by recently updated first
                    PendingResult<Quests.LoadQuestsResult> pr = Games.Quests.load(gameHelper.getApiClient(),
                            Quests.SELECT_ALL_QUESTS, Quests.SORT_ORDER_ENDING_SOON_FIRST, true);
                    pr.setResultCallback(qc);
                }
            });
        } else {
            waitingToLoadQuests = true;
            gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void loadQuestsByIds(LoadedQuestListener listener, final String... questIds) {
        this.questListener = listener;

        //Gdx.app.log(TAG, "Load Quests : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Load quest data");
                    waitingToLoadQuests = false;

                    QuestCallBack qc = new QuestCallBack();

                    // Load quests with current ids
                    PendingResult<Quests.LoadQuestsResult> pr = Games.Quests.loadByIds(gameHelper.getApiClient(), true, questIds);
                    pr.setResultCallback(qc);
                }
            });
        } else {
            waitingToLoadQuests = true;
            gameHelper.beginUserInitiatedSignIn();
        }
    }

    @Override
    public void registerQuestUpdate(final QuestRewardListener listener) {
        this.questRewardListener = listener;

        //Gdx.app.log(TAG, "Register Quests : " + isSignedIn());
        if (isSignedIn()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //Gdx.app.log(TAG, "Register quest");
                    waitingToUpdateQuests = false;

                    // Start the quest listener.
                    Games.Quests.registerQuestUpdateListener(gameHelper.getApiClient(), new QuestUpdateListener() {
                        @Override
                        public void onQuestCompleted(Quest quest) {
                            // Claim the quest reward.
                            Games.Quests.claim(gameHelper.getApiClient(),
                                    quest.getQuestId(),
                                    quest.getCurrentMilestone().getMilestoneId())
                                    .setResultCallback(mClaimMilestoneResultCallback);

                            // Process the RewardData to provision a specific reward.
//                            String reward = new
//                                    String(quest.getCurrentMilestone().getCompletionRewardData(),
//                                    Charset.forName("UTF-8"));
//
//                            questRewardListener.reward(reward);
                        }
                    });
                }
            });
        } else {
//            waitingToUpdateQuests = true;
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    gameHelper.beginUserInitiatedSignIn();
//                }
//            });
        }
    }

    private class QuestCallBack implements ResultCallback {

        // Handle the results from the quests load call
        public void onResult(Result result) {
            Quests.LoadQuestsResult r = (Quests.LoadQuestsResult) result;
            QuestBuffer qb = r.getQuests();

            for (int i = 0; i < qb.getCount(); i++) {
                Quest quest = qb.get(i);

                questListener.info(quest.getName(),
                        quest.getDescription(),
                        quest.getState());
            }
            qb.close();
        }
    }

    public void onMilestoneClaimed(Quests.ClaimMilestoneResult result) {
            // Process the RewardData binary array to provide a specific reward and present the
            // information to the user.
        try {
            if (result.getStatus().isSuccess()) {
                String reward = new String(result.getQuest().getCurrentMilestone().
                        getCompletionRewardData(),
                            "UTF-8");

                    questRewardListener.reward(reward);
                } else {
                    Log.e(TAG, "Reward was not claimed due to error.");
                    Toast.makeText(activity, "Reward was not claimed due to error.",
                            Toast.LENGTH_LONG).show();
                }
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
        }

    // checks for internet connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
