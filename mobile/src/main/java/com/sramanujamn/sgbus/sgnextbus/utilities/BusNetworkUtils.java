package com.sramanujamn.sgbus.sgnextbus.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by raja on 3/26/2018.
 * These utilities will be used to communicate with LTA DataMall API
 */

public class BusNetworkUtils {

    private static final String TAG = BusNetworkUtils.class.getSimpleName();

    private static final String QUERY_PARAM = "?";

    private static final String SKIP = "$skip";

    /**
     * BUS ARRIVALS
     */

    private static final String BUS_ARRIVAL_DATAMALL_API_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";

    private static final String BUS_STOP_CODE_PARAM = "BusStopCode";

    private static final String BUS_SERVICE_PARAM = "ServiceNo";

    /**
     * BUS STOPS
     */
    private static final String BUS_STOPS_DATAMALL_API_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusStops";

    /**
     * BUS SERVICES
     */
    private static final String BUS_SERVICES_DATAMALL_API_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusServices";

    /**
     * BUS ROUTES
     */
    private static final String BUS_ROUTES_DATAMALL_API_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusRoutes";

    /**
     * Build the URL for Bus Arrival based on the Bus Stop Code
     *
     * @param busStopCode
     * @return URL - the built URL
     */
    public static URL buildUrlFromBusStopCode(String busStopCode) {
        Uri builtUri = Uri.parse(BUS_ARRIVAL_DATAMALL_API_BASE_URL).buildUpon()
                .appendQueryParameter(BUS_STOP_CODE_PARAM, busStopCode)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch(MalformedURLException murle) {
            Log.e(TAG, "Could not connect to URL: " + url + murle.getMessage());
        }

        //Log.v(TAG, "Build URL: " + url);

        return url;
    }

    /**
     * Build the URL for the Bus Stops
     *
     * @param skip
     * @return
     */
    public static URL buildUrlForBusStops(int skip) {

        Uri builtUri;

        if(skip > 0) {
            builtUri = Uri.parse(BUS_STOPS_DATAMALL_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SKIP, String.valueOf(skip))
                    .build();
        } else {
            builtUri = Uri.parse(BUS_STOPS_DATAMALL_API_BASE_URL).buildUpon()
                    .build();
        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException murle) {
            Log.e(TAG, "Could not connect to URL: " + url + murle.getMessage());
        }
        return url;
    }

    public static URL buildUrlForBusServices(int skip) {

        Uri builtUri;

        if(skip > 0) {
            builtUri = Uri.parse(BUS_SERVICES_DATAMALL_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SKIP, String.valueOf(skip))
                    .build();
        } else {
            builtUri = Uri.parse(BUS_SERVICES_DATAMALL_API_BASE_URL).buildUpon()
                    .build();
        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException murle) {
            Log.e(TAG, "Could not connect to URL: " + url + murle.getMessage());
        }
        return url;
    }


    /**
     * Build the URL for the Bus Routes
     *
     * @param skip
     * @return
     */
    public static URL buildUrlForBusRoutes(int skip) {

        Uri builtUri;

        if(skip > 0) {
            builtUri = Uri.parse(BUS_ROUTES_DATAMALL_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SKIP, String.valueOf(skip))
                    .build();
        } else {
            builtUri = Uri.parse(BUS_ROUTES_DATAMALL_API_BASE_URL).buildUpon()
                    .build();
        }

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException murle) {
            Log.e(TAG, "Could not connect to URL: " + url + murle.getMessage());
        }
        return url;
    }

    /**
     * Helper function to get the JSON string response from the URL
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // always remove line before committing to github
        urlConnection.setRequestProperty("AccountKey", "UPTIRnJKR8SSXd5Bwo3YSQ===s");
        urlConnection.setRequestProperty("accept", "application/json");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
