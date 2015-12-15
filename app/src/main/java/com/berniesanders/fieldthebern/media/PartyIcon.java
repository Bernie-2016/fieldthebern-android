package com.berniesanders.fieldthebern.media;

import android.support.annotation.DrawableRes;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.Party;

import timber.log.Timber;

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
            case Party.DEMOCRAT_AFFILIATION :
                return R.drawable.ic_democrat;
            case Party.INDEPENDENT:
            case Party.INDEPENDENT_AFFILIATION :
                return R.drawable.ic_independent;
            case Party.REPUBLICAN:
            case Party.REPUBLICAN_AFFILIATION :
                return R.drawable.ic_republican;
            case Party.UNAFFILIATED:
            case Party.UNAFFILIATED_AFFILIATION :
                return R.drawable.ic_other;
            case Party.UNDECLARED:
            case Party.UNDECLARED_AFFILIATION :
                return R.drawable.ic_undeclared;
            case Party.UNKNOWN:
            case Party.UNKNOWN_AFFILIATION :
                return R.drawable.ic_undeclared;
            default:
                Timber.e("cant find PartyIcon for "+party);
                return R.drawable.ic_other;
        }
    }
}
