package com.sramanujamn.sgbus.sgnextbus;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;
import com.sramanujamn.sgbus.sgnextbus.data.BusContract;
import com.sramanujamn.sgbus.sgnextbus.data.BusRouteData;

import java.util.ArrayList;

/**
 * Created by raja on 3/27/2018.
 */

public class BusArrivalAdapter extends RecyclerView.Adapter<BusArrivalAdapter.BusArrivalAdapterViewHolder> {

    public static final String TAG = BusArrivalAdapter.class.getSimpleName();

    private boolean isExpanded;

    private BusArrivalData[] busArrivalDataList;

    private static final int INDEX_SERVICENO = 0;
    private static final int INDEX_BUSSTOPCODE = 1;
    private static final int INDEX_DISTANCE = 2;
    private static final int INDEX_DESCRIPTION = 3;
    private static final int INDEX_DIRECTION = 4;
    private static final int INDEX_STOPSEQUENCE = 5;

    private RecyclerView busRouteRecyclerView;

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    private String busStopCode;

    public BusArrivalAdapter() {

    }

    public BusArrivalAdapter(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public class BusArrivalAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mServiceNoTextView;
        public final TextView mEstimatedArrivalTextView;
        public final TextView mLoadTextView;
        public final ImageView mWheelchairImageView;
        public final TextView mEstimatedArrivalSecondBusTextView;
        public final TextView mEstimatedArrivalThirdBusTextView;
        //private ConstraintLayout busExpandedLayout;
        private RecyclerView busRouteRecyclerView;


        public BusArrivalAdapterViewHolder(View view) {
            super(view);
            mServiceNoTextView = (TextView)view.findViewById(R.id.tv_bus_number);
            mEstimatedArrivalTextView = (TextView)view.findViewById(R.id.tv_arrival_time);
            mLoadTextView = (TextView)view.findViewById(R.id.tv_seats_available);
            mWheelchairImageView = (ImageView)view.findViewById(R.id.iv_wab);


            mEstimatedArrivalSecondBusTextView = (TextView)view.findViewById(R.id.tv_next_bus2_arrival_time);
            mEstimatedArrivalThirdBusTextView = (TextView)view.findViewById(R.id.tv_next_bus3_arrival_time);
            isExpanded = false;
            //busExpandedLayout = (ConstraintLayout)view.findViewById(R.id.vw_bus_expanded);
            busRouteRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_nested_busroutes_list);
            //busExpandedLayout.setVisibility(View.GONE);
            busRouteRecyclerView.setVisibility(View.GONE);
            //int baseline = dividerView.getBaseline();

            //Context context = view.getContext();
            //LayoutInflater inflater = LayoutInflater.from(context);
            //boolean shouldAttachToParentImmediately = false;
            //View rootView = holder.itemView.getRootView();
            //View rootView = holder.busExpandedLayout.getRootView();
            //View rootView = inflater.inflate(R.layout.rv_busroutes_list, (ViewGroup) view.getParent(), shouldAttachToParentImmediately);
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            //busRouteRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_busroutes_list);
            //RecyclerView mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_busroutes_list);

            busRouteRecyclerView.setHasFixedSize(true);

            busRouteRecyclerView.setLayoutManager(layoutManager);

            //BusRoutesAdapter busRoutesAdapter = new BusRoutesAdapter();
            //busRoutesAdapter.setBusRouteDataList(busRouteData);

            //busRouteRecyclerView.setAdapter(busRoutesAdapter);

            //busRouteRecyclerView.setVisibility(View.VISIBLE);
            busRouteRecyclerView.setNestedScrollingEnabled(true);
            //busRoutesAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public BusArrivalAdapter.BusArrivalAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_busarrival_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BusArrivalAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BusArrivalAdapter.BusArrivalAdapterViewHolder holder, int position) {
        BusArrivalData busArrivalData = busArrivalDataList[position];
        holder.mServiceNoTextView.setText(busArrivalData.getServiceNo());
        holder.mEstimatedArrivalTextView.setText(busArrivalData.getEstimatedArrival());

        holder.mEstimatedArrivalSecondBusTextView.setText(busArrivalData.getEstimatedArrivalSecondBus());
        holder.mEstimatedArrivalThirdBusTextView.setText(busArrivalData.getEstimatedArrivalThirdBus());

        switch(busArrivalData.getLoad()) {
            case BusArrivalData.SEATS_AVAILABLE:
                holder.mLoadTextView.setText(R.string.available_seats);
                holder.mLoadTextView.setBackgroundColor(holder.mLoadTextView.getResources().getColor(R.color.colorGreen));
                break;
            case BusArrivalData.STANDING_AVAILABLE:
                holder.mLoadTextView.setText(R.string.available_standing);
                holder.mLoadTextView.setBackgroundColor(holder.mLoadTextView.getResources().getColor(R.color.colorAmber));
                break;
            case BusArrivalData.LIMITED_STANDING:
                holder.mLoadTextView.setText(R.string.available_limited_standing);
                holder.mLoadTextView.setBackgroundColor(holder.mLoadTextView.getResources().getColor(R.color.colorRed));
                break;
            default:
                break;
        }

        if(busArrivalData.isWheelChairAccessible()) {
            holder.mWheelchairImageView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.busRouteRecyclerView.getVisibility() == View.GONE) {
                    //holder.mEstimatedArrivalSecondBusTextView.setVisibility(View.VISIBLE);
                    //holder.dividerView.setVisibility(View.VISIBLE);
                    if(Build.VERSION.SDK_INT >= 19) {
                        TransitionManager.beginDelayedTransition(holder.busRouteRecyclerView);
                    }
                    holder.busRouteRecyclerView.setVisibility(View.VISIBLE);
                    isExpanded = true;
                    //TextView titleView = (TextView)holder.itemView.findViewById(R.id.tv_bus_stop_title);
                    setupBusRoute(holder, holder.mServiceNoTextView.getText().toString(), busStopCode);
                } else {
                    holder.busRouteRecyclerView.setVisibility(View.GONE);
                    isExpanded = false;
                }
            }
        });

        if(position == (getItemCount()-1)) {
            holder.itemView.setPadding(0, 0, 0 , 480);
        } else {
            holder.itemView.setPadding(0,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        if(busArrivalDataList == null) return 0;
        return busArrivalDataList.length;
    }

    public void setBusArrivalDataList(BusArrivalData[] busArrivalDataList) {
        this.busArrivalDataList = busArrivalDataList;
        notifyDataSetChanged();
    }

    public void setupBusRoute(BusArrivalAdapter.BusArrivalAdapterViewHolder holder, String busServiceNo, String busStopCode) {
        Log.v(TAG, "Bus Service No.: " + busServiceNo + ", Bus Stop Code: " + busStopCode);
        MainActivity mainActivity = (MainActivity)holder.itemView.getContext();
        Uri uri = BusContract.BusRoutesEntry.buildBusRouteSearchUri(busServiceNo, mainActivity.getBusStopCode(busStopCode));
        Log.v(TAG, "Uri: " + uri.toString());
        String[] selectionArgs = new String[] {busServiceNo, mainActivity.getBusStopCode(busStopCode)};
        Cursor cursor = mainActivity.getContentResolver().query(uri, null, null, selectionArgs, null);
        ArrayList<BusRouteData> busRouteDataList = new ArrayList<BusRouteData>();
        if(cursor.moveToFirst()) {
            do {
                BusRouteData busRouteData = new BusRouteData();
                busRouteData.setBusStopCode(cursor.getString(INDEX_DESCRIPTION));
                busRouteData.setDistance(cursor.getString(INDEX_DISTANCE));
                busRouteDataList.add(busRouteData);
            } while(cursor.moveToNext());
        }
        cursor.close();
        BusRouteData[] busRouteData = new BusRouteData[busRouteDataList.size()];
        for(int i = 0; i < busRouteDataList.size(); i++) {
            busRouteData[i] = busRouteDataList.get(i);
            Log.v(TAG, "Bus Stops: " + busRouteData[i].getBusStopCode());
        }

        /*Context context = holder.itemView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //View rootView = holder.itemView.getRootView();
        //View rootView = holder.busExpandedLayout.getRootView();
        View rootView = inflater.inflate(R.layout.rv_busroutes_list, (ViewGroup) holder.itemView.getParent(), shouldAttachToParentImmediately);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_busroutes_list);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        BusRoutesAdapter busRoutesAdapter = new BusRoutesAdapter();
        busRoutesAdapter.setBusRouteDataList(busRouteData);

        mRecyclerView.setAdapter(busRoutesAdapter);

        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setNestedScrollingEnabled(true);
        busRoutesAdapter.notifyDataSetChanged();
        */

        BusRoutesAdapter busRoutesAdapter = new BusRoutesAdapter();
        busRoutesAdapter.setBusRouteDataList(busRouteData);
        holder.busRouteRecyclerView.setAdapter(busRoutesAdapter);
        busRoutesAdapter.notifyDataSetChanged();
    }
}
