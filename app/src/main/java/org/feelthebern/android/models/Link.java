package org.feelthebern.android.models;

/**
 *
 */
public class Link {
    private String href;

    private int start; //zero based index of the character prior to starting the link
    private int end;    //zero based index of the character after to ending the link
}


// The following should linkify the word "majority"
//text: "The majority of the U.S. prison population is male and..."
//href: "http://www.example.com/",
//start: 4,
//end: 12
