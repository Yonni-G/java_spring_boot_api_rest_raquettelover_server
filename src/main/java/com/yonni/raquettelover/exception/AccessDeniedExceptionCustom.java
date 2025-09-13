package com.yonni.raquettelover.exception;

public class AccessDeniedExceptionCustom extends RuntimeException {
    public AccessDeniedExceptionCustom(String message) {
        super(message);
    }
}
