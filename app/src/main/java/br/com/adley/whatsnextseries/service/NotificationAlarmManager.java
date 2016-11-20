package br.com.adley.whatsnextseries.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.activities.HomeActivity;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShowDetails;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Adley.Damaceno on 18/11/2016.
 * Manage the Alarm to send notification.
 */

public class NotificationAlarmManager extends BroadcastReceiver {

    private String LOG_TAG = NotificationAlarmManager.class.getSimpleName();
    private Context mContext;
    private boolean mIsLanguageUsePtBr;
    private String mPosterSize;
    private String mBackDropSize;
    private List<TVShowDetails> mTVShowDetailsList;
    private List<Integer> mIdShowList;
    private int mCountAiringToday = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        //Init vars
        mContext = context;
        mTVShowDetailsList = new ArrayList<>();
        mIdShowList = new ArrayList<>();

        //Load preferences
        loadConfigPreferences();

        SharedPreferences sharedPref = context.getSharedPreferences(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        String restoredFavoritesListString = sharedPref.getString(AppConsts.FAVORITES_SHAREDPREFERENCES_KEY, null);
        if(restoredFavoritesListString != null && !restoredFavoritesListString.isEmpty()) {
            mIdShowList = Utils.convertStringToIntegerList(AppConsts.FAVORITES_SHAREDPREFERENCES_DELIMITER, restoredFavoritesListString);

            //Get Show Details Data
            for (int idShow: mIdShowList) {
                NotificationAlarmManager.ProcessTVShowsDetails processTVShowsDetails = new NotificationAlarmManager.ProcessTVShowsDetails(idShow , mPosterSize, mBackDropSize, mIsLanguageUsePtBr);
                processTVShowsDetails.execute();
            }
        }
    }

    // Process and execute data from show
    public class ProcessTVShowsDetails extends GetTVShowDetailsJsonData {
        private NotificationAlarmManager.ProcessTVShowsDetails.ProcessData processData;

        ProcessTVShowsDetails(int idShow, String posterSize, String backDropSize, boolean isLanguageUsePtBr) {
            super(idShow, posterSize, backDropSize, mContext, isLanguageUsePtBr);
        }

        public void execute() {
            // Start process data (download and get)
            processData = new NotificationAlarmManager.ProcessTVShowsDetails.ProcessData();
            processData.execute();
        }

        class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                if (getDownloadStatus() == DownloadStatus.OK || getTVShowsDetails() != null) {
                    //Get and Process SeasonData
                    if (getTVShowsDetails().getSeasonNumberList() != null && getTVShowsDetails().getSeasonNumberList().size() > 0
                            && getTVShowsDetails().isInProduction()) {
                        int lastSeason = Utils.maxNumber(getTVShowsDetails().getSeasonNumberList());
                        NotificationAlarmManager.ProcessSeason processSeason = new NotificationAlarmManager.ProcessSeason(getTVShowsDetails(), lastSeason);
                        processSeason.execute();
                    }
                }
            }
        }
    }

    // Process Season Data
    public class ProcessSeason extends GetTVShowSeasonJsonData {

        ProcessSeason(TVShowDetails show, int showNumber) {
            super(show, showNumber, mContext);
        }

        public void execute() {
            // Start process data (download and get)
            Log.d(LOG_TAG, "execute season");
            NotificationAlarmManager.ProcessSeason.ProcessData processData = new NotificationAlarmManager.ProcessSeason.ProcessData();
            processData.execute();
        }

        class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                Log.d(LOG_TAG, "OnPostExecute");
                super.onPostExecute(webData);
                mTVShowDetailsList.add(getTVShowDetails());
                Log.d(LOG_TAG, mTVShowDetailsList.toString());
                // Set next episode
                boolean isAiringTodayUtils = Utils.isShowAiringToday(getTVShowSeasons(), mContext);
                if(isAiringTodayUtils){
                    mCountAiringToday++;
                }
                Log.d(LOG_TAG, String.valueOf(mCountAiringToday));
                if (mIdShowList.size() == mTVShowDetailsList.size()) {
                    buildNotification(mCountAiringToday);
                }
            }
        }
    }

    private void loadConfigPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mIsLanguageUsePtBr = sharedPreferences.getBoolean(AppConsts.LANGUAGE_USE_PTBR, false);
        mPosterSize = sharedPreferences.getString(mContext.getString(R.string.preferences_poster_size_key), AppConsts.POSTER_DEFAULT_SIZE);
        mBackDropSize = sharedPreferences.getString(mContext.getString(R.string.preferences_backdrop_size_key), AppConsts.BACKDROP_DEFAULT_SIZE);
    }

    private void buildNotification(int favoritesAiringToday){
        // Sets an ID for the notification
        int mNotificationId = 1;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle("WhatsNext-Series")
                        .setContentText(favoritesAiringToday + " favorites tv shows airing today");

        //set Uri for sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //add sound when notification comes in.
        mBuilder.setSound(alarmSound);
        //add vibration when notification comes in.
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Intent homeIntent = new Intent(mContext, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
