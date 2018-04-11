package com.intuit.marketplace.service.exception;

/**
 * Exceptions thrown by the application
 *
 * @author Bhargav
 * @since 04/09/2018
 */
public class MktRuntimeException extends RuntimeException {

    public MktRuntimeException(String message) {
        super(message);
    }

    public MktRuntimeException(String message, Throwable error) {
        super(message, error);
    }
}
