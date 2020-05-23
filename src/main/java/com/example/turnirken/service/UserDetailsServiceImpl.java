package com.example.turnirken.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.repository.UserRepository;

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
        return new User(appUser.getLogin(), appUser.getPassword(), emptyList());
    }
}
