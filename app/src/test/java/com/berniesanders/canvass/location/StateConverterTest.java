package com.berniesanders.canvass.location;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class StateConverterTest {

    @Test
    public void testGetStateCode_returnsNullForNullKey() {
        assertThat(StateConverter.getStateCode(null)).isNull();
    }

    @Test
    public void testGetStateCode_returnsNullForNotFoundKey() {
        assertThat(StateConverter.getStateCode("This is not a state name")).isNull();
    }

    @Test
    public void testGetStateCode_returnsNYForNewYork() {
        assertThat(StateConverter.getStateCode("New York")).isEqualTo("NY");
    }

    @Test
    public void testGetStateCode_returnsNullForEmpty() {
        assertThat(StateConverter.getStateCode("")).isNull();
    }

    @Test
    public void testGetStateCode_returnsNullForBlank() {
        assertThat(StateConverter.getStateCode("  ")).isNull();
    }
}
