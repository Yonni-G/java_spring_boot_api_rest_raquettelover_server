package com.yonni.raquettelover.dto;
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private ApiError error;

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = error;
        return response;
    }

    // Getters & setters
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public ApiError getError() { return error; }
}