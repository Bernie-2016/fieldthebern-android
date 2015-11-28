package com.berniesanders.fieldthebern.exceptions;

/**
 *
 */
public class AddressUnavailableException extends LocationUnavailableException {

    public AddressUnavailableException() {
        super();
    }

    public AddressUnavailableException(String detailMessage) {
        super(detailMessage);
    }

    public AddressUnavailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AddressUnavailableException(Throwable throwable) {
        super(throwable);
    }
}
