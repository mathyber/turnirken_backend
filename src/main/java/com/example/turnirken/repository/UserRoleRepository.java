package com.example.turnirken.repository;

import com.example.turnirken.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByAppUser_Login(String login);
}
