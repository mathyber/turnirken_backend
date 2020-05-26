package com.example.turnirken.service;

import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.dto.TestRegModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
/*
    public String getGreeting() {

        return "mahmade!";
    }
*/
    public AppUser create(CreateUserModel userModel) {

        AppUser appUser = new AppUser();

        appUser.setLogin(userModel.getLogin());
        appUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        appUser.setEmail(userModel.getEmail());
        appUser.setRole("ROLE_USER");
        return userRepository.save(appUser);
    }

    public String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();//get logged in username
    }

    public JSONObject getUserinfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);
        JSONObject userInfo = new JSONObject();
        userInfo.put("id",user.getId());
        userInfo.put("login",user.getLogin());
        userInfo.put("email",user.getEmail());
        userInfo.put("role",user.getRole());
        return userInfo;
    }

    public boolean testLogin(TestRegModel userModel) {
        return userRepository.findByLogin(userModel.getStr())!=null;
    }

    public boolean testEmail(TestRegModel userModel) {
        return userRepository.findByEmail(userModel.getStr())!=null;
    }

    public Collection<? extends GrantedAuthority> getUserrole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }
}
