package com.sramanujamn.sgbus.sgnextbus.utilities;

import android.content.ContentValues;
import android.util.Log;

import com.sramanujamn.sgbus.sgnextbus.data.BusArrivalData;
import com.sramanujamn.sgbus.sgnextbus.data.BusContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raja on 3/27/2018.
 */

public class BusJsonUtils {
    public BusArrivalData[] busArrivalData;

    private static final String TAG = BusJsonUtils.class.getSimpleName();


    /**
     * Common
     */
    private static final String SERVICE_NO = "ServiceNo";


    /**
     * BUS ARRIVALS
     */
    private static final String SERVICES_LIST = "Services";

    private static final String NEXT_BUS = "NextBus";

    private static final String ESTIMATED_ARRIVAL = "EstimatedArrival";

    private static final String LOAD = "Load";

    private static final String FEATURE = "Feature";

    private static final String WHEELCHAIR_ACCESSIBLE = "WAB";

    private static final String NEXT_BUS_2 = "NextBus2";

    private static final String NEXT_BUS_3 = "NextBus3";


    /**
     * BUS STOPS
     */
    private static final String LIST = "value";

    private static final String BUS_STOP_CODE = "BusStopCode";

    private static final String ROAD_NAME = "RoadName";

    private static final String BUS_STOP_DESCRIPTION = "Description";

    private static final String BUS_STOP_LATITUDE = "Latitude";

    private static final String BUS_STOP_LONGITUDE = "Longitude";


    /**
     * BUS SERVICES
     */
    private static final String OPERATOR = "Operator";

    private static final String DIRECTION = "Direction";

    private static final String CATEGORY = "Category";

    private static final String ORIGIN_CODE = "OriginCode";

    private static final String DESTINATION_CODE = "DestinationCode";

    private static final String AM_PEAK_FREQ = "AM_Peak_Freq";

    private static final String AM_OFFPEAK_FREQ = "AM_Offpeak_Freq";

    private static final String PM_PEAK_FREQ = "PM_Peak_Freq";

    private static final String PM_OFFPEAK_FREQ = "PM_Offpeak_Freq";

    private static final String LOOPDESC = "LoopDesc";


    /**
     * BUS ROUTES
     */
    private static final String STOP_SEQUENCE = "StopSequence";

    private static final String DISTANCE = "Distance";

    private static final String WD_FIRST_BUS = "WD_FirstBus";

    private static final String WD_LAST_BUS = "WD_LastBus";

    private static final String SAT_FIRST_BUS = "SAT_FirstBus";

    private static final String SAT_LAST_BUS = "SAT_LastBus";

    private static final String SUN_FIRST_BUS = "SUN_FirstBus";

    private static final String SUN_LAST_BUS = "SUN_LastBus";



    public static BusArrivalData[] getBusArrivalDatafromJson(String jsonString) throws JSONException {

        BusArrivalData[] busArrivalDataList = null;

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray busArrivalsArr = jsonObject.getJSONArray(SERVICES_LIST);

        busArrivalDataList = new BusArrivalData[busArrivalsArr.length()];

        for(int i = 0; i < busArrivalsArr.length(); i++) {
            JSONObject busArrival = busArrivalsArr.getJSONObject(i);
            BusArrivalData busArrivalData = new BusArrivalData();

            busArrivalData.setServiceNo(busArrival.getString(SERVICE_NO));
            //busArrivalDataList[i].setServiceNo(busArrival.getString(SERVICE_NO));
            Log.v(TAG, "Service No: " + busArrival.getString(SERVICE_NO));

            // ADD Details of First Bus
            JSONObject nextBus = busArrival.getJSONObject(NEXT_BUS);
            //busArrivalData.setEstimatedArrival(nextBus.getString(ESTIMATED_ARRIVAL));
            busArrivalData.setEstimatedArrival(BusDateUtils.getRemainingTime(nextBus.getString(ESTIMATED_ARRIVAL)));
            //Log.v(TAG, "Arrival: " + BusDateUtils.getRemainingTime(nextBus.getString(ESTIMATED_ARRIVAL)));
            busArrivalData.setLoad(nextBus.getString(LOAD));
            if(nextBus.getString(FEATURE).equals(WHEELCHAIR_ACCESSIBLE)) {
                busArrivalData.setWheelChairAccessible(true);
            }

            // ADD Details of Second Bus
            JSONObject nextBus2 = busArrival.getJSONObject(NEXT_BUS_2);
            busArrivalData.setEstimatedArrivalSecondBus(BusDateUtils.getRemainingTime(nextBus2.getString(ESTIMATED_ARRIVAL)));
            busArrivalData.setLoadSecondBus(nextBus2.getString(LOAD));
            if(nextBus2.getString(FEATURE).equals(WHEELCHAIR_ACCESSIBLE)) {
                busArrivalData.setWheelChairAccessibleSecondBus(true);
            }


            // ADD Details of Third Bus
            JSONObject nextBus3 = busArrival.getJSONObject(NEXT_BUS_3);
            busArrivalData.setEstimatedArrivalThirdBus(BusDateUtils.getRemainingTime(nextBus3.getString(ESTIMATED_ARRIVAL)));
            busArrivalData.setLoadThirdBus(nextBus3.getString(LOAD));
            if(nextBus3.getString(FEATURE).equals(WHEELCHAIR_ACCESSIBLE)) {
                busArrivalData.setWheelChairAccessibleThirdBus(true);
            }

            busArrivalDataList[i] = busArrivalData;
        }

        return busArrivalDataList;
    }

    public static ContentValues[] getBusStopsFromJson(String jsonString) throws JSONException {

        ContentValues[] contentValues = null;

        JSONObject jsonRootObject = new JSONObject(jsonString);

        JSONArray jsonBusStopsArray = jsonRootObject.getJSONArray(LIST);

        contentValues = new ContentValues[jsonBusStopsArray.length()];

        for(int i = 0; i < contentValues.length; i++) {
            JSONObject busStop = jsonBusStopsArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE, busStop.getString(BUS_STOP_CODE));
            cv.put(BusContract.BusStopsEntry.COLUMN_ROADNAME, busStop.getString(ROAD_NAME));
            cv.put(BusContract.BusStopsEntry.COLUMN_DESCRIPTION, busStop.getString(BUS_STOP_DESCRIPTION));
            cv.put(BusContract.BusStopsEntry.COLUMN_LATITUDE, busStop.getDouble(BUS_STOP_LATITUDE));
            cv.put(BusContract.BusStopsEntry.COLUMN_LONGITUDE, busStop.getDouble(BUS_STOP_LONGITUDE));
            contentValues[i] = cv;
        }

        return contentValues;
    }

    public static ContentValues[] getBusServicesFromJson(String jsonString) throws JSONException {
        ContentValues[] contentValues = null;

        JSONObject jsonRootObject = new JSONObject(jsonString);

        JSONArray jsonBusServicesArray = jsonRootObject.getJSONArray(LIST);

        contentValues = new ContentValues[jsonBusServicesArray.length()];

        for(int i = 0; i < contentValues.length; i++) {
            JSONObject busService = jsonBusServicesArray.getJSONObject(i);
            ContentValues cv = new ContentValues();
            cv.put(BusContract.BusServicesEntry.COLUMN_SERVICENO, busService.getString(SERVICE_NO));
            cv.put(BusContract.BusServicesEntry.COLUMN_OPERATOR, busService.getString(OPERATOR));
            cv.put(BusContract.BusServicesEntry.COLUMN_DIRECTION, busService.getString(DIRECTION));
            cv.put(BusContract.BusServicesEntry.COLUMN_CATEGORY, busService.getString(CATEGORY));
            cv.put(BusContract.BusServicesEntry.COLUMN_ORIGINCODE, busService.getString(ORIGIN_CODE));
            cv.put(BusContract.BusServicesEntry.COLUMN_DESTINATIONCODE, busService.getString(DESTINATION_CODE));
            cv.put(BusContract.BusServicesEntry.COLUMN_AM_PEAK_FREQ, busService.getString(AM_PEAK_FREQ));
            cv.put(BusContract.BusServicesEntry.COLUMN_AM_OFFPEAK_FREQ, busService.getString(AM_OFFPEAK_FREQ));
            cv.put(BusContract.BusServicesEntry.COLUMN_PM_PEAK_FREQ, busService.getString(PM_PEAK_FREQ));
            cv.put(BusContract.BusServicesEntry.COLUMN_PM_OFFPEAK_FREQ, busService.getString(PM_OFFPEAK_FREQ));
            cv.put(BusContract.BusServicesEntry.COLUMN_LOOPDESC, busService.getString(LOOPDESC));
            contentValues[i] = cv;
        }

        return contentValues;
    }

    public static ContentValues[] getBusRoutesFromJson(String jsonString) throws JSONException {
        ContentValues[] contentValues = null;

        JSONObject jsonRootObject = new JSONObject(jsonString);

        JSONArray jsonBusRoutesArray = jsonRootObject.getJSONArray(LIST);

        contentValues = new ContentValues[jsonBusRoutesArray.length()];


        for(int i = 0; i < contentValues.length; i++) {
            JSONObject busRoute = jsonBusRoutesArray.getJSONObject(i);
            //Log.v(TAG, busRoute.toString());
            ContentValues cv = new ContentValues();
            cv.put(BusContract.BusRoutesEntry.COLUMN_SERVICENO, busRoute.getString(SERVICE_NO));
            cv.put(BusContract.BusRoutesEntry.COLUMN_OPERATOR, busRoute.getString(OPERATOR));
            cv.put(BusContract.BusRoutesEntry.COLUMN_DIRECTION, busRoute.getString(DIRECTION));
            cv.put(BusContract.BusRoutesEntry.COLUMN_STOPSEQUENCE, busRoute.getString(STOP_SEQUENCE));
            cv.put(BusContract.BusRoutesEntry.COLUMN_BUSSTOPCODE, busRoute.getString(BUS_STOP_CODE));
            cv.put(BusContract.BusRoutesEntry.COLUMN_DISTANCE, busRoute.getString(DISTANCE));
            cv.put(BusContract.BusRoutesEntry.COLUMN_WD_FIRSTBUS, busRoute.getString(WD_FIRST_BUS));
            cv.put(BusContract.BusRoutesEntry.COLUMN_WD_LASTBUS, busRoute.getString(WD_LAST_BUS));
            cv.put(BusContract.BusRoutesEntry.COLUMN_SAT_FIRSTBUS, busRoute.getString(SAT_FIRST_BUS));
            cv.put(BusContract.BusRoutesEntry.COLUMN_SAT_LASTBUS, busRoute.getString(SAT_LAST_BUS));
            cv.put(BusContract.BusRoutesEntry.COLUMN_SUN_FIRSTBUS, busRoute.getString(SUN_FIRST_BUS));
            cv.put(BusContract.BusRoutesEntry.COLUMN_SUN_LASTBUS, busRoute.getString(SUN_LAST_BUS));

            contentValues[i] = cv;
        }

        return contentValues;
    }
}
