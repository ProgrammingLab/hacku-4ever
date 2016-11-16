package com.kurume_nct.himawari;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class TimePickerDialogFragment extends BaseDialogFragment {

    private View hour;
    private View minute;

    public static TimePickerDialogFragment newInstance(Fragment fragment, int requestCode) {
        TimePickerDialogFragment dialog = new TimePickerDialogFragment();
        dialog.setTargetFragment(fragment, requestCode);
        return dialog;
    }

    private View createPickerView(int hourValue, int minuteValue) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.time_picker, null);

        NumberPicker hour = (NumberPicker) view.findViewById(R.id.picker_hour);
        hour.setMinValue(0);
        hour.setMaxValue(23);
        hour.setValue(hourValue);
        this.hour = hour;

        NumberPicker minute = (NumberPicker) view.findViewById(R.id.picker_minute);
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setValue(minuteValue);
        this.minute = minute;

        return view;
    }

    private int getHour() {
        NumberPicker picker = (NumberPicker) hour;
        return picker.getValue();
    }

    private int getMinute() {
        NumberPicker picker = (NumberPicker) minute;
        return picker.getValue();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(createPickerView(0, 0));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String val = String.format("%d:%d", getHour(), getMinute());
                OnValueSetListener listener = getListener();
                if (listener != null) {
                    listener.onValueSet(getTargetRequestCode(), val);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setCancelable(true);

        return builder.create();
    }
}
