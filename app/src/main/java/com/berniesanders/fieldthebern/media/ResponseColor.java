package com.berniesanders.fieldthebern.media;

import android.graphics.Color;

import com.berniesanders.fieldthebern.models.CanvassResponse;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class ResponseColor {

    /**
     * Returns the user displayable string based off the supplied response
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
    public static int getColor(@CanvassResponse.Response final String response) {

        switch (response) {
            case CanvassResponse.UNKNOWN:
                return Color.DKGRAY;
            case CanvassResponse.STRONGLY_FOR:
                return Color.BLUE;
            case CanvassResponse.LEANING_FOR:
                return Color.CYAN;
            case CanvassResponse.UNDECIDED:
                return Color.GREEN;
            case CanvassResponse.LEANING_AGAINST:
                return Color.MAGENTA;
            case CanvassResponse.STRONGLY_AGAINST:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }
}
