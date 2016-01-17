package com.berniesanders.fieldthebern.parsing;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FormValidatorTest extends TestCase {

    @Test
    public void testIsPhoneValid() throws Exception {
        //Valid numbers
        assertTrue("Test valid number", FormValidator.isPhoneValid("1234567890"));
        assertTrue("Test long valid number", FormValidator.isPhoneValid("73227257157322725715"));
        assertTrue("Test valid number with formatting", FormValidator.isPhoneValid("(732) 272-5715"));

        //Invalid numbers
        assertFalse("Test short number", FormValidator.isPhoneValid("123"));
        assertFalse("Test number starting with 0", FormValidator.isPhoneValid("0123456789"));
        assertFalse("Test short number starting with 0", FormValidator.isPhoneValid("0123"));
        assertFalse("Test letters", FormValidator.isPhoneValid("ABC"));
    }

}