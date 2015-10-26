package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_video)
public class Video extends Content {

    private String src;

    @Override
    public String getText() {
        return src;
    }
}
