package com.kurume_nct.himawari;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends BaseDialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static TimePickerDialogFragment newInstance(Fragment fragment, int requestCode) {
        TimePickerDialogFragment dialog = new TimePickerDialogFragment();
        dialog.setTargetFragment(fragment, requestCode);
        return dialog;
    }

    public OnValueSetListener getListener() {
        Fragment fragment = getTargetFragment();
        if (fragment instanceof OnValueSetListener) {
            return (OnValueSetListener) fragment;
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, hour, minute, true);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String val = String.format("%d:%d", hour, minute);
        try {
            getListener().onValueSet(getTargetRequestCode(), val);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }
}
