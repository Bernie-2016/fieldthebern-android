package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

import java.util.List;

/**
 *
 */
@Layout(R.layout.row_quote)
public class Quote extends Content {
    private List<Anchor> anchors;
    private List<Link> links;

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public List<Link> getLinks() {
        return links;
    }
}
