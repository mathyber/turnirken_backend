package com.example.turnirken.service;

import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Role;
import com.example.turnirken.entity.UserRole;
import com.example.turnirken.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository ) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole create(AppUser user, Role role) {

        UserRole ur = new UserRole();
        ur.setAppUser(user);
        ur.setRole(role);
        return userRoleRepository.save(ur);
    }
}
