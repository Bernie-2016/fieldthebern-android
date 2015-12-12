package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.Party;

import timber.log.Timber;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class PartyEvaluator {


    /**
     * Returns the API compatible response that maps to the selected string
     *
     *   <item>(select party)</item>
     *   <item>Democrat</item>
     *   <item>Republican</item>
     *   <item>Independent</item>
     *   <item>Undeclared</item>
     *   <item>Other</item>
     *
     * TODO better way to do this?!
    */
    @Party.Affiliation
    public static String getParty(String selectedParty, String[] partyArray) {
        if (selectedParty.equals(partyArray[0])) {
            Timber.e("PartyEvaluator.getParty() called but selection is not valid");
            return Party.UNKNOWN;
        } else if (selectedParty.equals(partyArray[1])) {
            return Party.DEMOCRAT;
        } else if (selectedParty.equals(partyArray[2])) {
            return Party.REPUBLICAN;
        } else if (selectedParty.equals(partyArray[3])) {
            return Party.INDEPENDENT;
        } else if (selectedParty.equals(partyArray[4])) {
            return Party.UNDECLARED;
        } else if (selectedParty.equals(partyArray[5])) {
            return Party.UNKNOWN;
        } else {
            Timber.e("PartyEvaluator.getParty() called but selection is default?");
            return Party.UNKNOWN;
        }
    }

    /**
     * Returns the API compatible response that maps to the selected string
     *
     *   <item>(select party)</item>
     *   <item>Democrat</item>
     *   <item>Republican</item>
     *   <item>Independent</item>
     *   <item>Undeclared</item>
     *   <item>Other</item>
     *
     * TODO better way to do this?!
    */

    public static String getText(@Party.Affiliation String apiPary, String[] partyArray) {

        switch (apiPary) {
            case Party.UNKNOWN:
                return partyArray[0];
            case Party.DEMOCRAT:
                return partyArray[1];
            case Party.REPUBLICAN:
                return partyArray[2];
            case Party.INDEPENDENT:
                return partyArray[3];
            case Party.UNDECLARED:
                return partyArray[4];
            case Party.UNAFFILIATED:
                return partyArray[5];
            default:
                Timber.e("PartyEvaluator.getText() called with unmapped party?");
                return partyArray[0];
        }
    }
}
