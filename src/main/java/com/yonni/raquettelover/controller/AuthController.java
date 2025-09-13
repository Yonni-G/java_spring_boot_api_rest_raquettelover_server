package com.yonni.raquettelover.controller;

import com.yonni.raquettelover.dto.ApiError;
import com.yonni.raquettelover.dto.ApiResponse;
import com.yonni.raquettelover.dto.UserDto;
import com.yonni.raquettelover.entity.Role;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.repository.RoleRepository;
import com.yonni.raquettelover.repository.UserRepository;
import com.yonni.raquettelover.security.CustomUserDetails;
import com.yonni.raquettelover.security.JwtUtil;
import com.yonni.raquettelover.security.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> authenticateUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password())
            );

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

    @PostMapping({"/signup", "/signup/{roleName}"})
    public ResponseEntity<ApiResponse<?>> registerUser(@PathVariable(required = false) String roleName, @Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ValidationUtil.buildValidationError(bindingResult)));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(new ApiError("USERNAME_TAKEN",
                            "Erreur : cette adresse email est déjà utilisée.")));
        }

        try {
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(encoder.encode(user.getPassword()));

            // Tout nouvel utilisateur doit avoir un prénom et un nom
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            // Attribution du rôle par défaut ROLE_USER
            Role joueurRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Le rôle ROLE_USER est introuvable."));
            newUser.getRoles().add(joueurRole);
            // Si un rôle est spécifié dans l'URL, on tente de l'ajouter
            if (roleName != null && roleName.equalsIgnoreCase("manager")) {
                Role managerRole = roleRepository.findByName("ROLE_MANAGER")
                        .orElseThrow(() -> new RuntimeException("Le rôle ROLE_MANAGER est introuvable."));
                newUser.getRoles().add(managerRole);
            }
            // A SUPPRIMER EN PRODUCTION : Si un rôle est spécifié dans l'URL, on tente de l'ajouter
            if (roleName != null && roleName.equalsIgnoreCase("admin")) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("Le rôle ROLE_ADMIN est introuvable."));
                newUser.getRoles().add(adminRole);
            }

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null, "Utilisateur enregistré avec succès !"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(new ApiError("BAD_REQUEST", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(new ApiError("INTERNAL_SERVER_ERROR",
                            "Erreur serveur interne lors de l'inscription.")));
        }
    }
}
