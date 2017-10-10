package id.pptik.semutangkot.helper;


import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {


    public static String getTimeAgo(String dateInput) {
        String timeformat = dateInput;
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(dateInput);
            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            timeformat = String.valueOf(relavetime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeformat;
    }
}