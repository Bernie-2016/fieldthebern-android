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

    public static String getText(@Party.Affiliation String apiParty, String[] partyArray) {

        switch (apiParty) {
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


    /**
     * This is a hack to get around an inconsistency in a bug in the API
     *
     * The API returns party in the format "democrat_affiliation"
     * However, the API accepts party in the format "Democrat"
     *
     * See: https://github.com/Bernie-2016/fieldthebern-android/issues/219
     *
     * Marked as deprecated to make clear this should only be used in very limited situations
     * @deprecated
     */
    @Party.Affiliation
    public static String mapApiParty(@Party.Affiliation String output) {

        switch (output) {
            case Party.UNKNOWN_AFFILIATION:
                return Party.UNKNOWN;
            case Party.DEMOCRAT_AFFILIATION:
                return Party.DEMOCRAT;
            case Party.REPUBLICAN_AFFILIATION:
                return Party.REPUBLICAN;
            case Party.INDEPENDENT_AFFILIATION:
                return Party.INDEPENDENT;
            case Party.UNDECLARED_AFFILIATION:
                return Party.UNDECLARED;
            case Party.UNAFFILIATED_AFFILIATION:
                return Party.UNAFFILIATED;
            default:
                Timber.e("PartyEvaluator.mapApiParty() called with unmapped party? returning original string as a fallback");
                return output;
        }
    }
}
