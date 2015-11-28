/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com, Coderly, LostPacketSoftware
 * and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.canvass.models;

import com.google.gson.annotations.SerializedName;

/**
 * Not to be confused with android.location.Address
 *
 * Docs:
 *      https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Addresses
 * Format:
 * {
 *     id: <address_id>,
 *     type: 'addresses',
 *     attributes: {
 *          longitude: <longitude>,
 *          latitude: <latitude>,
 *          street_1: <street_1>,
 *          street_2: <street_2>,
 *          city: <city>,
 *          state_code: <state_code>,
 *          zip_code: <zip_code>,
 *          visited_at: <time_and_date_of_the_last_visit_made_to_this_address>,
 *          best_canvass_response: <most_supportive_canvass_response_value_for_this_address>,
 *          last_canvass_response: <last_canvass_response_value_for_this_address>
 *    }
 * }
 *
 * NOTE: 'best_canvass_response' and 'last_canvass_response' respond with
 * 'not_yet_visited' and 'unknown' respectively when the address hasn't been canvassed previously.
 */
public class ApiAddress {

    long id;
    String type;
    Attributes attributes;

    public static class Attributes {
        double longitude;
        double latitude;
        @SerializedName("street_1")
        String street1;
        @SerializedName("street_2")
        String street2;
        String city;
        @SerializedName("state_code")
        String state;
        @SerializedName("zip_code")
        String zip;
        @SerializedName("visited_at")
        String visitedAt;
        @SerializedName("best_canvass_response")
        String bestCanvassResponse;
        @SerializedName("last_canvass_response")
        String lastCanvassResponse;


        public double longitude() {
            return this.longitude;
        }

        public double latitude() {
            return this.latitude;
        }

        public String street1() {
            return this.street1;
        }

        public String street2() {
            return this.street2;
        }

        public String city() {
            return this.city;
        }

        public String state() {
            return this.state;
        }

        public String zip() {
            return this.zip;
        }

        public String visitedAt() {
            return this.visitedAt;
        }

        public String bestCanvassResponse() {
            return this.bestCanvassResponse;
        }

        public String lastCanvassResponse() {
            return this.lastCanvassResponse;
        }

        public Attributes longitude(final double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Attributes latitude(final double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Attributes street1(final String street1) {
            this.street1 = street1;
            return this;
        }

        public Attributes street2(final String street2) {
            this.street2 = street2;
            return this;
        }

        public Attributes city(final String city) {
            this.city = city;
            return this;
        }

        public Attributes state(final String state) {
            this.state = state;
            return this;
        }

        public Attributes zip(final String zip) {
            this.zip = zip;
            return this;
        }

        public Attributes visitedAt(final String visitedAt) {
            this.visitedAt = visitedAt;
            return this;
        }

        public Attributes bestCanvassResponse(final String bestCanvassResponse) {
            this.bestCanvassResponse = bestCanvassResponse;
            return this;
        }

        public Attributes lastCanvassResponse(final String lastCanvassResponse) {
            this.lastCanvassResponse = lastCanvassResponse;
            return this;
        }


    }

    public long id() {
        return id;
    }

    public void id(long id) {
        this.id = id;
    }

    public String type() {
        return type;
    }

    public void type(String type) {
        this.type = type;
    }

    public Attributes attributes() {
        return attributes;
    }

    public void attributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ApiAddress{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", attributes=" + attributes.toString() +
                '}';
    }
}