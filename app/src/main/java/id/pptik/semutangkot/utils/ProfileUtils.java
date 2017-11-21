package id.pptik.semutangkot.utils;

import android.content.Context;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;

import id.pptik.semutangkot.models.Profile;

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
}
