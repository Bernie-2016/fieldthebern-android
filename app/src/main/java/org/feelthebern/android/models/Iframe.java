package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_iframe)
public class Iframe extends Content {

    private String src;

    @Override
    public String getText() {
        return src;
    }
}
