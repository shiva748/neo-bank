package com.neo.bank.service;

import com.neo.bank.entity.Users;
import com.neo.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService  implements UserDetailsService {
    UserRepo userRepo;

    @Autowired
    MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Users> user = Optional.of(userRepo.findByUsername(username));
        if(user.isPresent()){
            return User.withUsername(user.get().getUsername()).password(user.get().getPassword()).roles("USER").build();
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    };
}
