package com.yonni.raquettelover.controller.advice;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.exception.AccessDeniedExceptionCustom;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔸 Gestion des erreurs de validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiError.FieldError(err.getField(), err.getDefaultMessage()))
                .toList();

        ApiError apiError = new ApiError(
                "INVALID_DATA",
                "Certains champs sont invalides",
                fieldErrors);

        return ResponseEntity.badRequest().body(ApiResponse.error(apiError));
    }

    // exceptions Access Denied METIER
    @ExceptionHandler(AccessDeniedExceptionCustom.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedExceptionCustom ex) {
        ApiError apiError = new ApiError("FORBIDDEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(apiError));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {
        ApiError apiError = new ApiError("FORBIDDEN", "Accès refusé");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(apiError));
    }

    // exceptions AuthorizationDeniedException
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ApiError apiError = new ApiError("FORBIDDEN", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(apiError));
    }

    

    // exceptions Not Unique
    @ExceptionHandler(NotUniqueExceptionCustom.class)
    public ResponseEntity<ApiResponse<Object>> handleNotUnique(NotUniqueExceptionCustom ex) {
        ApiError.FieldError fieldError = new ApiError.FieldError(ex.getFieldName(), ex.getMessage());
        ApiError apiError = new ApiError("EVER_TAKEN", ex.getMessage(), Collections.singletonList(fieldError));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(apiError));
    }

    // exceptions Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFounde(EntityNotFoundException ex) {
        ApiError apiError = new ApiError("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(apiError));
    }

    // 🔸 Gestion des autres erreurs globales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiError apiError = new ApiError("SERVER_ERROR", "Une erreur est survenue côté serveur");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(apiError));
    }
}