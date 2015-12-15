package com.berniesanders.fieldthebern.parsing;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
import android.widget.EditText;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FormValidator {

    public static boolean isNullOrBlank(EditText editText) {
        return (editText.getText()==null || isBlank(editText.getText().toString()));
    }
}
