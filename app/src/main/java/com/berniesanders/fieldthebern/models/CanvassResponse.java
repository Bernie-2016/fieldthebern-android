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

package com.berniesanders.fieldthebern.models;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CanvassResponse {

  // list of accepted constants
  // 'Unknown'|'Asked to leave'|'Strongly for'|'Leaning for'|
  // 'Undecided'|'Leaning against'|'Strongly against'
  @StringDef({
                 UNKNOWN, ASKED_TO_LEAVE, STRONGLY_FOR, LEANING_FOR, UNDECIDED, LEANING_AGAINST,
                 STRONGLY_AGAINST, NO_ONE_HOME
             })

  @Retention(RetentionPolicy.SOURCE)

  //Declare the annotation
  public @interface Response {
  }

  public static final String UNKNOWN = "unknown";
  public static final String ASKED_TO_LEAVE = "asked_to_leave";
  public static final String STRONGLY_FOR = "strongly_for";
  public static final String LEANING_FOR = "leaning_for";
  public static final String UNDECIDED = "undecided";
  public static final String LEANING_AGAINST = "leaning_against";
  public static final String STRONGLY_AGAINST = "strongly_against";
  public static final String NO_ONE_HOME = "not_home";
}
