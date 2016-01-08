package com.berniesanders.fieldthebern.parsing;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
import android.util.Patterns;
import android.widget.EditText;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FormValidator {

    public static boolean isNullOrBlank(EditText editText) {
        return (editText.getText()==null || isBlank(editText.getText().toString()));
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPhoneValid(CharSequence phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

}
