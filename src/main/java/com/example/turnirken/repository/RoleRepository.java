package com.example.turnirken.repository;

import com.example.turnirken.entity.Role;
import com.example.turnirken.entity.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
    Optional<Role> findByName(UserRoleEnum name);
}
