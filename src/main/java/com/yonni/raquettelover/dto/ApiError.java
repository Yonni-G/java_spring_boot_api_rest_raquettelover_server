package com.yonni.raquettelover.dto;

import java.util.List;

public class ApiError {
    private String code;
    private String message;
    private List<FieldError> fields; // optionnel

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError(String code, String message, List<FieldError> fields) {
        this.code = code;
        this.message = message;
        this.fields = fields;
    }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
    }

    // Getters
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public List<FieldError> getFields() { return fields; }
}
