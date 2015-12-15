package com.berniesanders.fieldthebern.exceptions;

/**
 *
 */
public class LocationUnavailableException extends Exception {

    public LocationUnavailableException() {
        super();
    }

    public LocationUnavailableException(String detailMessage) {
        super(detailMessage);
    }

    public LocationUnavailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LocationUnavailableException(Throwable throwable) {
        super(throwable);
    }
}
