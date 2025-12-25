package com.neo.bank.controller;

import com.neo.bank.dto.UpdatePassword;
import com.neo.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> home(Authentication authentication) {
        return userService.getUser(authentication);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(Authentication authentication, @RequestBody UpdatePassword updatePassword) {
        return userService.updatePassword(authentication, updatePassword);
    }
}
