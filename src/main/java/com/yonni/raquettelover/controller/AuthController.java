package com.yonni.raquettelover.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.UserSignInDto;
import com.yonni.raquettelover.dto.UserSignUpDto;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.JwtUtil;
import com.yonni.raquettelover.security.ValidationUtil;
import com.yonni.raquettelover.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> authenticateUser(@Valid @RequestBody UserSignInDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // dans la charge utile, on met le username comme sujet ainsi que les roles
            String jwtToken = jwtUtils.generateToken(userDetails.getUsername(), roles, userDetails.getId());

            Map<String, String> data = new HashMap<>();
            data.put("jwtToken", jwtToken);
            data.put("firstName", userDetails.getFirstName());
            return ResponseEntity.ok(ApiResponse.success(data, "Authentification réussie !"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("BAD_CREDENTIALS",
                            "Erreur : nom d'utilisateur ou mot de passe incorrect.")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(new ApiError("INTERNAL_SERVER_ERROR",
                            "Erreur serveur interne lors de l'authentification.")));
        }
    }

    @PostMapping({ "/signup/{signUpAs}" })
    public ResponseEntity<ApiResponse<?>> registerUser(@PathVariable String signUpAs, @Valid @RequestBody UserSignUpDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        userService.signup(signUpAs, dto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(null, "Compte créé avec succès !"));
    }
}
