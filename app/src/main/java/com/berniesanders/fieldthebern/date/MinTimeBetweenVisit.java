package com.berniesanders.fieldthebern.date;

import android.app.AlarmManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MinTimeBetweenVisit {

    public static final String API_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    static SimpleDateFormat sdf  = new SimpleDateFormat(API_FORMAT, Locale.US);

    public static final long MIN_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;


    /**
     * @param dateTime ISO-8601 format date time string from the api
     *                 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     *
     * @return if the min time has elapsed since 'dateTime'
     * @see #MIN_TIME
     */
    public static boolean elapsed(String dateTime) throws ParseException {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(dateTime);

        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()
                > date.getTime() + MIN_TIME;
    }
}
