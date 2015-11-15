package com.berniesanders.canvass.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StringValidator {

    /**
     * Uses apache.commons StringUtils.isBlank() to check for null/blank
     * Useful for exception/debug messages
     *
     * @param strings to check for null or blankness.
     * @param names names of the strings to check in the same order
     * @return A list of the null or empty names
     */
    public static List<String> findNullOrBlank(String[] strings, String[] names) {

        List<String> nullOrBlankStrings = new ArrayList<>();

        for(int i=0; i < strings.length; i++) {
            if (StringUtils.isBlank(strings[i])) {
                nullOrBlankStrings.add(names[i]);
            }
        }

        return nullOrBlankStrings;
    }
}
