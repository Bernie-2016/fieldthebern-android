package com.berniesanders.fieldthebern.models;


import java.util.List;

/**
 * indicates a model that can contain Link(s) to be added to the text
 */
public interface Linkable {
    List<Link> getLinks();
    String getText();
}
