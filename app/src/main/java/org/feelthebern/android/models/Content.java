package org.feelthebern.android.models;

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
 * @see org.feelthebern.android.adapters.PageContentTypeAdapter
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
}
