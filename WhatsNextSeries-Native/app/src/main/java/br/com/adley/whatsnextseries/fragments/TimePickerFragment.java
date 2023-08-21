package br.com.adley.whatsnextseries.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.adley.whatsnextseries.R;
import br.com.adley.whatsnextseries.library.AppConsts;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    // Notice I removed "implements OnTimeSetListener" and changed the variables

    int hour, minute;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.DialogTheme,this, hour, minute, true);
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        NotificationsFragment notificationsFragment = (NotificationsFragment) getFragmentManager().findFragmentByTag(AppConsts.TAG_NOTIFICATIONS);
        notificationsFragment.onTimeChosen(view,hourOfDay,minute);
    }
}