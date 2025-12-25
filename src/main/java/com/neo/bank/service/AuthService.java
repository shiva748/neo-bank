package com.neo.bank.service;

import com.neo.bank.dto.ApiResponse;
import com.neo.bank.dto.LoginRequest;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.entity.Role;
import com.neo.bank.entity.Users;
import com.neo.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @Autowired
    public AuthService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    public ResponseEntity<?> Register(Users user) {
        if (!user.isValid()) {
            return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(false, "Please enter valid details"));
        }
        try{
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Users newUser = userRepo.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Registration done", true , newUser));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, e.getMessage()));
        }
    }

    public Optional<String> Login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            Optional<Users> users = Optional.ofNullable(userRepo.findByUsername(loginRequest.getUsername()));
            return Optional.of(jwtService.generateToken(users.get().getUsername(), users.get().getRole()));
        }else{
            return Optional.empty();
        }
    }
}
