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
    List<CanvassData> included = new ArrayList<>();

    public List<CanvassData> included() {
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
