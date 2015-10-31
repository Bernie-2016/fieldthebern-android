package org.feelthebern.android.parsing;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.feelthebern.android.models.Link;
import org.feelthebern.android.models.Linkable;

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

    public static void linkify(TextView textView, List<Link> links, String text) {

        int length = text.length();
        StringBuilder stringBuilder = new StringBuilder();

        for (Link link : links) {

            int start           = link.getStart();
            int end             = link.getEnd();
            String href         = link.getHref();
            String leadingText  = text.substring(0, start);
            String openTag      = String.format(aherf, href);
            String linkedText   = text.substring(start, end);
            String trailingText = text.substring(end, length);

            stringBuilder
                    .append(leadingText)
                    .append(openTag)
                    .append(linkedText)
                    .append(closeTag)
                    .append(trailingText);
        }

        String linkedText = stringBuilder.toString();
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(linkedText));
    }


    public static boolean hasLinks(Linkable linkableModel) {
        return  linkableModel.getLinks() !=null &&
                !linkableModel.getLinks().isEmpty();
    }
}
