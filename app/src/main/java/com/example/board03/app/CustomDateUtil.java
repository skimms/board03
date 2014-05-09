package com.example.board03.app;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by gkimms on 2014-04-30.
 */
public final class  CustomDateUtil {
    private static Calendar cal;

    public static String unixtimeToDatetime(int integer) {
        cal  = Calendar.getInstance();
        String DateTime = "";
        long tmp_long = Long.parseLong(String.valueOf(integer)) * 1000;
        cal.setTimeInMillis(tmp_long);
        int year      = cal.get(Calendar.YEAR);
        int month     = cal.get(Calendar.MONTH) + 1;
        int date      = cal.get(Calendar.DATE);
        int hour      = cal.get(Calendar.HOUR_OF_DAY);
        int minute    = cal.get(Calendar.MINUTE);
        int second    = cal.get(Calendar.SECOND);

        String monthStr  = String.valueOf(month);
        String dateStr   = String.valueOf(date);
        String hourStr   = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        String secondStr = String.valueOf(second);

        if(monthStr.length() < 2){
            monthStr = "0" + monthStr;
        }
        dateStr    = CustomStrUtil.padLeft(dateStr, 2).replace(' ', '0');
        hourStr    = CustomStrUtil.padLeft(hourStr, 2).replace(' ', '0');
        minuteStr  = CustomStrUtil.padLeft(minuteStr, 2).replace(' ', '0');
        secondStr  = CustomStrUtil.padLeft(secondStr, 2).replace(' ', '0');

        DateTime = year +"-" + monthStr +"-"+dateStr + " " + hourStr + ":" + minuteStr + ":" + secondStr;
        return DateTime;
    }
}
