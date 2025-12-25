package com.neo.bank.repository;

import com.neo.bank.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface UserRepo extends JpaRepository<Users, UUID> {

    Users findByUsername(String username);
}
