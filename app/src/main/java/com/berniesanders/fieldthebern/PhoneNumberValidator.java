package com.berniesanders.fieldthebern;

public class PhoneNumberValidator {
    public static boolean isValid(String number) {
        number = number.replaceAll("[^\\d]", "");
        if (number.length() < 10) {
            return false;
        }
        if (number.startsWith("0")) {
            return false;
        }
        return true;
    }
}
