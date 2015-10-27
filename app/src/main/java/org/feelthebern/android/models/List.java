package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
 */
@Layout(R.layout.row_list)
public class List extends Content {

    private java.util.List<String> list;

    @Override
    public String getText() {
        return list.get(0);//TODO
    }
}
