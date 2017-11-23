package id.pptik.semutangkot.utils;

import android.content.Context;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;
import com.google.gson.Gson;

import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;

public class ProfileUtils {

    public static Profile getProfile(Context context){
        Profile p = null;
        try {
            p = (Profile) new GSONSharedPreferences(context).getObject(new Profile());
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static RequestStatus getReqStatus(String response){
        return new Gson().fromJson(response, RequestStatus.class);
    }


    public static String getDistance(Context context, double latDes, double lonDes){
        String s = "";

        AppPreferences preferences = new AppPreferences(context);
        s = ""+NumUtils.round(new NumUtils().distance(
                preferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0),
                preferences.getFloat(AppPreferences.KEY_MY_LONGITUDE, 0),
                latDes,
                lonDes, "K"
        ),2);

        return s;
    }
}
