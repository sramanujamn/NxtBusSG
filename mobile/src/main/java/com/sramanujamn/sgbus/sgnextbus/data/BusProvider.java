package com.sramanujamn.sgbus.sgnextbus.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BusProvider extends ContentProvider {

    private static final String TAG = BusProvider.class.getSimpleName();

    public static final int CODE_ALL_BUSSTOPS = 300;
    public static final int CODE_BUSSTOPS_WITH_SEARCHSTRING = 303;

    public static final int CODE_ALL_BUSSERVICES = 500;
    public static final int CODE_BUSSERVICES_WITH_SEARCHSTRING = 503;

    public static final int CODE_ALL_BUSROUTES = 400;
    public static final int CODE_BUSROUTES_WITH_SEARCHSTRING = 403;

    private static final String PERCENTAGE = "%";

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private BusDBHelper busDBHelper;


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BusContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BusContract.PATH_BUSSTOPS, CODE_ALL_BUSSTOPS);

        matcher.addURI(authority, BusContract.PATH_BUSSTOPS + "/*", CODE_BUSSTOPS_WITH_SEARCHSTRING);

        matcher.addURI(authority, BusContract.PATH_BUSSERVICES, CODE_ALL_BUSSERVICES);

        matcher.addURI(authority, BusContract.PATH_BUSSERVICES + "/*", CODE_BUSSERVICES_WITH_SEARCHSTRING);

        matcher.addURI(authority, BusContract.PATH_BUSROUTES, CODE_ALL_BUSROUTES);

        matcher.addURI(authority, BusContract.PATH_BUSROUTES + "/*/*", CODE_BUSROUTES_WITH_SEARCHSTRING);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        busDBHelper = new BusDBHelper(getContext());
        Log.v(TAG, "Inside onCreate()");
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = busDBHelper.getWritableDatabase();
        int rowsInserted = 0;
        Log.v(TAG, "URI: " + uri);
        Log.v(TAG, "URI Matched: " + sUriMatcher.match(uri));

        switch(sUriMatcher.match(uri)) {
            case CODE_ALL_BUSSTOPS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(BusContract.BusStopsEntry.TABLE_NAME, null, value);
                        if(_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            /*case CODE_ALL_BUSSERVICES:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(BusContract.BusServicesEntry.TABLE_NAME, null, value);
                        if(_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;*/

            case CODE_ALL_BUSROUTES:
                Log.v(TAG, "INSERTING INTO BUS ROUTES!!!");
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(BusContract.BusRoutesEntry.TABLE_NAME, null, value);
                        if(_id != -1) {
                            Log.v(TAG, "INSERTED!!!");
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectorArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        String resultsLimit = "0, 5";
        String[] selectionArguments;
        String selectionCriteria;

        switch (match) {
            case CODE_BUSSTOPS_WITH_SEARCHSTRING:
                String busStopSearchString = PERCENTAGE + uri.getLastPathSegment() + PERCENTAGE;
                selectionArguments = new String[] {busStopSearchString, busStopSearchString};
                selectionCriteria = BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE + " LIKE ? OR " + BusContract.BusStopsEntry.COLUMN_DESCRIPTION + " LIKE ? ";
                Log.v(TAG, selectionCriteria);

                cursor = busDBHelper.getReadableDatabase().query(
                        BusContract.BusStopsEntry.TABLE_NAME,
                        projection,
                        selectionCriteria,
                        selectionArguments,
                        null,
                        null,
                        sortOrder, resultsLimit
                );
                break;

            case CODE_ALL_BUSSTOPS:
                cursor = busDBHelper.getReadableDatabase().query(
                        BusContract.BusStopsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectorArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_BUSSERVICES_WITH_SEARCHSTRING:
                String busServiceSearchString = uri.getLastPathSegment();
                selectionArguments = new String[] {busServiceSearchString};
                selectionCriteria = BusContract.BusServicesEntry.COLUMN_SERVICENO + " = ? ";
                Log.v(TAG, selectionCriteria);

                cursor = busDBHelper.getReadableDatabase().query(
                        BusContract.BusServicesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_BUSROUTES_WITH_SEARCHSTRING:
                //String busServiceNoString = getQueryParameter(uri, BusContract.BusRoutesEntry.COLUMN_SERVICENO);
                //String busStopCodeString = getQueryParameter(uri, BusContract.BusRoutesEntry.COLUMN_BUSSTOPCODE);
                //selectionArguments = new String[] {busServiceNoString, busStopCodeString};

                /*selectionCriteria = BusContract.BusRoutesEntry.COLUMN_SERVICENO + " = ? AND "
                        + BusContract.BusRoutesEntry.COLUMN_DIRECTION + " = (select direction from busroutes where " + BusContract.BusRoutesEntry.COLUMN_BUSSTOPCODE + " = ?)";

                cursor = busDBHelper.getReadableDatabase().query(
                        BusContract.BusRoutesEntry.TABLE_NAME,
                        projection,
                        selectionCriteria,
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                ); */

                String sqlQuery = "select route.serviceno as serviceno, route.busstopcode as busstopcode, route.distance as distance, " +
                        " stop.description as description, route.direction as direction, route.stopsequence as stopsequence " +
                        " from busroutes route inner join busstops stop on route.busstopcode = stop.busstopcode " +
                        " and serviceno = ? and direction = (select direction from busroutes where serviceno = route.serviceno and busstopcode = ?) " +
                        " order by route.direction, distance, route.stopsequence";

                cursor = busDBHelper.getReadableDatabase().rawQuery(
                        sqlQuery,
                        selectorArgs
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        busDBHelper.close();
        super.shutdown();
    }

    public String getQueryParameter(Uri uri, String key) {
        String value = uri.getQueryParameter(key);
        if(value != null && Build.VERSION.SDK_INT < 14) {
            value = value.replaceFirst("\\+", " ");
        }
        return value;
    }



}
