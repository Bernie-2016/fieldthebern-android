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

import com.google.gson.annotations.SerializedName;

/**
 * {
 * id: <score_id>
 * type: 'scores',
 * attributes: {
 * points_for_updates: <points_for_updates>,
 * points_for_knock: <points_for_knock>
 * },
 * relationships: {
 * visit: {
 * data: { id: <visit_id>, type: 'visits' }
 * }
 * }
 * }
 */
public class Score {
  long id;
  String type;
  Attributes attributes = new Attributes();
  Relationships relationships = new Relationships();

  public long id() {
    return this.id;
  }

  public String type() {
    return this.type;
  }

  public Attributes attributes() {
    return this.attributes;
  }

  public Relationships relationships() {
    return this.relationships;
  }

  public Score id(final long id) {
    this.id = id;
    return this;
  }

  public Score type(final String type) {
    this.type = type;
    return this;
  }

  public Score attributes(final Attributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public Score relationships(final Relationships relationships) {
    this.relationships = relationships;
    return this;
  }

  public static class Attributes {

    @SerializedName("points_for_updates")
    int pointsForUpdates;

    @SerializedName("points_for_knock")
    int pointsForKnock;

    public int pointsForUpdates() {
      return this.pointsForUpdates;
    }

    public int pointsForKnock() {
      return this.pointsForKnock;
    }

    public Attributes pointsForUpdates(final int pointsForUpdates) {
      this.pointsForUpdates = pointsForUpdates;
      return this;
    }

    public Attributes pointsForKnock(final int pointsForKnock) {
      this.pointsForKnock = pointsForKnock;
      return this;
    }
  }

  public static class Relationships {
    Visit visit;

    public Visit visit() {
      return this.visit;
    }

    public Relationships visit(final Visit visit) {
      this.visit = visit;
      return this;
    }
  }
}
