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
    // <'unknown_affiliation'|'unaffiliated_affiliation'
    // |'undeclared_affiliation'|'democrat_affiliation'
    // |'republican_affiliation'|'independent_affiliation'
    // |'other_affiliation'>,
    @StringDef({
            UNKNOWN,
            UNAFFILIATED,
            UNDECLARED,
            DEMOCRAT,
            REPUBLICAN,
            INDEPENDENT,
            OTHER
            })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Affiliation{}

    //Api input
    public static final String UNKNOWN          = "unknown_affiliation";
    public static final String UNAFFILIATED     = "unaffiliated_affiliation";
    public static final String UNDECLARED       = "undeclared_affiliation";
    public static final String DEMOCRAT         = "democrat_affiliation";
    public static final String REPUBLICAN       = "republican_affiliation";
    public static final String INDEPENDENT      = "independent_affiliation";
    public static final String OTHER            = "other_affiliation";

}
