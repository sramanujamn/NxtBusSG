package com.sramanujamn.sgbus.sgnextbus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sramanujamn.sgbus.sgnextbus.data.BusStopLocationData;

import java.util.ArrayList;

public class NearbyBusStopsAdapter extends RecyclerView.Adapter<NearbyBusStopsAdapter.NearbyBusStopAdapterViewHolder> {

    //private BusStopLocationData[] nearbyBusStops;

    private static final String DISTANCE_MEASURE = " meters";

    SortedList<BusStopLocationData> sortedNearbyBusStopsList;

    BusFragmentPagerAdapter busFragmentPagerAdapter;

    public NearbyBusStopsAdapter() {
        sortedNearbyBusStopsList = new SortedList<BusStopLocationData>(BusStopLocationData.class, new SortedList.Callback<BusStopLocationData>() {
            @Override
            public int compare(BusStopLocationData o1, BusStopLocationData o2) {
                return Float.compare(o1.getDistance(), o2.getDistance());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(BusStopLocationData oldItem, BusStopLocationData newItem) {
                return oldItem.getDistance() == newItem.getDistance();
            }

            @Override
            public boolean areItemsTheSame(BusStopLocationData item1, BusStopLocationData item2) {
                return item1.getDistance() == item2.getDistance();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });

    }

    public class NearbyBusStopAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mBusStopTextView;
        public final TextView mDistanceTextView;

        public NearbyBusStopAdapterViewHolder(View view) {
            super(view);
            mBusStopTextView = (TextView)view.findViewById(R.id.tv_bus_stop);
            mDistanceTextView = (TextView)view.findViewById(R.id.tv_distance);
        }
    }

    @Override
    public NearbyBusStopsAdapter.NearbyBusStopAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragments_nearby_busstops;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new NearbyBusStopAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final NearbyBusStopsAdapter.NearbyBusStopAdapterViewHolder holder, int position) {
        //BusStopLocationData busStopLocationData = nearbyBusStops[position];
        final BusStopLocationData busStopLocationData = sortedNearbyBusStopsList.get(position);
        holder.mBusStopTextView.setText(busStopLocationData.getBusStopDescription() + " (" + busStopLocationData.getBusStopCode() + ")");
        holder.mDistanceTextView.setText(busStopLocationData.getDistance() + DISTANCE_MEASURE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.getContext().
                MainActivity mainActivity = (MainActivity)view.getContext();
                mainActivity.loadBusArrivalList(holder.mBusStopTextView.getText().toString(), busStopLocationData.getBusStopCode());

                //BusListFragment busListFragment = new BusListFragment();
                //Bundle args = new Bundle();
                //args.putString(MainActivity.BUS_STOP_CODE, busStopLocationData.getBusStopCode());
                //busListFragment.setArguments(args);

                //FragmentTransaction fragmentTransaction = busListFragment.getFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.frame_buslist, busListFragment);
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        //if(nearbyBusStops == null) return 0;
        return sortedNearbyBusStopsList.size();
    }

    public void addAll(ArrayList<BusStopLocationData> busStopLocationDataArrayList) {
        //this.nearbyBusStops = nearbyBusStops;
        sortedNearbyBusStopsList.beginBatchedUpdates();
        for(int i = 0; i < busStopLocationDataArrayList.size(); i++) {
            sortedNearbyBusStopsList.add(busStopLocationDataArrayList.get(i));
        }
        sortedNearbyBusStopsList.endBatchedUpdates();
        //notifyDataSetChanged();
    }
}
