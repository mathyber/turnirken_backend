package com.example.turnirken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.turnirken.entity.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByLogin(String login);
    AppUser findByEmail(String email);
}
