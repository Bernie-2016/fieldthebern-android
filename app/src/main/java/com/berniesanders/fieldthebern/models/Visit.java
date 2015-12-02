package com.berniesanders.fieldthebern.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class Visit {

    Attributes attributes = new Attributes();

    /**
     * Mix of ApiAddress and Person objects
     */
    List<CanvassData> included = new ArrayList<>();

    public Attributes attributes() {
        return this.attributes;
    }

    public List<CanvassData> included() {
        return this.included;
    }

    public Visit attributes(final Attributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Visit included(final List<CanvassData> included) {
        this.included = included;
        return this;
    }


    public void start() {
        attributes.startTime = System.currentTimeMillis();
    }

    public void stop() {
        attributes.finishTime = System.currentTimeMillis();
        attributes.duration((int) ((attributes.finishTime - attributes.startTime)/1000));
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
