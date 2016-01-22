/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

  static SimpleDateFormat sdf = new SimpleDateFormat(API_FORMAT, Locale.US);

  public static final long MIN_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

  /**
   * @param dateTime ISO-8601 format date time string from the api
   * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
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
