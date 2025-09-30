package com.yonni.raquettelover.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonni.raquettelover.dto.UserSignUpDto;
import com.yonni.raquettelover.entity.Role;
import com.yonni.raquettelover.entity.User;
import com.yonni.raquettelover.exception.NotUniqueExceptionCustom;
import com.yonni.raquettelover.repository.RoleRepository;
import com.yonni.raquettelover.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserSubscriptionPlaceService userSubscriptionPlaceService;

    public boolean hasRoleAdmin(UserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRoleManager(UserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_MANAGER")) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void signup(String signUpAs, UserSignUpDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new NotUniqueExceptionCustom("username", "Cette adresse email est déjà utilisée.");
        }

        User newUser = new User();

        newUser.setUsername(dto.username());
        newUser.setPassword(encoder.encode(dto.password()));

        // Tout nouvel utilisateur doit avoir un prénom et un nom
        newUser.setFirstName(dto.firstName());
        newUser.setLastName(dto.lastName());
        // Attribution du rôle par défaut ROLE_USER
        Role joueurRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Le rôle ROLE_USER est introuvable."));
        newUser.getRoles().add(joueurRole);

        // Si un rôle manager est spécifié dans l'URL, on tente de l'ajouter
        if (signUpAs != null && signUpAs.equalsIgnoreCase("manager")) {
            Role managerRole = roleRepository.findByName("ROLE_MANAGER")
                    .orElseThrow(() -> new EntityNotFoundException("Le rôle ROLE_MANAGER est introuvable."));
            newUser.getRoles().add(managerRole);
        }

        userRepository.save(newUser);

        // Si un rôle joueur est spécifié dans l'URL, on tente d'ajouter son club (lieu)
        if (signUpAs != null && signUpAs.equalsIgnoreCase("player")) {
            // on ajoute sa subscription au lieu
            userSubscriptionPlaceService.createSubscription(newUser, dto.placeId());
        }
    }
}
