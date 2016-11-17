package com.kurume_nct.himawari;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kurume_nct.himawari.dummy.DummyContent.DummyTimeData;

import java.util.List;

public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final List<StoreData> stores;
    private final List<DummyTimeData> durations;

    public RouteRecyclerViewAdapter(Context context, List<StoreData> stores, List<DummyTimeData> durations) {
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
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
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
        }
    }

    @Override
    public int getItemCount() {
        return stores.size() + durations.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName;
        private TextView storePrice;

        public StoreViewHolder(View view) {
            super(view);

            this.storeName = (TextView) view.findViewById(R.id.store_name);
            this.storePrice = (TextView) view.findViewById(R.id.store_price);
        }

        public void onBindItemViewHolder(StoreData data) {
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

        public void onBindItemViewHolder(DummyTimeData data) {
            routeTime.setText(data.getDuration());
            routeDistance.setText(data.getDistance());
        }
    }
}
