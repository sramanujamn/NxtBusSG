package com.sramanujamn.sgbus.sgnextbus;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;
import com.sramanujamn.sgbus.sgnextbus.data.BusDBHelper;
import com.sramanujamn.sgbus.sgnextbus.data.BusProvider;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusJsonUtils;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusNetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class BusArrivalListFragment extends Fragment {

    private static final String TAG = BusArrivalListFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private BusArrivalAdapter mBusArrivalAdapter;

    private Button mButton;

    BusArrivalData[] busArrivalDataList;

    private BusProvider busProvider;

    private BusDBHelper busDBHelper;

    private TextView titleView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        //Log.v(TAG, "INSIDE OnCreateView()!!!");
        Context context = rootView.getContext();
        busDBHelper = new BusDBHelper(context);

        titleView = (TextView)rootView.findViewById(R.id.tv_bus_stop_title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_bus_list);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        mBusArrivalAdapter = new BusArrivalAdapter();

        mRecyclerView.setAdapter(mBusArrivalAdapter);

        //handleIntent(getIntent());

        //Intent intent = getIntent();
        Bundle args = getArguments();
        if(args != null && args.getString(MainActivity.BUS_STOP_NAME) != null) {
            //Log.v(TAG, "BUSSTOPCODE has Values!!! Inside OnCreateView()!!!");
            String busStopName = args.getString(MainActivity.BUS_STOP_NAME);
            titleView.setText(busStopName);
            mBusArrivalAdapter.setBusStopCode(busStopName);
            //rootView.setTitle(intent.getStringExtra(MainActivity.BUS_STOP_NAME));
        } else {
            //Log.v(TAG, "Bus Stop Code is null!!!");
            //String busStopCode = "42119";
            //new BusArrivalListFragment.FetchBusStopArrivalsTask().execute(busStopCode);
        }

        if(args != null && args.getString(MainActivity.BUS_STOP_CODE) != null) {
            //Log.v(TAG, "BUSSTOPCODE has Values!!! Inside OnCreateView()!!!");
            String busStopCode = args.getString(MainActivity.BUS_STOP_CODE);
            new BusArrivalListFragment.FetchBusStopArrivalsTask().execute(busStopCode);
            //rootView.setTitle(intent.getStringExtra(MainActivity.BUS_STOP_NAME));
        } else {
            //Log.v(TAG, "Bus Stop Code is null!!!");
            //String busStopCode = "42119";
            //new BusArrivalListFragment.FetchBusStopArrivalsTask().execute(busStopCode);
        }

        mRecyclerView.setVisibility(View.VISIBLE);

        return rootView;
    }

    public class FetchBusStopArrivalsTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0) {
                return null;
            }

            String busStopCode = strings[0];
            URL busStopArrivalUrl = BusNetworkUtils.buildUrlFromBusStopCode(busStopCode);

            // Getting the busstops
            //URL busStopsUrl = BusNetworkUtils.buildUrlForBusStops();


            try {
                URL busRoutesUrl = BusNetworkUtils.buildUrlForBusServices(0);
                String jsonBusRoutesResponse = BusNetworkUtils.getResponseFromHttpUrl(busRoutesUrl);
                //Log.v(TAG, jsonBusRoutesResponse);
                // busProvider = new BusProvider();
                //int rowsInserted = getContentResolver().bulkInsert(BusContract.BusStopsEntry.CONTENT_URI, BusJsonUtils.getBusStopsFromJson(jsonBusStopsResponse));
                //Log.v(TAG, "Rows inserted: " + rowsInserted);

                String jsonApiResponse = BusNetworkUtils.getResponseFromHttpUrl(busStopArrivalUrl);
                //Log.v(TAG, jsonApiResponse);
                //Log.v(TAG, "Got JSON Response");
                busArrivalDataList = BusJsonUtils.getBusArrivalDatafromJson(jsonApiResponse);
                return jsonApiResponse;

            } catch (IOException ioe) {
                Log.e(TAG, "Error receiving response: " + busStopArrivalUrl);
                return null;
            } catch(JSONException jsone) {
                Log.e(TAG, "Error parsing JSON response: " + jsone.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if(busArrivalDataList != null) {
                //mRecyclerView.setAdapter(mBusArrivalAdapter);
                mBusArrivalAdapter.setBusArrivalDataList(busArrivalDataList);
                mBusArrivalAdapter.notifyDataSetChanged();
                //Log.v(TAG, "Item count: " + mBusArrivalAdapter.getItemCount());
            }
        }
    }
}
