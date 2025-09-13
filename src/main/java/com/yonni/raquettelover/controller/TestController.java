package com.yonni.raquettelover.controller;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }
}
