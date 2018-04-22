package com.sramanujamn.sgbus.sgnextbus.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BusDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nextbusg.db";

    private static final int DATABASE_VERSION = 2;

    public BusDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         * Create the BUSSTOPS table. This will hold the data of bus stops which we would be storing in SQLite DB.
         */
        final String SQL_CREATE_BUSSTOPS_TABLE = "CREATE TABLE " + BusContract.BusStopsEntry.TABLE_NAME
                + "("
                + BusContract.BusStopsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE + " TEXT NOT NULL, "
                + BusContract.BusStopsEntry.COLUMN_ROADNAME + " TEXT NOT NULL, "
                + BusContract.BusStopsEntry.COLUMN_DESCRIPTION + " TEXT, "
                + BusContract.BusStopsEntry.COLUMN_LATITUDE + " REAL NOT NULL, "
                + BusContract.BusStopsEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "
                + " UNIQUE (" + BusContract.BusStopsEntry.COLUMN_BUSSTOPCODE + ") ON CONFLICT REPLACE"
                + ")";

        final String SQL_CREATE_BUSROUTES_TABLE = "CREATE TABLE " + BusContract.BusRoutesEntry.TABLE_NAME
                + "("
                + BusContract.BusRoutesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BusContract.BusRoutesEntry.COLUMN_SERVICENO + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_OPERATOR + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_DIRECTION + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_STOPSEQUENCE + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_BUSSTOPCODE + " INTEGER NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_DISTANCE + " REAL NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_WD_FIRSTBUS + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_WD_LASTBUS + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_SAT_FIRSTBUS + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_SAT_LASTBUS + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_SUN_FIRSTBUS + " TEXT NOT NULL, "
                + BusContract.BusRoutesEntry.COLUMN_SUN_LASTBUS + " TEXT NOT NULL "
                + " ON CONFLICT REPLACE"
                + ")";

        //sqLiteDatabase.execSQL(SQL_CREATE_BUSSTOPS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_BUSROUTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BusContract.BusStopsEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BusContract.BusRoutesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean hasTableInDB(String tableName) {
        Cursor cursor = this.getReadableDatabase().query(
                tableName, null, null, null, null, null, null, "5"
        );

        if(cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        return false;
    }

}
