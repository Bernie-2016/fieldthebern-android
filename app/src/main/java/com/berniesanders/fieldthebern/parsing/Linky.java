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
