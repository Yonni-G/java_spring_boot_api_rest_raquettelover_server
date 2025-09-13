package com.yonni.raquettelover.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yonni.raquettelover.repository.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/user")
@Data
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if(userRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully!");
    }
}
