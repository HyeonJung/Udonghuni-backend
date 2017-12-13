package com.hj.udonghuni.web.exception;

public final class CustomResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3418188561532694566L;

    public CustomResourceNotFoundException() {
        super();
    }

    public CustomResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CustomResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomResourceNotFoundException(String message) {
        super(message);
    }

    public CustomResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
