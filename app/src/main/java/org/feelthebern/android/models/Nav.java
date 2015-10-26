package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

import java.util.List;

/**
 * TODO: delay implementation until implmenting all other content items
 */
@Layout(R.layout.row_nav)
public class Nav extends Content {
    private List<Anchor> anchors;
    private List<Link> links;

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public List<Link> getLinks() {
        return links;
    }
}
