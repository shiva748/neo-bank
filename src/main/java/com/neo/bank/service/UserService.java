package com.neo.bank.service;

import com.neo.bank.dto.ApiResponse;
import com.neo.bank.dto.SimpleResponse;
import com.neo.bank.dto.UpdatePassword;
import com.neo.bank.entity.Users;
import com.neo.bank.repository.UserRepo;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

    public ResponseEntity<?> updatePassword(Authentication authentication, @RequestBody UpdatePassword updatePassword) {
        try{
            String username = authentication.getName();
            Optional<Users> user = Optional.ofNullable(userRepo.findByUsername(username));
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "User Not Found."));
            }
            if(bCryptPasswordEncoder.matches(updatePassword.getOldPassword(), user.get().getPassword())){
                user.get().setPassword(bCryptPasswordEncoder.encode(updatePassword.getNewPassword()));
                userRepo.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(true,"Password Updated."));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse(false, "Old Password Mismatch."));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
