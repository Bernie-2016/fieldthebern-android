/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com, Coderly, LostPacketSoftware
 * and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.models;

/**
 * Docs:
 *      https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Addresses
 *
 *  request format
 *  {
 *      latitude: <latitude>,
 *      longitude: <longitude>,
 *      radius: <distance_from_location_in_meters>
 *  }
 */
public class RequestMultipleAddresses {
    double latitude;
    double longitude;
    int radius;

    public double latitude() {
        return latitude;
    }

    public RequestMultipleAddresses latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double longitude() {
        return longitude;
    }

    public RequestMultipleAddresses longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public int radius() {
        return radius;
    }

    public RequestMultipleAddresses radius(int radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public String toString() {
        return "RequestMultipleAddresses{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }
}
