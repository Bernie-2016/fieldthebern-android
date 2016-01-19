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

package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.Party;

import timber.log.Timber;

public class PartyEvaluator {


    /**
     * Returns the API compatible response that maps to the selected string
     *
     *    <item>(select party)</item>
     *    <item>Democrat</item>
     *    <item>Republican</item>
     *    <item>Independent</item>
     *    <item>Undeclared</item>
     *    <item>Unknown</item>
     *    <item>Other</item>
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
            return Party.OTHER;
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
            case Party.UNAFFILIATED:
                return partyArray[0];
            case Party.DEMOCRAT:
                return partyArray[1];
            case Party.REPUBLICAN:
                return partyArray[2];
            case Party.INDEPENDENT:
                return partyArray[3];
            case Party.UNDECLARED:
                return partyArray[4];
            case Party.OTHER:
                return partyArray[5];
            default:
                Timber.w("PartyEvaluator.getText() called with unmapped party?");
                return partyArray[0];
        }
    }

}
