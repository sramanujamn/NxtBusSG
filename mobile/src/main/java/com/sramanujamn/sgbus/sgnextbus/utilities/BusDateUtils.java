package com.sramanujamn.sgbus.sgnextbus.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by raja on 3/27/2018.
 */

public class BusDateUtils {

    private static final String TAG = BusDateUtils.class.getSimpleName();

    private static final String NO_EST_AVAILABLE = "No Est. Available";

    public static String getRemainingTime(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        //Log.v(TAG, "Parsing Date: " + dateString);
        if(dateString != null && dateString.length() > 0) {
            try {

                Date date = formatter.parse(dateString);
                //Log.v(TAG, "" + date.getDate() + "-" + date.getMonth() + "-" + date.getYear() + "-" + date.getHours() + ":" + date.getMinutes());
                long seconds = (date.getTime() - System.currentTimeMillis())/1000;
                //Log.v(TAG, "seconds: " + seconds);
                int minutes = (int)(seconds/60);
                //Log.v(TAG, "Minutes Remaining: " + minutes);
                if(minutes > 1) {
                    return minutes + " mins";
                } else {
                    return "Arr";
                }

            } catch(ParseException pe) {
                Log.e(TAG, "Parse exception: " + pe.getMessage());
                return "Error";
            }
        } else {
            return NO_EST_AVAILABLE;
        }
    }
}
