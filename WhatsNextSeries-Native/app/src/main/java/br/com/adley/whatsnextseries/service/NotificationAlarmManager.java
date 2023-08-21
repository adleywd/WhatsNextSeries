package br.com.adley.whatsnextseries.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.activities.MainActivity;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.library.Utils;
import br.com.adley.whatsnextseries.library.enums.DownloadStatus;
import br.com.adley.whatsnextseries.models.TVShowDetails;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

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
    private int mTVShowDetailsListCount;
    private List<Integer> mIdShowList;
    private int mCountAiringToday = 0;
    public static final String CHANNEL_AIR_TODAY_ID = "air_today_channel";
    private NotificationManagerCompat mNotificationManager;
    private List<TVShowDetails> mTVShowsAiringToday;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Init vars
        mContext = context;
        mIdShowList = new ArrayList<>();
        mTVShowsAiringToday = new ArrayList<>();

        mNotificationManager = NotificationManagerCompat.from(context);

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
            NotificationAlarmManager.ProcessSeason.ProcessData processData = new NotificationAlarmManager.ProcessSeason.ProcessData();
            processData.execute();
        }

        class ProcessData extends DownloadJsonData {
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTVShowDetailsListCount++;
                // Set next episode
                boolean isAiringTodayUtils = Utils.isShowAiringToday(getTVShowSeasons(), mContext);
                if(isAiringTodayUtils){
                    mCountAiringToday++;
                    mTVShowsAiringToday.add(getTVShowDetails());
                }
                if (mIdShowList.size() == mTVShowDetailsListCount) {
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

    private void buildNotification(int favoritesAiringToday) {
        if (favoritesAiringToday > 0) {
            sendToAirTodayChannel(favoritesAiringToday);
        }
    }

    public void sendToAirTodayChannel(int favoritesAiringToday) {

        // Set notification click destination
        Intent homeIntent = new Intent(mContext, MainActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(favoritesAiringToday > 1 ?
                mContext.getString(R.string.notification_message, Utils.generateTvShowsNameList(mContext,mTVShowsAiringToday)) :
                mContext.getString(R.string.notification_message_single_show, Utils.generateTvShowsNameList(mContext,mTVShowsAiringToday))
        );

        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_AIR_TODAY_ID)
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(favoritesAiringToday > 1 ?
                        mContext.getString(R.string.notification_message_general, favoritesAiringToday) :
                        mContext.getString(R.string.notification_message_general_single_show, favoritesAiringToday))
                .setStyle(bigTextStyle) // Show all shows airing today
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(PendingIntent.getActivity(mContext, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true) // Close notification when clicked.
                .build();

        mNotificationManager.notify(AppConsts.NOTIFCATION_ID_AIR_TODAY_CHANNEL, notification);
    }

}
