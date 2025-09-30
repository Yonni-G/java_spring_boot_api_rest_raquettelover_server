package com.yonni.raquettelover.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.yonni.raquettelover.dto.UserSignUpDto;

public interface UserService {
    boolean hasRoleAdmin(UserDetails user);
    boolean hasRoleManager(UserDetails user);
    void signup(String signUpAs, UserSignUpDto dto);
}
