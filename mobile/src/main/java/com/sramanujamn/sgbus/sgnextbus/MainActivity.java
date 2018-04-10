package com.sramanujamn.sgbus.sgnextbus;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.widget.SearchView;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;
import com.sramanujamn.sgbus.sgnextbus.data.BusContract;
import com.sramanujamn.sgbus.sgnextbus.data.BusDBHelper;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusJsonUtils;
import com.sramanujamn.sgbus.sgnextbus.utilities.BusNetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private BusArrivalAdapter mBusArrivalAdapter;

    private Button mButton;

    BusArrivalData[] busArrivalDataList;

    private BusAutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<String> busStopsArrayAdapter;

    private SearchView searchView;

    private SearchView.SearchAutoComplete searchAutoComplete;

    public static final String BUS_STOP_CODE = "BusStopCode";

    public static final String BUS_STOP_NAME = "BusStopName";

    public static final String[] BUS_STOPS_PROJECTION = {
            BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE,
            BusContract.BusStopsEntry.COLUMN_DESCRIPTION
    };

    public static final int INDEX_BUS_STOP_CODE = 0;

    public static final int INDEX_BUS_STOP_DESCRIPTION = 1;

    private String busStopCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_bus_list);

        //mRecyclerView.setHasFixedSize(true);

        //mRecyclerView.setLayoutManager(layoutManager);

        //mBusArrivalAdapter = new BusArrivalAdapter();

        //mRecyclerView.setAdapter(mBusArrivalAdapter);

        //mRecyclerView.setVisibility(View.VISIBLE);

        //mButton = (Button)findViewById(R.id.btn_search);

        //mButton.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View view) {
                //testButton(view);
            //}
        //});

        autoCompleteTextView = (BusAutoCompleteTextView)findViewById(R.id.tv_autoCompletBusStops);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {
                MainActivity.this.loadAutoCompleteText(userInput.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        busStopsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new String[] {""});
        autoCompleteTextView.setAdapter(busStopsArrayAdapter);

        //BusDBHelper busDBHelper = new BusDBHelper(getApplicationContext());
        //SQLiteDatabase db = busDBHelper.getReadableDatabase();
        if(!new BusDBHelper(getBaseContext()).hasTableInDB(BusContract.BusStopsEntry.TABLE_NAME)) {
            new FetchBusStopsTask().execute("Test");
        }
    }

    public void loadAutoCompleteText(String userInput) {
        Log.v(TAG, "User input: " + userInput);
        if(userInput == null || userInput.length() == 0) {
            return;
        }
        Uri uri = BusContract.BusStopsEntry.buildBusStopsUri(userInput);
        ArrayList<String> busStops = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(uri, BUS_STOPS_PROJECTION, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                busStops.add( cursor.getString(INDEX_BUS_STOP_DESCRIPTION) + " (" + cursor.getString(INDEX_BUS_STOP_CODE) + ")");
            } while(cursor.moveToNext());
        }
        busStopsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, busStops);
        autoCompleteTextView.setAdapter(busStopsArrayAdapter);
        //Log.v(TAG, busStopsArrayAdapter.getItem(0));
        busStopsArrayAdapter.notifyDataSetChanged();
    }

    public void loadAutoCompleteSuggestions(String userInput) {
        Log.v(TAG, "User input: " + userInput);
        if(userInput == null || userInput.length() == 0) {
            return;
        }
        Uri uri = BusContract.BusStopsEntry.buildBusStopsUri(userInput);
        ArrayList<String> busStops = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(uri, BUS_STOPS_PROJECTION, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                busStops.add( cursor.getString(INDEX_BUS_STOP_DESCRIPTION) + " (" + cursor.getString(INDEX_BUS_STOP_CODE) + ")");
            } while(cursor.moveToNext());
        }
        busStopsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, busStops);
        //searchView.setSuggestionsAdapter(busStopsArrayAdapter);
        searchAutoComplete.setAdapter(busStopsArrayAdapter);
        //Log.v(TAG, busStopsArrayAdapter.getItem(0));
        busStopsArrayAdapter.notifyDataSetChanged();
    }

    public void testButton(View view) {
        //EditText editText = (EditText)findViewById(R.id.et_search);
        BusAutoCompleteTextView busStopTextView = (BusAutoCompleteTextView)findViewById(R.id.tv_autoCompletBusStops);
        //String busStopCode = editText.getText().toString();
        String busStopCode = getBusStopCode(busStopTextView.getText().toString());

        //new FetchBusStopArrivalsTask().execute(busStopCode);

        Intent intent = new Intent(this, BusListActivity.class);
        intent.putExtra(BUS_STOP_CODE, busStopCode);
        intent.putExtra(BUS_STOP_NAME, busStopTextView.getText().toString());
        startActivity(intent);
    }

    public String getBusStopCode(String string) {
        int start = string.lastIndexOf("(") + 1;
        int end = string.lastIndexOf(")");
        //Log.v(TAG, "Substring: " + start + " " + end);
        return string.substring(start, end);
    }

    public class FetchBusStopsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            int skip = 0;
            int rowsInserted = 0;
            URL busStopsUrl = null;

            do {
                try {
                    busStopsUrl = BusNetworkUtils.buildUrlForBusStops(skip);
                    Log.v(TAG, busStopsUrl.toString());
                    String jsonApiResponse = BusNetworkUtils.getResponseFromHttpUrl(busStopsUrl);

                    rowsInserted = getContentResolver().bulkInsert(BusContract.BusStopsEntry.CONTENT_URI, BusJsonUtils.getBusStopsFromJson(jsonApiResponse));
                    Log.v(TAG, "Rows inserted: " + rowsInserted);
                    skip += rowsInserted;
                } catch (IOException ioe) {
                    Log.e(TAG, "Error receiving response: " + busStopsUrl);
                    return null;
                } catch(JSONException jsone) {
                    Log.e(TAG, "Error parsing JSON response: " + jsone.getMessage());
                    return null;
                }

            } while(rowsInserted > 0);

            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            //if(busArrivalDataList != null) {
                Log.v(TAG, "Reached onPostExecute");
                //mBusArrivalAdapter.setBusArrivalDataList(busArrivalDataList);
            //}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate the searchable configuration with the Search View
        //SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.search).getActionView();

        Log.v(TAG, getComponentName().toString());
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, BusListActivity.class)));
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_normal_background));
        /*searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {
                MainActivity.this.loadAutoCompleteSuggestions(userInput.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                Log.v(TAG, "Reached here!!!");
                String query = (String)adapterView.getItemAtPosition(itemIndex);
                Log.v(TAG, "User Query: " + query);
                String busStopCode = getBusStopCode(query);

                //new FetchBusStopArrivalsTask().execute(busStopCode);

                Intent intent = new Intent(MainActivity.this, BusListActivity.class);
                intent.putExtra(BUS_STOP_CODE, busStopCode);
                intent.putExtra(BUS_STOP_NAME, query);
                startActivity(intent);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(TAG, "Query Text: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.v(TAG, "Query changed...");
                MainActivity.this.loadAutoCompleteSuggestions(query);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Reached here!");
            }
        });

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        Log.v(TAG, "Reached here!");
        //super.onNewIntent(intent);
    }
}
