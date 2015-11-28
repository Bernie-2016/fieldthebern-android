package com.berniesanders.fieldthebern.annotations;

/*
 * Copyright 2016 FeelTheBern.org
 * Copyright 2013 Square Inc.
 *
 * Originally Licensed under the Apache License, Version 2.0
 */


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class that designates a screen and specifies its layout. A screen is a distinct part of
 * an application containing all information that describes this state.
 *
 * <p>For example, <pre><code>
 * {@literal@}Layout(R.layout.my_layout)
 * public class MyScreen { ... }
 * </code></pre>
 */
@Retention(RUNTIME) @Target(TYPE) public @interface Layout {
    int value();
}
