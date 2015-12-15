package com.berniesanders.fieldthebern.apilevels;

import android.os.Build;

/**
 *
 */
public class ApiLevel {

    public static boolean isIcsOrAbove() {
        //Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        return Build.VERSION.SDK_INT >= 14;
    }
    public static boolean isLollipopOrAbove() {
        //Build.VERSION_CODES.LOLLIPOP
        return Build.VERSION.SDK_INT >= 21 ;
    }
}
