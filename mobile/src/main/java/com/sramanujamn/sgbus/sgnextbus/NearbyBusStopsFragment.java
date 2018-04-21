package com.sramanujamn.sgbus.sgnextbus;

import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sramanujamn.sgbus.sgnextbus.data.BusContract;
import com.sramanujamn.sgbus.sgnextbus.data.BusStopLocationData;

import java.util.ArrayList;

public class NearbyBusStopsFragment extends Fragment {

    private static final String TAG = NearbyBusStopsFragment.class.getSimpleName();

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationSettingsRequest.Builder builder;

    private LocationRequest locationRequest;

    //private TextView nearbyLocationDetails;

    private static final long LOCATION_REQUEST_INTERVAL = 10000;

    private static final long LOCATION_FASTEST_REQUEST_INTERVAL = 10000;

    public static final float MAX_DISTANCE = 1500; // 1500 meters

    private boolean requestingNetworkUpdates;

    private LocationCallback locationCallback;

    private Location mLocation;

    public static final String[] BUS_STOPS_PROJECTION =
            {
                    BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE,
                    BusContract.BusStopsEntry.COLUMN_DESCRIPTION,
                    BusContract.BusStopsEntry.COLUMN_LATITUDE,
                    BusContract.BusStopsEntry.COLUMN_LONGITUDE
            };

    public static final int INDEX_BUSSTOPCODE = 0;
    public static final int INDEX_DESCRIPTION = 1;
    public static final int INDEX_LATITUDE = 2;
    public static final int INDEX_LONGITUDE = 3;

    ArrayList<BusStopLocationData> busStops;

    private StringBuilder nearbyBusStops;

    private BusStopLocationData[] nearbyBusStopsList;

    private RecyclerView mRecyclerView;

    private NearbyBusStopsAdapter nearbyBusStopsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Getting Location!");
        View rootView = inflater.inflate(R.layout.rv_nearby_busstops_list, container, false);
        Context context = rootView.getContext();

        createLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        //nearbyLocationDetails = (TextView)rootView.findViewById(R.id.tv_nearby_stop);

        nearbyBusStops = new StringBuilder();


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) {
                    return;
                } else {
                    for(Location location : locationResult.getLocations()) {
                        Log.v(TAG, "Location got: " + location);
                    }
                }
            }
        };

        startLocationUpdates();

        try {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mLocation = location;
                            new FetchNearbyBusStopsTask().execute(location);

                            //nearbyLocationDetails.setText("Provider: " + location.getProvider() +
                            //        "Latitude/Longitude" + location.getLatitude() + "/" + location.getLongitude());
                            //Log.v(TAG, nearbyLocationDetails.getText().toString());
                        }
                    });
        } catch (SecurityException se) {
            Log.v(TAG, "Exception: " + se);
        }

        if(mLocation != null) {
            Log.v(TAG, "Location is not null!");
            new FetchNearbyBusStopsTask().execute(mLocation);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_busstops_list);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        nearbyBusStopsAdapter = new NearbyBusStopsAdapter();
        mRecyclerView.setAdapter(nearbyBusStopsAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);

        return rootView;
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_FASTEST_REQUEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this.getContext());

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.v(TAG, "Successfully added location request task.");
            }
        });

        task.addOnFailureListener(this.getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                        resolvableApiException.startResolutionForResult(NearbyBusStopsFragment.this.getActivity(), 1);
                    } catch(IntentSender.SendIntentException sie) {

                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(requestingNetworkUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            Log.v(TAG, "Started Location Updates!");
        } catch (SecurityException se) {
            Log.v(TAG, "Security exception: " + se);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.v(TAG, "Stopped Location Updates!");
    }

    public void getNearbyBusStops() {

    }

    public class FetchNearbyBusStopsTask extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... locations) {
            if(locations.length == 0) {
                return null;
            }

            Uri uri = BusContract.BusStopsEntry.CONTENT_URI;
            busStops = new ArrayList<BusStopLocationData>();

            Cursor cursor = getActivity().getContentResolver().query(uri, BUS_STOPS_PROJECTION, null, null, null);
            if(cursor.moveToFirst()) {
                do {
                    Location location = new Location("");
                    location.setLatitude(cursor.getDouble(INDEX_LATITUDE));
                    location.setLongitude(cursor.getDouble(INDEX_LONGITUDE));
                    BusStopLocationData busStopLocationData = new BusStopLocationData();
                    //Log.v(TAG, cursor.getString(INDEX_DESCRIPTION) + " (" + cursor.getString(INDEX_BUSSTOPCODE) + ")");
                    //Log.v(TAG, "Distance from my place: " + mLocation.distanceTo(location) + " meters.");
                    float distance = mLocation.distanceTo(location);
                    if(distance <= MAX_DISTANCE) {
                        busStopLocationData.setBusStopCode(cursor.getString(INDEX_BUSSTOPCODE));
                        busStopLocationData.setBusStopDescription(cursor.getString(INDEX_DESCRIPTION));
                        busStopLocationData.setDistance(distance);
                        //busStops.add(cursor.getString(INDEX_DESCRIPTION) + " (" + cursor.getString(INDEX_BUSSTOPCODE) + "). Distance: " + mLocation.distanceTo(location) + " meters.");
                        busStops.add(busStopLocationData);
                    }
                } while(cursor.moveToNext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //nearbyBusStops.append(nearbyLocationDetails.getText()).append('\n');
            /*nearbyBusStopsList = new BusStopLocationData[busStops.size()];
            for(int i = 0; i < busStops.size(); i++) {
                //Log.v(TAG, busStops.get(i));
                //nearbyBusStops.append(i).append("-> ").append(busStops.get(i)).append('\n');
                nearbyBusStopsList[i] = busStops.get(i);
            }
            if(nearbyBusStopsList.length > 0) {
                nearbyBusStopsAdapter.setNearbyBusStops(nearbyBusStopsList);
                nearbyBusStopsAdapter.notifyDataSetChanged();
            }*/

            nearbyBusStopsAdapter.addAll(busStops);
            mRecyclerView.setAdapter(nearbyBusStopsAdapter);
            //nearbyBusStopsAdapter.notifyDataSetChanged();
            //nearbyLocationDetails.setText(nearbyBusStops.toString());
        }
    }
}
