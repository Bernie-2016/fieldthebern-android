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

public class Contact {

    // Contact Method
    // <'email'|'phone'>,
    @StringDef({
            EMAIL,
            PHONE
    })

    @Retention(RetentionPolicy.SOURCE)

    public @interface Method{}

    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

}
