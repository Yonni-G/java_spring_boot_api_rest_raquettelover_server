package com.yonni.raquettelover.exception;

public class IllegalArgumentExceptionCustom extends RuntimeException {
    private final String fieldName;

    public IllegalArgumentExceptionCustom(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
