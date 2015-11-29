package com.berniesanders.fieldthebern.models;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class TokenTest {

    public static final int TWO_HOURS_MILLIS = (2 * 1000 * 60 * 60);


    @Test
    public void testIsExpired_isExpiredForEqualTime() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now/1000)
                .expires(0);

        assertThat(token.isExpired(now)).isTrue();
    }

    @Test
    public void testIsExpired_isExpiredForEqualElapsedTime() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now/1000)
                .expires(1);

        assertThat(token.isExpired(now+1000)).isTrue();
    }

    @Test
    public void testIsExpired_isNotExpired() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now/1000)
                .expires(1000);

        assertThat(token.isExpired(now)).isFalse();
    }

    @Test
    public void testIsExpired_isExpiredForNegativeExpires() {
        long now = System.currentTimeMillis();

        Token token = new Token()
                .created(now/1000)
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
    @Test
    public void testIsExpired_fromSampleApiData() {
        final long now = 1447745123000L; //ms since epoch
        final long nowSecondsSinceEpoch = 1447745123;//value from api
        final int expiresInSeconds = 7200; //2hrs


        long twoHoursInTheFuture = now + TWO_HOURS_MILLIS;

        Token token = new Token()
                .created(nowSecondsSinceEpoch)
                .expires(expiresInSeconds);

        assertThat(token.isExpired(twoHoursInTheFuture)).isTrue();
    }

    @Test
    public void testIsExpired_validFor1hr59mins() {
        final long now = 1447745123000L;
        final long nowSecondsSinceEpoch = 1447745123;//value from api
        final int expiresInSeconds = 7200; //2hrs


        long twoHoursInTheFuture = now + TWO_HOURS_MILLIS-2;

        Token token = new Token()
                .created(nowSecondsSinceEpoch)
                .expires(expiresInSeconds);

        assertThat(token.isExpired(twoHoursInTheFuture)).isFalse();
    }
}
