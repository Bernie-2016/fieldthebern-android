package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Party {

    // party_affiliation:
    // <'Unknown'|'Unaffiliated'|'Undeclared'|
    // 'Democrat'|'Republican'|'Independent'>,
    @StringDef({
            UNKNOWN,
            UNAFFILIATED,
            UNDECLARED,
            DEMOCRAT,
            REPUBLICAN,
            INDEPENDENT
    })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Affiliation{}

    public static final String UNKNOWN = "Unknown";
    public static final String UNAFFILIATED = "Unaffiliated";
    public static final String UNDECLARED = "Undeclared";
    public static final String DEMOCRAT = "Democrat";
    public static final String REPUBLICAN = "Republican";
    public static final String INDEPENDENT = "Independent";

}
