package com.sramanujamn.sgbus.sgnextbus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sramanujamn.sgbus.sgnextbus.data.BusRouteData;

public class BusRoutesAdapter extends RecyclerView.Adapter<BusRoutesAdapter.BusRoutesAdapterViewHolder> {

    public static final String TAG = BusRoutesAdapter.class.getSimpleName();

    private BusRouteData[] busRouteDataList;

    @Override
    public BusRoutesAdapter.BusRoutesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.bus_list_expanded;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BusRoutesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusRoutesAdapter.BusRoutesAdapterViewHolder holder, int position) {
        BusRouteData busRouteData = busRouteDataList[position];
        holder.busStopView.setText(busRouteData.getBusStopCode());
        holder.distanceView.setText(busRouteData.getDistance());
        //Log.v(TAG, "BusStopView Text set to: " + busRouteData.getBusStopCode());
    }

    @Override
    public int getItemCount() {
        if(busRouteDataList == null) return 0;
        return busRouteDataList.length;
    }

    public class BusRoutesAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView busStopView;
        public final TextView distanceView;

        public BusRoutesAdapterViewHolder(View view) {
            super(view);
            busStopView = (TextView)view.findViewById(R.id.tv_route_bus_stop);
            distanceView = (TextView)view.findViewById(R.id.tv_route_distance);
            //Log.v(TAG, "BusStopView TextView initialized");
        }
    }

    public void setBusRouteDataList(BusRouteData[] busRouteDataList) {
        this.busRouteDataList = busRouteDataList;
        notifyDataSetChanged();
    }
}
