package id.pptik.semutangkot.helper;

import android.content.Context;
import net.grandcentrix.tray.TrayPreferences;


public class AppPreferences extends TrayPreferences {

    public static String KEY_IS_LOGGED_IN = "key.is.logged.in";
    public static String KEY_IS_FIRST_LAUNCH = "key.is.firs.launch";
    public static String KEY_SHOW_ANGKOT = "key.show.angkot";
    public static String KEY_SHOW_LAPORAN = "key.show.laporan";
    public static String KEY_SHOW_CCTV = "key.show.cctv";
    public static String KEY_SHOW_JALUR = "key.show.jalur";

    public AppPreferences(Context context) {
        super(context, "App Module", 1);
    }
}
