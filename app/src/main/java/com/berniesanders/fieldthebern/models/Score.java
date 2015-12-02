package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import com.google.gson.annotations.SerializedName;

/**
{
    id: <score_id>
    type: 'scores',
    attributes: {
        points_for_updates: <points_for_updates>,
        points_for_knock: <points_for_knock>
    },
    relationships: {
        visit: {
            data: { id: <visit_id>, type: 'visits' }
        }
    }
}
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
