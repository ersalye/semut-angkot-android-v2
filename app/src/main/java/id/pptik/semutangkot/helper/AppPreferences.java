package id.pptik.semutangkot.helper;

import android.content.Context;
import net.grandcentrix.tray.TrayPreferences;


public class AppPreferences extends TrayPreferences {

    public static String KEY_IS_LOGGED_IN = "key.is.logged.in";

    public AppPreferences(Context context) {
        super(context, "App Module", 1);
    }
}
