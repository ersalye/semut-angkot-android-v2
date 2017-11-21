package id.pptik.semutangkot.utils;


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

}
