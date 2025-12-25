package com.neo.bank.service;

import com.neo.bank.dto.ApiResponse;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.entity.Users;
import com.neo.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> getUser(Authentication authentication) {
        try{
            String username = authentication.getName();
            Optional<Users> user = Optional.ofNullable(userRepo.findByUsername(username));
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
