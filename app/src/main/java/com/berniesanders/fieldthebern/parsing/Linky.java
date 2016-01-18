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

import com.berniesanders.fieldthebern.models.Link;

import java.util.List;

/**
 * The following should linkify the word "majority"
 * text: "The majority of the U.S. prison population is male and..."
 * href: "http://www.example.com/",
 * start: 4,
 * end: 12
 *
 * @see Link
 */
public class Linky {

    private static final String aherf = "<a href=\"%s\">";
    private static final String closeTag = "</a>";

    public static String linkify(List<Link> links, String text) {

        //if(!hasLinks(links)) { return text; }

        int addedChars = 0; //offset for inserting

        for (Link link : links) {
            StringBuilder stringBuilder = new StringBuilder(text);

            int start           = link.getStart();
            int end             = link.getEnd();
            String href         = link.getHref();
            String openTag      = String.format(aherf, href);

            stringBuilder.insert(addedChars + start, openTag);
            addedChars += openTag.length();

            stringBuilder.insert(addedChars + end, closeTag);
            addedChars += closeTag.length();

            text = stringBuilder.toString();
        }

        return text;
    }


    public static boolean hasLinks(List<Link> links) {
        return  links!=null &&
                !links.isEmpty();
    }
}
