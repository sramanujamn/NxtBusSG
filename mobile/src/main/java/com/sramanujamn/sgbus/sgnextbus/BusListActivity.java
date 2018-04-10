package com.sramanujamn.sgbus.sgnextbus;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;
import com.sramanujamn.sgbus.sgnextbus.data.BusDBHelper;
import com.sramanujamn.sgbus.sgnextbus.data.BusProvider;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusJsonUtils;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusNetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by raja on 3/27/2018.
 */

public class BusListActivity extends AppCompatActivity {

    private static final String TAG = BusListActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private BusArrivalAdapter mBusArrivalAdapter;

    private Button mButton;

    BusArrivalData[] busArrivalDataList;

    private BusProvider busProvider;

    private BusDBHelper busDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        busDBHelper = new BusDBHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_bus_list);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        mBusArrivalAdapter = new BusArrivalAdapter();

        mRecyclerView.setAdapter(mBusArrivalAdapter);

        //handleIntent(getIntent());

        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.BUS_STOP_CODE)) {
            String busStopCode = intent.getStringExtra(MainActivity.BUS_STOP_CODE);
            new FetchBusStopArrivalsTask().execute(busStopCode);
            this.setTitle(intent.getStringExtra(MainActivity.BUS_STOP_NAME));
        }

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleIntent(Intent intent) {
        Log.v(TAG, "Reached here!");
        if(intent.getAction().equals(Intent.ACTION_SEARCH)) {
            Log.v(TAG, "Matched Intent!");
            String busStopCode = intent.getStringExtra(SearchManager.QUERY);
            new FetchBusStopArrivalsTask().execute(busStopCode);
            this.setTitle(intent.getStringExtra(MainActivity.BUS_STOP_NAME));
        }
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
                Log.v(TAG, jsonBusRoutesResponse);
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
                //Log.v(TAG, "Reached onPostExecute" );
                mBusArrivalAdapter.setBusArrivalDataList(busArrivalDataList);
                //Log.v(TAG, "Item count: " + mBusArrivalAdapter.getItemCount());
            }
        }
    }

}
