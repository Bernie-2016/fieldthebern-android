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

package com.berniesanders.fieldthebern.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.berniesanders.fieldthebern.R;
import com.google.common.base.Joiner;
import java.util.LinkedList;

/**
 * Central place for configuring data access urls
 */
public class ConfigImpl implements Config {
  //includes json for the whole site, not used currently
  //public static final String FULL_JSON_URL = "http://feelthebern.org/ftb-json/";

  private final String BASE_URL;
  private final String CANVASS_URL;

  private final String CLIENT_ID;
  private final String CLIENT_SECRET;
  private final String USER_AGENT;

  public ConfigImpl(Context context) {
    BASE_URL = context.getString(R.string.baseUrl);
    CANVASS_URL = context.getString(R.string.canvassUrl);
    CLIENT_ID = context.getString(R.string.oauth2ClientId);
    CLIENT_SECRET = context.getString(R.string.oauth2ClientSecret);

    String name = "FieldTheBern";
    String version = "dev";
    try {
      PackageInfo packageInfo =
          context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      version = packageInfo.versionName + "-b" + packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      // do nothing
    }

    LinkedList<String> comments = new LinkedList<>();
    if (Build.VERSION.RELEASE != null) {
      comments.add("Android " + Build.VERSION.RELEASE);
    }
    if (Build.MODEL != null) {
      comments.add(Build.MODEL);
    }

    String nameAndVersion = name + "/" + version;

    String comment = Joiner.on("; ").join(comments);
    if (comment.length() > 0) {
      comment = " (" + comment + ")";
    }

    USER_AGENT = nameAndVersion + comment;
  }

  @Override
  public String getFeelTheBernUrl() {
    return BASE_URL;
  }

  @Override
  public String getCanvassUrl() {
    return CANVASS_URL;
  }

  @Override
  public String getClientId() {
    return CLIENT_ID;
  }

  @Override
  public String getClientSecret() {
    return CLIENT_SECRET;
  }

  @Override
  public String getUserAgent() {
    return USER_AGENT;
  }
}
