package com.example.turnirken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.turnirken.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByLogin(String login);
}
