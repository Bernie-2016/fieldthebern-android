package org.feelthebern.android.dagger;

import android.content.Context;

/**
 * Created by AndrewOrobator on 8/29/15.
 */
public class Dagger {
    private static MainComponent sMainComponent;

    public static MainComponent mainCompenent(Context context) {
        if (sMainComponent == null) {
            sMainComponent = DaggerMainComponent.builder()
                    .mainModule(new MainModule(context))
                    .build();
        }

        return sMainComponent;
    }
}
