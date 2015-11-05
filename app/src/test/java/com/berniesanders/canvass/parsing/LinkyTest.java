package com.berniesanders.canvass.parsing;

import com.berniesanders.canvass.models.Link;

import static com.berniesanders.canvass.parsing.Linky.linkify;

import org.junit.Test;

//import static org.junit.Assert.*;
//import static org.assertj.android.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LinkyTest {


    /*
     * The following should linkify the word "majority"
     * text: "The majority of the U.S. prison population is male and..."
     * href: "http://www.example.com/",
     * start: 4,
     * end: 12
     *
     * @see Link
     */
    private String mockString = "One two three.";
    private List<Link> links = new ArrayList<>();


    private Link firstLink() {
        Link link = new Link();
        link.setStart(0);
        link.setEnd(3);
        link.setHref("http://example.com");
        return link;
    }

    private Link secondLink() {
        Link link = new Link();
        link.setStart(4);
        link.setEnd(7);
        link.setHref("http://example.com");
        return link;
    }

    private Link thirdLink() {
        Link link = new Link();
        link.setStart(8);
        link.setEnd(13);
        link.setHref("http://example.com");
        return link;
    }

    //sanity
    @Test
    public void testLinkify_noLink() throws Exception {

        //given no link
        links = new ArrayList<>();
        final String expected = mockString;
        assertThat(linkify(links, mockString)).isEqualTo(expected);
    }


    @Test
    public void testLinkify_oneLink() throws Exception {

        //given one link
        links = new ArrayList<>();
        links.add(firstLink());

        final String expected = "<a href=\"http://example.com\">One</a> two three.";

        assertThat(linkify(links, mockString)).isEqualTo(expected);
    }

    @Test
    public void testLinkify_secondLink() throws Exception {

        //given one link
        links = new ArrayList<>();
        links.add(secondLink());

        final String expected = "One <a href=\"http://example.com\">two</a> three.";

        assertThat(linkify(links, mockString)).isEqualTo(expected);
    }

    @Test
    public void testLinkify_thirdLink() throws Exception {

        //given one link
        links = new ArrayList<>();
        links.add(thirdLink());

        final String expected = "One two <a href=\"http://example.com\">three</a>.";

        assertThat(linkify(links, mockString)).isEqualTo(expected);
    }


    @Test
    public void testLinkify_firstAndThirdLink() throws Exception {

        //given one link
        links = new ArrayList<>();
        links.add(firstLink());
        links.add(thirdLink());

        final String expected = "<a href=\"http://example.com\">One</a> two <a href=\"http://example.com\">three</a>.";

        assertThat(linkify(links, mockString)).isEqualTo(expected);
    }
}
