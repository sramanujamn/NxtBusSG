package com.sramanujamn.sgbus.sgnextbus;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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

    //private BusSearchAutoCompleteView.SearchAutoComplete searchAutoComplete;

    public static final String BUS_STOP_CODE = "BusStopCode";

    public static final String BUS_STOP_NAME = "BusStopName";

    public static final String[] BUS_STOPS_PROJECTION = {
            BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE,
            BusContract.BusStopsEntry.COLUMN_DESCRIPTION
    };

    public static final int INDEX_BUS_STOP_CODE = 0;

    public static final int INDEX_BUS_STOP_DESCRIPTION = 1;

    private String busStopCode;

    private BusFragmentPagerAdapter busFragmentPagerAdapter;
    private ViewPager busViewPager;
    private TabLayout tabLayout;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = new Bundle();

        busFragmentPagerAdapter = new BusFragmentPagerAdapter(getSupportFragmentManager(), bundle);
        busViewPager = (ViewPager)findViewById(R.id.vp_bus);
        busViewPager.setAdapter(busFragmentPagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout_bus);
        tabLayout.setupWithViewPager(busViewPager);

        if(!new BusDBHelper(getBaseContext()).hasTableInDB(BusContract.BusStopsEntry.TABLE_NAME)) {
            new FetchBusStopsTask().execute("Test");
        } else {
            Log.v(TAG, "BUSSTOPS Table already exists.");
        }

        if(!new BusDBHelper(getBaseContext()).hasTableInDB(BusContract.BusRoutesEntry.TABLE_NAME)) {
            new FetchBusRoutesTask().execute("Test");
        } else {
            Log.v(TAG, "BUSRoutes Table already exists.");
        }
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
                //busStops.add( cursor.getString(INDEX_BUS_STOP_DESCRIPTION) + " " + cursor.getString(INDEX_BUS_STOP_CODE));
            } while(cursor.moveToNext());
        }
        for(int i = 0; i < busStops.size(); i++) {
            Log.v(TAG, busStops.get(i));
        }
        busStopsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, busStops);
        //searchView.setSuggestionsAdapter(busStopsArrayAdapter);
        //searchAutoComplete.setFilters(new InputFilter[] {});
        //searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchAutoComplete.setAdapter(busStopsArrayAdapter);
        Log.v(TAG, "ArrayAdapter Count: " + busStopsArrayAdapter.getCount());
        cursor.close();
        busStopsArrayAdapter.notifyDataSetChanged();
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


    public class FetchBusRoutesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            int skip = 0;
            int rowsInserted = 0;
            URL busRoutesUrl = null;

            do {
                try {
                    busRoutesUrl = BusNetworkUtils.buildUrlForBusRoutes(skip);
                    Log.v(TAG, busRoutesUrl.toString());
                    String jsonApiResponse = BusNetworkUtils.getResponseFromHttpUrl(busRoutesUrl);
                    Log.v(TAG, jsonApiResponse);

                    rowsInserted = getContentResolver().bulkInsert(BusContract.BusRoutesEntry.CONTENT_URI, BusJsonUtils.getBusRoutesFromJson(jsonApiResponse));
                    Log.v(TAG, "Rows inserted: " + rowsInserted);
                    skip += rowsInserted;
                } catch (IOException ioe) {
                    Log.e(TAG, "Error receiving response: " + busRoutesUrl);
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
            Log.v(TAG, "Reached onPostExecute of Bus Routes");
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
        searchView.setQueryRefinementEnabled(false);

        //Log.v(TAG, getComponentName().toString());
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, BusListActivity.class)));
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoComplete = (BusSearchAutoCompleteView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        searchAutoComplete.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light_normal_background));

        /*InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                return null;
            }
        };*/

        //searchAutoComplete.setFilters(new InputFilter[] {filter});

        //searchView.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC | InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        //searchAutoComplete.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                Log.v(TAG, "Reached here!!!");
                String query = (String)adapterView.getItemAtPosition(itemIndex);
                Log.v(TAG, "User Query: " + query);
                String busStopCode = getBusStopCode(query);

                //Bundle args = new Bundle();
                bundle.clear();
                bundle.putString(MainActivity.BUS_STOP_CODE, busStopCode);
                bundle.putString(MainActivity.BUS_STOP_NAME, query);
                BusArrivalListFragment busArrivalListFragment = (BusArrivalListFragment) busFragmentPagerAdapter.getItem(BusFragmentPagerAdapter.INDEX_BUS_LIST);
                //BusArrivalListFragment busArrivalListFragment = (BusArrivalListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_buslist);
                //busArrivalListFragment.refreshData(bundle);
                //busFragmentPagerAdapter.
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.remove(busArrivalListFragment);
                //busArrivalListFragment = new BusArrivalListFragment();
                busArrivalListFragment.setArguments(bundle);
                Log.v(TAG, "SET THE BUNDLE!!!");
                fragmentTransaction.replace(R.id.frame_buslist, busArrivalListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                busViewPager.setCurrentItem(BusFragmentPagerAdapter.INDEX_BUS_LIST);
                //busFragmentPagerAdapter.notifyDataSetChanged();
                searchAutoComplete.setText("" + query);
            }
        });

        //searchView.setInputType(InputType.TYPE_CLASS_TEXT);

        //searchAutoComplete.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.v(TAG, "Query Text: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Log.v(TAG, "Query changed...");
                MainActivity.this.loadAutoCompleteSuggestions(query);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v(TAG, "Reached here!");
            }
        });

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        //Log.v(TAG, "Reached here!");
        //super.onNewIntent(intent);
    }

    public void loadBusArrivalList(String busStopName, String busStopCode) {
        bundle.clear();
        bundle.putString(MainActivity.BUS_STOP_CODE, busStopCode);
        bundle.putString(MainActivity.BUS_STOP_NAME, busStopName);
        BusArrivalListFragment busArrivalListFragment = (BusArrivalListFragment) busFragmentPagerAdapter.getItem(BusFragmentPagerAdapter.INDEX_BUS_LIST);
        //BusArrivalListFragment busArrivalListFragment = (BusArrivalListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_buslist);
        //busArrivalListFragment.refreshData(bundle);
        //busFragmentPagerAdapter.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.remove(busArrivalListFragment);
        //busArrivalListFragment = new BusArrivalListFragment();
        busArrivalListFragment.setArguments(bundle);
        Log.v(TAG, "SET THE BUNDLE!!!");
        fragmentTransaction.replace(R.id.frame_buslist, busArrivalListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        busViewPager.setCurrentItem(BusFragmentPagerAdapter.INDEX_BUS_LIST);
    }
}
