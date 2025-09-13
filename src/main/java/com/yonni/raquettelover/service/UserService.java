package com.yonni.raquettelover.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.yonni.raquettelover.security.CustomUserDetails;

public interface UserService {
    boolean hasRoleAdmin(UserDetails user);
    boolean hasRoleManager(UserDetails user);
}
