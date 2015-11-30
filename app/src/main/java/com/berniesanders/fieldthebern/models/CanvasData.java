package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, Coderly, Lost Packet Software and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

/**
 * Wrapper class for an array of mixed items of Person and ApiAddress
 * see Visit
 */
public class CanvasData {

    protected String type;

    public String type() {
        return this.type;
    }

    public CanvasData type(final String type) {
        this.type = type;
        return this;
    }

}
