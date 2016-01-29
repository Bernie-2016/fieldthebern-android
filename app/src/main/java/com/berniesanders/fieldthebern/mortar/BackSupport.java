/*
 * Copyright 2014 Square Inc.
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
 */

package com.berniesanders.fieldthebern.mortar;

import android.view.View;
import com.crashlytics.android.Crashlytics;
import flow.Flow;
import timber.log.Timber;

/**
 * Support for {@link HandlesBack}.
 */
public class BackSupport {

  public static boolean onBackPressed(View childView) {

    try {
      if (childView instanceof HandlesBack) {
        if (((HandlesBack) childView).onBackPressed()) {
          return true;
        }
      }
      return Flow.get(childView).goBack();
    } catch (Exception e) {
      Timber.e(e, "error going back for: "+childView);
      Crashlytics.logException(e);
      return false;
    }
  }

  private BackSupport() {
  }
}
