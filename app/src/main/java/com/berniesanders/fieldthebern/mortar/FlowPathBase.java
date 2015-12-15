package com.berniesanders.fieldthebern.mortar;

import flow.path.Path;

/**
 *
 */
public abstract class FlowPathBase extends Path {

    public abstract Object createComponent();

    public abstract String getScopeName();
}
