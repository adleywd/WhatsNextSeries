package br.com.adley.whatsnextseries.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.Calendar;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.library.AppConsts;
import br.com.adley.whatsnextseries.service.NotificationAlarmManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    public static NotificationsFragment newInstance(){
        return new NotificationsFragment();
    }

    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    private Switch mSwitchNotifications;
    private TextView mTimeDisplay;
    private int mHours = 0;
    private int mMinutes = 0;
    private boolean mNotifyEnabled = false;
    private Calendar mCalendar;
    private DecimalFormat mDecimalFormat;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Load SharedConfigs
        final View notificationsFragment = inflater.inflate(R.layout.fragment_notifications, container, false);
        loadConfigPreferences();
        mCalendar = Calendar.getInstance();

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(notificationsFragment.getContext(), NotificationAlarmManager.class);
        mPendingIntent = PendingIntent.getBroadcast(notificationsFragment.getContext(), 0, alarmIntent, 0);

        notificationsFragment.findViewById(R.id.set_time_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        notificationsFragment.findViewById(R.id.notification_help_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogHelp(v);
            }
        });

        mSwitchNotifications = (Switch) notificationsFragment.findViewById(R.id.switch_notification);
        mTimeDisplay = (TextView) notificationsFragment.findViewById(R.id.time_text_display);
        mTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        if(mNotifyEnabled){
            mSwitchNotifications.setChecked(true);
        }else{
            mSwitchNotifications.setChecked(false);
        }
        mDecimalFormat = new DecimalFormat("00");
        mTimeDisplay.setText(getTimeDisplay());

        mAlarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

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

        return notificationsFragment;
    }


    private void loadConfigPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mHours = sharedPreferences.getInt(AppConsts.NOTIFICATION_HOUR_KEY, 13);
        mMinutes = sharedPreferences.getInt(AppConsts.NOTIFICATION_MINUTE_KEY, 0);
        mNotifyEnabled = sharedPreferences.getBoolean(AppConsts.NOTIFICATION_ENABLED_KEY, false);
    }

    private void updateConfigPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putInt(AppConsts.NOTIFICATION_HOUR_KEY, mHours);
        spEditor.putInt(AppConsts.NOTIFICATION_MINUTE_KEY, mMinutes);
        spEditor.putBoolean(AppConsts.NOTIFICATION_ENABLED_KEY, mNotifyEnabled);
        spEditor.apply();
    }

    public void startAlarm(View view) {
        Toast.makeText(getContext(), getToastTimeDisplay(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), getString(R.string.alarm_off), Toast.LENGTH_SHORT).show();
            mAlarmManager.cancel(mPendingIntent);
            mNotifyEnabled = false;
            updateConfigPreferences();
        }
    }

    public void showTimePickerDialog(View v) {
        //TODO CHECK LATER
//        TimePickerFragment newFragment = new TimePickerFragment();
//        newFragment.setTargetFragment(this,0);
//        newFragment.show(this.getFragmentManager(), "timePicker");
    }

    public void onTimeChosen(TimePicker view, int hourOfDay, int minute){
        mHours = hourOfDay;
        mMinutes = minute;
        mNotifyEnabled = true;
        mTimeDisplay.setText(String.format("%s:%s", mDecimalFormat.format(mHours), mDecimalFormat.format(mMinutes)));
        updateConfigPreferences();
        if(mAlarmManager != null){
            mAlarmManager.cancel(mPendingIntent);
        }
        startAlarm(view);
        mSwitchNotifications.setChecked(true);
        Toast.makeText(getActivity(), getToastTimeDisplay(), Toast.LENGTH_SHORT).show();
    }

    public void showDialogHelp(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.notification_description_help))
                .setCancelable(true)
                .setIcon(R.drawable.ic_help_outline_black_24dp)
                .setTitle(getString(R.string.help_notifications_title))
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private String getTimeDisplay(){
        return  getString(R.string.time_display_format, mDecimalFormat.format(mHours), mDecimalFormat.format(mMinutes));
    }
    private String getToastTimeDisplay(){
        return  getString(R.string.alarm_set, mDecimalFormat.format(mHours), mDecimalFormat.format(mMinutes));
    }

}
