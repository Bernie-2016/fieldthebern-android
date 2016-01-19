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

import android.support.annotation.DrawableRes;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.Party;

import timber.log.Timber;

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
                Timber.e("cant find PartyIcon for "+party);
                return R.drawable.ic_other;
        }
    }
}
