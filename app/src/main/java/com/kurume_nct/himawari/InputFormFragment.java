package com.kurume_nct.himawari;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class InputFormFragment extends Fragment implements ListView.OnItemClickListener, BaseDialogFragment.OnValueSetListener {
    public static  final String IS_SUBMIT = "submit_";
    public static final String PRICE_KEY = "price_";
    public static final String TIME_KEY = "time_";
    public static final String POS_KEY = "marker_position";

    private int price;
    private Date time;
    private LatLng markerPos;

    private ArrayList<InputFormItem> items;
    private ListView listView;


    public InputFormFragment() {
        // Required empty public constructor
    }

    public static InputFormFragment newInstance(Fragment fragment, int requestCode, LatLng markerPos) {
        InputFormFragment self = new InputFormFragment();
        self.setTargetFragment(fragment, requestCode);
        Date time = new Date();

        Bundle args = new Bundle();
        args.putInt(PRICE_KEY, 500);
        args.putSerializable(TIME_KEY, time);
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
        importFromBundle(savedInstanceState);
    }

    private void importFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PRICE_KEY)) {
                price = savedInstanceState.getInt(PRICE_KEY);
            }
            if (savedInstanceState.containsKey(TIME_KEY)) {
                time = (Date) savedInstanceState.getSerializable(TIME_KEY);
            }
            if (savedInstanceState.containsKey(POS_KEY)) {
                markerPos = savedInstanceState.getParcelable(POS_KEY);
            }
        }
    }

    public int getPrice() {
        return price;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PRICE_KEY, price);
        outState.putSerializable(TIME_KEY, time);
        outState.putParcelable(POS_KEY, markerPos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_form, container, false);

        Bundle args = getArguments();
        importFromBundle(args);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.form_title));
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(IS_SUBMIT, 0);
                returnValue(result);
            }
        });
        toolbar.inflateMenu(R.menu.submit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent result = new Intent();
                result.putExtra(IS_SUBMIT, 1);
                result.putExtra(TIME_KEY, time);
                result.putExtra(PRICE_KEY, price);
                returnValue(result);
                return true;
            }
        });

        listView = (ListView) v.findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        items = new ArrayList<InputFormItem>();
        items.add(new InputFormItem(getString(R.string.price_label), price, PriceDialogFragment.newInstance(this, 0, getPrice())));
        items.add(new InputFormItem(getString(R.string.time_label), time, TimePickerDialogFragment.newInstance(this, 1, getTime())));
        listView.setAdapter(new InputListViewAdapter(getContext(), R.layout.fragment_input_item, items));
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
    public void onValueSet(int requstCode, Object val) {
        if (requstCode == 0) {
            String tmp = (String) val;
            try {
                price = Integer.parseInt(tmp);
            } catch (NumberFormatException e) {
                price = 0;
            }
            items.get(0).setValue(val);
        }
        if (requstCode == 1) {
            time = (Date) val;
            items.get(1).setValue(val);
        }
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    public class InputFormItem {
        private String label;
        private Object val;

        private DialogFragment dialog;

        public InputFormItem(String label, Object val, DialogFragment dialogFragment) {
            this.label = label;
            this.val = val;
            this.dialog = dialogFragment;
        }

        public String getLabel() {
            return this.label;
        }

        public Object getValue() {
            return this.val;
        }

        public void setValue(Object val) {
            this.val = val;
        }

        public String toString() {
            if (val instanceof Date) {
                Date time = (Date) val;
                DateFormat df = new SimpleDateFormat("HH:mm");
                return df.format(time);
            }
            return val.toString();
        }

        public DialogFragment getDialog() {
            return this.dialog;
        }
    }
}
