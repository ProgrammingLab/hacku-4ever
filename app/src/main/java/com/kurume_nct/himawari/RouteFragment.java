package com.kurume_nct.himawari;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RouteFragment extends Fragment {
    public static final String ROUTE_KEY = "route_";
    private static final String STORES_KEY = "stores_";
    private static final String TIMES_KEY = "times_";

    public RouteFragment() {
    }

    @SuppressWarnings("unused")
    public static RouteFragment newInstance(Fragment target, int requestCode, ArrayList<StoreData> stores, ArrayList<WayTime> times) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STORES_KEY, stores);
        args.putParcelableArrayList(TIMES_KEY, times);
        fragment.setArguments(args);
        fragment.setTargetFragment(target, requestCode);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.way_toolbar);
        toolbar.setTitle(getString(R.string.route_title));

        Bundle args = getArguments();
        ArrayList<StoreData> stores = args.getParcelableArrayList(STORES_KEY);
        ArrayList<WayTime> times = args.getParcelableArrayList(TIMES_KEY);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.route_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RouteRecyclerViewAdapter(getContext(), stores, times));
        return view;
    }
}
