package com.berniesanders.fieldthebern.media;

import android.support.annotation.DrawableRes;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.Party;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class PartyIcon {


    @DrawableRes
    public static int get(@Party.Affiliation String party) {

        switch (party) {
            case Party.DEMOCRAT:
                return R.drawable.ic_democrat;
            case Party.INDEPENDENT:
                return R.drawable.ic_independent;
            case Party.REPUBLICAN:
                return R.drawable.ic_republican;
            case Party.UNAFFILIATED:
                return R.drawable.ic_other;
            case Party.UNDECLARED:
                return R.drawable.ic_undeclared;
            case Party.UNKNOWN:
                return R.drawable.ic_undeclared;
            default:
                throw new IllegalArgumentException("cant find PartyIcon for "+party);
        }
    }
}
