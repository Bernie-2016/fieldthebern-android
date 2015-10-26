package org.feelthebern.android.models;

import org.feelthebern.android.R;
import org.feelthebern.android.annotations.Layout;

/**
 *
    text: "http://feelthebern.org/wp-content/uploads/2015/07/US-rates-over-time.jpg",
    type: "img",
    width: 445,
    height: 512,
    caption: "Source",
    source: "http://www.washingtonpost.com/news/wonkblog/wp/2014/04/30/the-meteoric-costly-and-unprecedented-rise-of-incarceration-in-america/"
 */
@Layout(R.layout.row_img)
public class Img extends Content {

    //text here is the url of the image
    //private String text;

    private int width;
    private int height;
    private String caption;
    private String source;  //attribution source

    /*
    text: "http://feelthebern.org/wp-content/uploads/2015/07/US-rates-over-time.jpg",
    type: "img",
    width: 445,
    height: 512,
    caption: "Source",
    source: "http://www.washingtonpost.com/news/wonkblog/wp/2014/04/30/the-meteoric-costly-and-unprecedented-rise-of-incarceration-in-america/"
    */
}
