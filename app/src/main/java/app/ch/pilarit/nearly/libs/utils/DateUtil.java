package app.ch.pilarit.nearly.libs.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ch_pilarit on 4/17/15 AD.
 */
public class DateUtil {

    public static boolean isBetweenTime(int from, int to){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        boolean isBetween = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
        return isBetween;
    }

    public static int timeToInt(String time){
        String[] hourMin = time.split(":");
        int hour = Integer.valueOf(hourMin[0]);
        int min = Integer.valueOf(hourMin[1]);
        int timeInt = hour * 100 + min;
        return timeInt;
    }
}
