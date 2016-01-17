package com.berniesanders.fieldthebern.parsing;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FormValidator {

    public static boolean isNullOrBlank(EditText editText) {
        return (editText.getText() == null || isBlank(editText.getText().toString()));
    }

    public static boolean isNullOrBlank(AutoCompleteTextView autoCompleteTextView) {
        return (autoCompleteTextView.getText()==null || isBlank(autoCompleteTextView.getText().toString()));
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneValid(CharSequence phone) {
        if (Patterns.PHONE.matcher(phone).matches()) {
            return passesPhoneRequirements(phone.toString());
        } else {
            return false;
        }
    }

    private static boolean passesPhoneRequirements(String number) {
        number = number.replaceAll("[^\\d]", "");
        if (number.length() < 10) {
            return false;
        } else if (number.startsWith("0")) {
            return false;
        }
        return true;

    }

}
