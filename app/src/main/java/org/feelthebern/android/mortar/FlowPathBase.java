package org.feelthebern.android.mortar;

import flow.path.Path;

/**
 *
 */
public abstract class FlowPathBase extends Path {
    public abstract int getLayout();

    public abstract Object createComponent();

    public abstract String getScopeName();
}
