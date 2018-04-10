package com.sramanujamn.sgbus.sgnextbus;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;

/**
 * Created by raja on 3/27/2018.
 */

public class BusArrivalAdapter extends RecyclerView.Adapter<BusArrivalAdapter.BusArrivalAdapterViewHolder> {

    private BusArrivalData[] busArrivalDataList;

    public BusArrivalAdapter() {

    }

    public class BusArrivalAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mServiceNoTextView;
        public final TextView mEstimatedArrivalTextView;
        public final TextView mLoadTextView;
        public final ImageView mWheelchairImageView;

        public BusArrivalAdapterViewHolder(View view) {
            super(view);
            mServiceNoTextView = (TextView)view.findViewById(R.id.tv_bus_number);
            mEstimatedArrivalTextView = (TextView)view.findViewById(R.id.tv_arrival_time);
            mLoadTextView = (TextView)view.findViewById(R.id.tv_seats_available);
            mWheelchairImageView = (ImageView)view.findViewById(R.id.iv_wab);
        }

    }

    @Override
    public BusArrivalAdapter.BusArrivalAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.bus_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BusArrivalAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusArrivalAdapter.BusArrivalAdapterViewHolder holder, int position) {
        BusArrivalData busArrivalData = busArrivalDataList[position];
        holder.mServiceNoTextView.setText(busArrivalData.getServiceNo());
        holder.mEstimatedArrivalTextView.setText(busArrivalData.getEstimatedArrival());

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
}
