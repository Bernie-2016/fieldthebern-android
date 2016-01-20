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

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class Visit {

    Data data = new Data();

    /**
     * Mix of ApiAddress and Person objects
     */
    List<CanvassData> included;

    public List<CanvassData> included() {
        if (included == null) {
            included = new ArrayList<>();
        }
        return this.included;
    }

    public Attributes attributes() {
        return data.attributes;
    }

    public Visit attributes(final Attributes attributes) {
        data.attributes = attributes;
        return this;
    }

    public Visit included(final List<CanvassData> included) {
        this.included = included;
        return this;
    }


    public void start() {
        data.attributes.startTime = System.currentTimeMillis();
    }

    public void stop() {
        data.attributes.finishTime = System.currentTimeMillis();
        data.attributes.duration((int) ((data.attributes.finishTime - data.attributes.startTime)/1000));
    }


    public static class Data {
        Attributes attributes = new Attributes();
    }

    public static class Attributes {

        /**
         * Milliseconds since epoch
         */
        transient long startTime;

        /**
         * Milliseconds since epoch
         */
        transient long finishTime;

        /**
         * Visit duration in seconds
         */
        @SerializedName("duration_sec")
        int duration = 0;

        public int duration() {
            return this.duration;
        }

        public Attributes duration(final int duration) {
            this.duration = duration;
            return this;
        }
    }
}
