package com.berniesanders.fieldthebern.media;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.berniesanders.fieldthebern.R;
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
    public static int getColor(@CanvassResponse.Response final String response, Context context) {

        switch (response) {
            case CanvassResponse.UNKNOWN:
                return ContextCompat.getColor(context, R.color.bernie_grey);
            case CanvassResponse.STRONGLY_FOR:
                return ContextCompat.getColor(context, R.color.bernie_dark_blue);
            case CanvassResponse.LEANING_FOR:
                return ContextCompat.getColor(context, R.color.b_light_blue);
            case CanvassResponse.UNDECIDED:
                return ContextCompat.getColor(context, R.color.bernie_green);
            case CanvassResponse.LEANING_AGAINST:
                return ContextCompat.getColor(context, R.color.bernie_red);
            case CanvassResponse.STRONGLY_AGAINST:
                return ContextCompat.getColor(context, R.color.bernie_red);
            case CanvassResponse.ASKED_TO_LEAVE:
                return ContextCompat.getColor(context, R.color.bernie_red);
            case CanvassResponse.NO_ONE_HOME:
                return ContextCompat.getColor(context, R.color.bernie_grey);
            default:
                return Color.WHITE;
        }
    }
}
