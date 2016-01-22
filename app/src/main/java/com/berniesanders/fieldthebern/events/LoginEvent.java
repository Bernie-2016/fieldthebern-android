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

package com.berniesanders.fieldthebern.events;

import android.support.annotation.StringDef;
import com.berniesanders.fieldthebern.models.User;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoginEvent {

  public static final String LOGIN = "LoginEvent.LOGIN";
  public static final String LOGOUT = "LoginEvent.LOGOUT";

  private final String eventType;
  private final User user;

  @StringDef({ LOGIN, LOGOUT })

  @Retention(RetentionPolicy.SOURCE)
  public @interface EventType {
  }

  public LoginEvent(@EventType String eventType, User user) {
    this.eventType = eventType;

    this.user = user;
  }

  @EventType
  public String getEventType() {
    return eventType;
  }

  public User getUser() {
    return user;
  }
}
