package com.kurume_nct.himawari;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class InputFormFragment extends Fragment implements ListView.OnItemClickListener, BaseDialogFragment.OnValueSetListener {
    public static final String PRICE_KEY = "price_";
    public static final String DURATION_HOUR = "duration_hour";
    public static final String DURATION_MINUTE = "duration_minute";
    public static final String POS_KEY = "marker_position";

    private int price;
    private int hour;
    private int minute;

    private ArrayList<InputFormItem> items;
    private ListView listView;


    public InputFormFragment() {
        // Required empty public constructor
    }

    public static InputFormFragment newInstance(Fragment fragment, int requestCode, LatLng markerPos) {
        InputFormFragment self = new InputFormFragment();
        self.setTargetFragment(fragment, requestCode);

        Bundle args = new Bundle();
        args.putInt(PRICE_KEY, 500);
        args.putInt(DURATION_HOUR, 0);
        args.putInt(DURATION_MINUTE, 30);
        args.putParcelable(POS_KEY, markerPos);
        self.setArguments(args);
        return self;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PRICE_KEY)) {
                price = savedInstanceState.getInt(PRICE_KEY);
            }
            if (savedInstanceState.containsKey(DURATION_HOUR)) {
                hour = savedInstanceState.getInt(DURATION_HOUR);
            }
            if (savedInstanceState.containsKey(DURATION_MINUTE)) {
                minute = savedInstanceState.getInt(DURATION_MINUTE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PRICE_KEY, price);
        outState.putInt(DURATION_HOUR, hour);
        outState.putInt(DURATION_MINUTE, minute);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_form, container, false);

        Bundle args = getArguments();
        LatLng markerPos = args.getParcelable(POS_KEY);
        Log.d("HOGE", markerPos.toString());

        if (v instanceof ListView) {
            listView = (ListView) v;
            listView.setOnItemClickListener(this);
            items = new ArrayList<InputFormItem>();
            items.add(new InputFormItem(getString(R.string.price_label), Integer.toString(price), PriceDialogFragment.newInstance(this, 0, getString(R.string.price_label))));
            items.add(new InputFormItem(getString(R.string.duration_label), Integer.toString(hour) + ":" + Integer.toString(minute), TimePickerDialogFragment.newInstance(this, 1)));
            listView.setAdapter(new InputListViewAdapter(getContext(), R.layout.fragment_input_item, items));
        }
        return v;
    }

    private void returnValue(Intent result) {
        Fragment fragment = getTargetFragment();
        if (fragment != null) {
            fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListView listView = (ListView) adapterView;
        InputFormItem item = (InputFormItem) listView.getItemAtPosition(i);
        item.getDialog().show(getFragmentManager(), item.getLabel());
    }

    @Override
    public void onValueSet(int requstCode, String val) {
        if (requstCode == 0) {
            items.get(0).setValue(val);
        }
        if (requstCode == 1) {
            items.get(1).setValue(val);
        }
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    public class InputFormItem {
        private String label;
        private String val;

        private DialogFragment dialog;

        public InputFormItem(String label, String val, DialogFragment dialogFragment) {
            this.label = label;
            this.val = val;
            this.dialog = dialogFragment;
        }

        public String getLabel() {
            return this.label;
        }

        public String getValue() {
            return this.val;
        }

        public void setValue(String val) {
            this.val = val;
        }

        public DialogFragment getDialog() {
            return this.dialog;
        }
    }
}
