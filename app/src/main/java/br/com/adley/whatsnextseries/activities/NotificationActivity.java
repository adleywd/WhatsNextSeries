package br.com.adley.whatsnextseries.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.fragments.TimePickerFragment;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.service.NotificationAlarmManager;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{

    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    private Switch mSwitchNotifications;
    private TextView mTimeDisplay;
    private int mHours = 0;
    private int mMinutes = 0;
    private boolean mNotifyEnabled = false;
    private Calendar mCalendar;
    private DecimalFormat mDecimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Load SharedConfigs
        loadConfigPreferences();
        mCalendar = Calendar.getInstance();

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, NotificationAlarmManager.class);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        final Button notification = (Button) findViewById(R.id.fire_notification);

        mSwitchNotifications = (Switch) findViewById(R.id.switch_notification);
        mTimeDisplay = (TextView) findViewById(R.id.time_text_display);
        if(mNotifyEnabled){
            mSwitchNotifications.setChecked(true);
        }else{
            mSwitchNotifications.setChecked(false);
        }
        mDecimalFormat = new DecimalFormat("00");
        mTimeDisplay.setText(mDecimalFormat.format(mHours)+":"+mDecimalFormat.format(mMinutes));

        mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        mSwitchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startAlarm(buttonView);
                }else{
                    cancelAlarm(buttonView);
                }
            }
        });
        /*start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarm(v);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(v);
            }
        });
        */
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildNotification(2);
            }
        });


    }

    private void loadConfigPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
        mHours = sharedPreferences.getInt(AppConsts.NOTIFICATION_HOUR_KEY, 0);
        mMinutes = sharedPreferences.getInt(AppConsts.NOTIFICATION_MINUTE_KEY, 0);
        mNotifyEnabled = sharedPreferences.getBoolean(AppConsts.NOTIFICATION_ENABLED_KEY, false);
    }

    private void updateConfigPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NotificationActivity.this);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putInt(AppConsts.NOTIFICATION_HOUR_KEY, mHours);
        spEditor.putInt(AppConsts.NOTIFICATION_MINUTE_KEY, mMinutes);
        spEditor.putBoolean(AppConsts.NOTIFICATION_ENABLED_KEY, mNotifyEnabled);
        spEditor.apply();
    }

    public void startAlarm(View view) {
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        //mCalendar.add(Calendar.DAY_OF_YEAR, 1); // Avoid to run immediately.
        mCalendar.set(Calendar.HOUR_OF_DAY, mHours);
        mCalendar.set(Calendar.MINUTE, mMinutes);
        mCalendar.set(Calendar.SECOND, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, mPendingIntent);
        mNotifyEnabled = true;
        updateConfigPreferences();
    }

    public void cancelAlarm(View view) {
        if (mAlarmManager != null) {
            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
            mAlarmManager.cancel(mPendingIntent);
            mNotifyEnabled = false;
            updateConfigPreferences();
        }
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHours = hourOfDay;
        mMinutes = minute;
        mNotifyEnabled = true;
        mTimeDisplay.setText(mDecimalFormat.format(mHours)+":"+mDecimalFormat.format(mMinutes));
        updateConfigPreferences();
        if(mAlarmManager != null){
            mAlarmManager.cancel(mPendingIntent);
        }
        startAlarm(view);
        mSwitchNotifications.setChecked(true);
        Toast.makeText(NotificationActivity.this, "Alarm set to: "+ mHours +":"+ mMinutes, Toast.LENGTH_SHORT).show();
    }



    private void buildNotification(int favoritesAiringToday){
        // Sets an ID for the notification
        int mNotificationId = 1;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle("WhatsNext-Series")
                        .setContentText(favoritesAiringToday + " favorites tv shows airing today");

        //set Uri for sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //add sound when notification comes in.
        mBuilder.setSound(alarmSound);
        //add vibration when notification comes in.
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
