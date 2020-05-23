package com.example.turnirken.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MainService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MainService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String getGreeting() {

        return "mahmade!";
    }

    public AppUser create(CreateUserModel userModel) {

        AppUser applicationAppUser = new AppUser();

        applicationAppUser.setLogin(userModel.getLogin());
        applicationAppUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        applicationAppUser.setEmail(userModel.getEmail());

        return userRepository.save(applicationAppUser);
    }

    public String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();//get logged in username
    }
}
