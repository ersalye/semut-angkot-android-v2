package id.pptik.semutangkot.utils;


import android.util.Log;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static id.pptik.semutangkot.helper.TimeHelper.getTimeAgo;

public class NumUtils {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public static String convertMongoDate(String val){
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = df.parse(val);
            String finalStr = outputFormat.format(d);
            val = finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static String convertMongoDateToAgo(String val){
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = df.parse(val);
            String finalStr = outputFormat.format(d);
            val = getTimeAgo(finalStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }


    public static String convertMongoDateToAgo7(String val){
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final long millisToAdd = 28_800_000; //7 hours
            Date d = df.parse(val);
            d.setTime(d.getTime() - millisToAdd);
            String finalStr = outputFormat.format(d);

            val = finalStr;
            //Log.i("7", finalStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }


    public double distance(double lat1, double lon1, double lat2, double lon2, String sr) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (sr.equals("K")) {
            dist = dist * 1.609344;
        } else if (sr.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
