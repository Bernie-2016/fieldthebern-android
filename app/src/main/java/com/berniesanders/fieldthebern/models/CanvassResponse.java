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
            STRONGLY_AGAINST
    })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Response{}

    public static final String UNKNOWN = "Unknown";
    public static final String ASKED_TO_LEAVE = "Asked to leave";
    public static final String STRONGLY_FOR = "Strongly for";
    public static final String LEANING_FOR = "Leaning for";
    public static final String UNDECIDED = "Undecided";
    public static final String LEANING_AGAINST = "Leaning against";
    public static final String STRONGLY_AGAINST = "Strongly against";

}
