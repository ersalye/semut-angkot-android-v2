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
    public static String KEY_STORE_ANGKOT_PATH = "key.store.angkot.path";

    public static String KEY_MY_LATITUDE = "key.my.latitude";
    public static String KEY_MY_LONGITUDE = "key.my.longitude";

    public static final String ACTION_BROADCAST_LOCATION = "action.broadcast.location";
    public static final String INTENT_BROADCAST_TYPE = "intent.broadcast.type";
    public static final String INTENT_BROADCAST_MSG = "intent.broadcast.msg";

    public static final String BROADCAST_TYPE_LOCATION = "broadcast.type.location";

    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    public AppPreferences(Context context) {
        super(context, "App Module", 1);
    }
}
