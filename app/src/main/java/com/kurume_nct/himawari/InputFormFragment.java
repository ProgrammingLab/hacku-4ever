package com.kurume_nct.himawari;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;


public class InputFormFragment extends Fragment implements View.OnClickListener {
    public static final String PRICE_KEY = "price_";
    public static final String DURATION_KEY = "duration_";
    public static final String POS_KEY = "marker_position";

    private TimePicker durationPicker;
    private EditText priceText;
    private Button submitButton;

    public InputFormFragment() {
        // Required empty public constructor
    }

    public static InputFormFragment newInstance(Fragment fragment, int requestCode, LatLng markerPos) {
        InputFormFragment self = new InputFormFragment();
        self.setTargetFragment(fragment, requestCode);

        Bundle args = new Bundle();
        args.putParcelable(POS_KEY, markerPos);
        self.setArguments(args);
        return self;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_form, container, false);

        Bundle args = getArguments();
        LatLng markerPos = args.getParcelable(POS_KEY);
        Log.d("HOGE", markerPos.toString());

        durationPicker = (TimePicker) v.findViewById(R.id.duration);
        priceText = (EditText) v.findViewById(R.id.price);
        submitButton = (Button) v.findViewById(R.id.form_submit_button);
        submitButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        Intent result = new Intent();
        result.putExtra(PRICE_KEY, priceText.getText().toString());

        int hour, minute;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            hour = durationPicker.getHour();
            minute = durationPicker.getMinute();
        } else {
            hour = durationPicker.getCurrentHour();
            minute = durationPicker.getCurrentMinute();
        }
        result.putExtra(DURATION_KEY, String.format("%d:%d", hour, minute));
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
            getFragmentManager().popBackStack();
        }
    }
}
