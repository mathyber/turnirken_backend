package com.example.turnirken.service;

import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Role;
import com.example.turnirken.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByLogin(login);
        if (appUser == null) {
            throw new UsernameNotFoundException(login);
        }

        List<GrantedAuthority> rolly = new ArrayList<>();

        Set<Role> r = appUser.getRoles();
        r.forEach(role -> rolly.add(new SimpleGrantedAuthority(role.getName().name())));

        return new User(appUser.getLogin(), appUser.getPassword(), rolly);
    }
}
