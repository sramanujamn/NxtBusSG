package com.sramanujamn.sgbus.sgnextbus.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BusContract {

    public static final String CONTENT_AUTHORITY = "com.sramanujamn.sgbus.sgnextbus";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_BUSSTOPS = "busstops";

    public static final String PATH_BUSSERVICES = "busservices";

    public static final String PATH_BUSROUTES = "busroutes";

    /**
     * Table for Bus Stops
     */
    public static final class BusStopsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BUSSTOPS)
                .build();

        public static final String TABLE_NAME = "busstops";

        public static final String COLUMN_BUSSTOPCODE = "busstopcode";
        public static final String COLUMN_ROADNAME = "roadname";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static Uri buildBusStopsUri(String busStop) {
             return CONTENT_URI.buildUpon()
                     .appendPath(busStop)
                     .build();
        }
    }

    /**
     * Table for Bus Services
     */
    public static final class BusServicesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BUSSERVICES)
                .build();

        public static final String TABLE_NAME = "busservices";

        public static final String COLUMN_SERVICENO = "serviceno";
        public static final String COLUMN_OPERATOR = "operator";
        public static final String COLUMN_DIRECTION = "direction";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_ORIGINCODE = "origincode";
        public static final String COLUMN_DESTINATIONCODE = "destinationcode";
        public static final String COLUMN_AM_PEAK_FREQ = "am_peak_freq";
        public static final String COLUMN_AM_OFFPEAK_FREQ = "am_offpeak_freq";
        public static final String COLUMN_PM_PEAK_FREQ = "pm_peak_freq";
        public static final String COLUMN_PM_OFFPEAK_FREQ = "pm_offpeak_freq";
        public static final String COLUMN_LOOPDESC = "loopdesc";
    }

    public static final class BusRoutesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BUSROUTES)
                .build();

        public static final String TABLE_NAME = "busroutes";

        public static final String COLUMN_SERVICENO = "serviceno";
        public static final String COLUMN_OPERATOR = "operator";
        public static final String COLUMN_DIRECTION = "direction";
        public static final String COLUMN_STOPSEQUENCE = "stopsequence";
        public static final String COLUMN_BUSSTOPCODE = "busstopcode";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_WD_FIRSTBUS = "wd_firstbus";
        public static final String COLUMN_WD_LASTBUS = "wd_lastbus";
        public static final String COLUMN_SAT_FIRSTBUS = "sat_firstbus";
        public static final String COLUMN_SAT_LASTBUS = "sat_lastbus";
        public static final String COLUMN_SUN_FIRSTBUS = "sun_firstbus";
        public static final String COLUMN_SUN_LASTBUS = "sun_lastbus";

        public static Uri buildBusRouteSearchUri(String busServiceNo, String busStopCode) {
            return CONTENT_URI.buildUpon()
                    .appendPath(busServiceNo)
                    .appendPath(busStopCode)
                    .build();
        }

    }
}
