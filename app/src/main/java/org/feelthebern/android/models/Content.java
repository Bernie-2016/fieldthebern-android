package org.feelthebern.android.models;

import org.feelthebern.android.parsing.PageContentDeserializer;

/**
 * Content is a single item in a page's 'contents' array
 * content item types include "p" "h1" "h2" "h3" "nav"
 *
 * The full site JSON structure is something like:
 *
 * collection {obj}
 *  +-items [array]
 *      +-page
 *      +-collection
 *          +-items [array]
 *              +-page
 *              +-page
 *      +-page
 *      +-collection
 *          +-items [array]
 *              +-page
 *              +-page {obj}
 *                  +-content [array]
 *                      +-content item (this object) h1
 *                      +-content item (this object) h2
 *                      +-content item (this object) p
 *
 * @see PageContentDeserializer
 */
public class Content {
    protected String text;
    protected String type;

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }
}
