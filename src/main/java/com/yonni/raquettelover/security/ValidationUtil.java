package com.yonni.raquettelover.security;

import com.yonni.raquettelover.dto.ApiError;
import org.springframework.validation.BindingResult;

import java.util.List;

public class ValidationUtil {

    private ValidationUtil() {} // Classe utilitaire = non instanciable

    public static ApiError buildValidationError(BindingResult bindingResult) {
        List<ApiError.FieldError> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map(err -> new ApiError.FieldError(err.getField(), err.getDefaultMessage()))
                .toList();

        return new ApiError("INVALID_DATA", "Certains champs sont invalides", fieldErrors);
    }
}
