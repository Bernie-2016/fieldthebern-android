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
            INDEPENDENT,
            UNKNOWN_AFFILIATION,
            UNAFFILIATED_AFFILIATION,
            UNDECLARED_AFFILIATION,
            DEMOCRAT_AFFILIATION,
            REPUBLICAN_AFFILIATION,
            INDEPENDENT_AFFILIATION,
            })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Affiliation{}

    //Api input
    public static final String UNKNOWN = "Unknown";
    public static final String UNAFFILIATED = "Unaffiliated";
    public static final String UNDECLARED = "Undeclared";
    public static final String DEMOCRAT = "Democrat";
    public static final String REPUBLICAN = "Republican";
    public static final String INDEPENDENT = "Independent";


    //fixme API apparently spits this out instead of the same format it takes above as input
    //fixme see https://github.com/Bernie-2016/fieldthebern-android/issues/187
    //fixme work-around until fixed by API
    public static final String UNKNOWN_AFFILIATION          = "unknown_affiliation";
    public static final String UNAFFILIATED_AFFILIATION     = "unaffiliated_affiliation";
    public static final String UNDECLARED_AFFILIATION       = "undeclared_affiliation";
    public static final String DEMOCRAT_AFFILIATION         = "democrat_affiliation";
    public static final String REPUBLICAN_AFFILIATION       = "republican_affiliation";
    public static final String INDEPENDENT_AFFILIATION      = "independent_affiliation";

}
