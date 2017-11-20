package id.pptik.semutangkot.utils;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.helper.AppPreferences;


public class Utils {

    public static boolean requestingLocationUpdates(Context context) {
        return new AppPreferences(context)
                .getBoolean(AppPreferences.KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        new AppPreferences(context).put(AppPreferences.KEY_REQUESTING_LOCATION_UPDATES,
                requestingLocationUpdates);
    }

    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return DateFormat.getDateTimeInstance().format(new Date());
    }
}