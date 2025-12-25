package com.neo.bank.controller;

import com.neo.bank.dto.LoginRequest;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.entity.Users;
import com.neo.bank.service.AuthService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${spring.application.token.expiry}")
    private int tokenExpiry;
    private AuthService authService;

    @Autowired
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        return authService.Register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<String> token = authService.Login(loginRequest);
        if(token.isPresent()){
            String Token = token.get();
            Cookie cookie = new Cookie("jwt",Token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(tokenExpiry);
            return ResponseEntity.ok()
                    .header("Set-Cookie", String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly",
                            cookie.getName(),
                            cookie.getValue(),
                            cookie.getMaxAge(),
                            cookie.getPath()))
                    .body(new SimpleResponse(true,"login successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleResponse(false, "login failed"));
    }
}
