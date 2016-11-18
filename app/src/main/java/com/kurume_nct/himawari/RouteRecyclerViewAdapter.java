package com.kurume_nct.himawari;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final ArrayList<StoreData> stores;
    private final ArrayList<WayTime> durations;

    public RouteRecyclerViewAdapter(Context context, ArrayList<StoreData> stores, ArrayList<WayTime> durations) {
        this.context = context;
        this.stores = stores;
        this.durations = durations;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.fragment_route_store, parent, false);
                return new StoreViewHolder(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.fragment_route, parent, false);
                return new DurationViewHolder(view);
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.fragment_route_point, parent, false);
                return new PointViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == getItemCount() - 1) {
            return 2;
        }
        return position % 2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((StoreViewHolder) holder).onBindItemViewHolder(stores.get(position / 2));
                break;
            case 1:
                ((DurationViewHolder) holder).onBindItemViewHolder(durations.get(position / 2));
                break;
            case 2:
                ((PointViewHolder) holder).onBindItemViewHolder(stores.get(position / 2));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return stores.size() + durations.size();
    }

    public class PointViewHolder extends RecyclerView.ViewHolder {
        private TextView pointName;

        public PointViewHolder(View view) {
            super(view);

            this.pointName = (TextView) view.findViewById(R.id.route_name);
        }

        public void onBindItemViewHolder(StoreData data) {
            pointName.setText(data.getStoreName());
        }
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView storeTime;
        private TextView storeName;
        private TextView storePrice;

        public StoreViewHolder(View view) {
            super(view);

            this.storeTime = (TextView) view.findViewById(R.id.store_time);
            this.storeName = (TextView) view.findViewById(R.id.store_name);
            this.storePrice = (TextView) view.findViewById(R.id.store_price);
        }

        public void onBindItemViewHolder(StoreData data) {
            storeTime.setText(String.valueOf(data.getStayedTime() / 60));
            storeName.setText(data.getStoreName());
            storePrice.setText(String.valueOf(data.getPrice()));
        }
    }

    public class DurationViewHolder extends RecyclerView.ViewHolder {
        private TextView routeTime;
        private TextView routeDistance;

        public DurationViewHolder(View view) {
            super(view);

            this.routeTime = (TextView) view.findViewById(R.id.route_time);
            this.routeDistance = (TextView) view.findViewById(R.id.route_distance);
        }

        public void onBindItemViewHolder(WayTime data) {
            routeTime.setText(String.valueOf(data.getTime() / 60));
            routeDistance.setText(String.valueOf(data.getDistance()));
        }
    }
}
