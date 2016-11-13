package com.kurume_nct.himawari;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InputListViewAdapter extends ArrayAdapter<InputFormFragment.InputFormItem> {
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<InputFormFragment.InputFormItem> items;

    public InputListViewAdapter(Context context, int res, ArrayList<InputFormFragment.InputFormItem> objects) {
        super(context, res, objects);
        inflater = LayoutInflater.from(context);
        items = objects;
        resource = res;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.input_label);
        TextView val = (TextView) convertView.findViewById(R.id.input_value);

        InputFormFragment.InputFormItem item = items.get(position);

        label.setText(item.getLabel());
        val.setText(item.getValue());

        return convertView;
    }
}
