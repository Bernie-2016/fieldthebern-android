package com.berniesanders.canvass.models;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class TokenTest {


    @Test
    public void testIsExpired_isExpiredForEqualTime() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now)
                .expires(0);

        assertThat(token.isExpired(now)).isTrue();
    }

    @Test
    public void testIsExpired_isExpiredForEqualElapsedTime() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now)
                .expires(1);

        assertThat(token.isExpired(now+1000)).isTrue();
    }

    @Test
    public void testIsExpired_isNotExpired() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now)
                .expires(1000);

        assertThat(token.isExpired(now)).isFalse();
    }

    @Test
    public void testIsExpired_isExpiredForNegativeExpires() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now)
                .expires(-1);

        assertThat(token.isExpired(now)).isTrue();
    }

    @Test
    public void testIsExpired_isExpiredFor1970() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(0)
                .expires(0);

        assertThat(token.isExpired(now)).isTrue();
    }

    @Test
    public void testIsExpired_isExpiredForTimeTravel() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(0)
                .expires(-2700);

        assertThat(token.isExpired(now)).isTrue();
    }
}
