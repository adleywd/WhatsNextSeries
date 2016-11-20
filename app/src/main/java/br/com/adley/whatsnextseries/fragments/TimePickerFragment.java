package br.com.adley.whatsnextseries.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    // Notice I removed "implements OnTimeSetListener" and changed the variables

    private Activity mActivity;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    int hour, minute;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mTimeSetListener = (TimePickerDialog.OnTimeSetListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(mActivity, mTimeSetListener, hour, minute, true);
    }
}