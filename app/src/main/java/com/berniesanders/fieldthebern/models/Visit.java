package com.berniesanders.fieldthebern.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class Visit {

    Attributes attributes;

    /**
     * Mix of ApiAddress and Person objects
     * TODO: need custom serializer
     */
    List<CanvasData> included;

    public Attributes attributes() {
        return this.attributes;
    }

    public List<CanvasData> included() {
        return this.included;
    }

    public Visit attributes(final Attributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Visit included(final List<CanvasData> included) {
        this.included = included;
        return this;
    }


    public static class Attributes {

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
