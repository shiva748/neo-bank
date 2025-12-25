package com.neo.bank.controller;

import com.neo.bank.dto.ApiResponse;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.entity.Users;
import com.neo.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserRepo userRepo;
    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public ResponseEntity<?> home(Authentication authentication) {
        try{
            String username = authentication.getName();
            Optional<Users> user = Optional.of(userRepo.findByUsername(username));
            if(user.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User detail's fetched.", true, user.get()));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Some error Occured."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Some error Occured."));
        }
    }

}
