package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.CanvassResponse;

import timber.log.Timber;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class CanvassResponseEvaluator {

    /**
     * Returns the API compatible response that maps to the selected string
     *
     * <item>(select support level)</item>
     * <item>Strongly for Bernie</item>
     * <item>Leaning for Bernie</item>
     * <item>Undecided</item>
     * <item>Leaning against Bernie</item>
     * <item>Strongly against Bernie</item>
     *
     * TODO better way to do this?!
     */
    @CanvassResponse.Response
    public static String getResponse(String selectedItem, String[] interest) {
        if (selectedItem.equals(interest[0])) {
            Timber.e("CanvassResponseEvaluator.getResponse() called but selection is not valid");
            return CanvassResponse.UNKNOWN;
        } else if (selectedItem.equals(interest[1])) {
            return CanvassResponse.STRONGLY_FOR;
        } else if (selectedItem.equals(interest[2])) {
            return CanvassResponse.LEANING_FOR;
        } else if (selectedItem.equals(interest[3])) {
            return CanvassResponse.UNDECIDED;
        } else if (selectedItem.equals(interest[4])) {
            return CanvassResponse.LEANING_AGAINST;
        } else if (selectedItem.equals(interest[5])) {
            return CanvassResponse.STRONGLY_AGAINST;
        } else {
            Timber.e("CanvassResponseEvaluator.getResponse() called but selection is default?");
            return CanvassResponse.UNKNOWN;
        }
    }
}
