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

package com.berniesanders.fieldthebern.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Party {

    // party_affiliation:
    // <'unknown_affiliation'|'unaffiliated_affiliation'
    // |'undeclared_affiliation'|'democrat_affiliation'
    // |'republican_affiliation'|'independent_affiliation'
    // |'other_affiliation'>,
    @StringDef({
            UNKNOWN,
            UNAFFILIATED,
            UNDECLARED,
            DEMOCRAT,
            REPUBLICAN,
            INDEPENDENT,
            OTHER
            })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the annotation
    public @interface Affiliation{}

    //Api input
    public static final String UNKNOWN          = "unknown_affiliation";
    public static final String UNAFFILIATED     = "unaffiliated_affiliation";
    public static final String UNDECLARED       = "undeclared_affiliation";
    public static final String DEMOCRAT         = "democrat_affiliation";
    public static final String REPUBLICAN       = "republican_affiliation";
    public static final String INDEPENDENT      = "independent_affiliation";
    public static final String OTHER            = "other_affiliation";

}
