package com.sramanujamn.sgbus.sgnextbus;


import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
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

    private boolean isExpanded;

    private BusArrivalData[] busArrivalDataList;

    public BusArrivalAdapter() {

    }

    public class BusArrivalAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mServiceNoTextView;
        public final TextView mEstimatedArrivalTextView;
        public final TextView mLoadTextView;
        public final ImageView mWheelchairImageView;
        public final TextView mEstimatedArrivalSecondBusTextView;
        public final TextView getmEstimatedArrivalThirdBusTextView;
        private ConstraintLayout busExpandedLayout;


        public BusArrivalAdapterViewHolder(View view) {
            super(view);
            mServiceNoTextView = (TextView)view.findViewById(R.id.tv_bus_number);
            mEstimatedArrivalTextView = (TextView)view.findViewById(R.id.tv_arrival_time);
            mLoadTextView = (TextView)view.findViewById(R.id.tv_seats_available);
            mWheelchairImageView = (ImageView)view.findViewById(R.id.iv_wab);


            mEstimatedArrivalSecondBusTextView = (TextView)view.findViewById(R.id.tv_next_bus2_arrival_time2);
            getmEstimatedArrivalThirdBusTextView = (TextView)view.findViewById(R.id.tv_next_bus3_arrival_time);
            isExpanded = false;
            busExpandedLayout = (ConstraintLayout)view.findViewById(R.id.vw_bus_expanded);
            busExpandedLayout.setVisibility(View.GONE);
            //int baseline = dividerView.getBaseline();
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
        holder.getmEstimatedArrivalThirdBusTextView.setText(busArrivalData.getEstimatedArrivalThirdBus());

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
                if(holder.busExpandedLayout.getVisibility() == View.GONE) {
                    //holder.mEstimatedArrivalSecondBusTextView.setVisibility(View.VISIBLE);
                    //holder.dividerView.setVisibility(View.VISIBLE);
                    if(Build.VERSION.SDK_INT >= 19) {
                        TransitionManager.beginDelayedTransition(holder.busExpandedLayout);
                    }
                    holder.busExpandedLayout.setVisibility(View.VISIBLE);
                    isExpanded = true;
                } else {
                    holder.busExpandedLayout.setVisibility(View.GONE);
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
}
