package com.yonni.raquettelover.exception;

public class NotUniqueExceptionCustom extends RuntimeException {
    private final String fieldName;

    public NotUniqueExceptionCustom(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
