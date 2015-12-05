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

public class CanvassResponse {


    // list of accepted constants
    // 'Unknown'|'Asked to leave'|'Strongly for'|'Leaning for'|
    // 'Undecided'|'Leaning against'|'Strongly against'
    @StringDef({
            UNKNOWN,
            ASKED_TO_LEAVE,
            STRONGLY_FOR,
            LEANING_FOR,
            UNDECIDED,
            LEANING_AGAINST,
            STRONGLY_AGAINST,
            NO_ONE_HOME
    })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Response{}

    public static final String UNKNOWN = "unknown";
    public static final String ASKED_TO_LEAVE = "asked_to_leave";
    public static final String STRONGLY_FOR = "strongly_for";
    public static final String LEANING_FOR = "leaning_for";
    public static final String UNDECIDED = "undecided";
    public static final String LEANING_AGAINST = "leaning_against";
    public static final String STRONGLY_AGAINST = "strongly_against";
    public static final String NO_ONE_HOME = "not_home";

}
