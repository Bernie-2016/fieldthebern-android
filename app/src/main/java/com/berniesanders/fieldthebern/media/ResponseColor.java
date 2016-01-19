/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.media;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.CanvassResponse;

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
                return ContextCompat.getColor(context, R.color.bernie_light_red);
            case CanvassResponse.STRONGLY_AGAINST:
                return ContextCompat.getColor(context, R.color.bernie_red);
            case CanvassResponse.ASKED_TO_LEAVE:
                return Color.BLACK;
            case CanvassResponse.NO_ONE_HOME:
                return Color.GRAY;
            default:
                return Color.WHITE;
        }
    }
}
