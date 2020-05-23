package com.example.turnirken.service;

import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
