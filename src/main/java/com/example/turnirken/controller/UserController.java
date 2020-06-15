package com.example.turnirken.controller;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.service.UserService;
import com.sun.net.httpserver.Authenticator;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/user")
class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateUserModel userModel) {
        userService.create(userModel);
    }

    @PostMapping("newPassword")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody NewPasswordModel model) {
        if(userService.newPassword(model)) return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        else return new ResponseEntity<Error>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("newEmail")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody NewEmailModel model) {
        if(userService.newEmail(model)) return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        else return new ResponseEntity<Error>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("registration/testLogin")
    public boolean testLogin(@RequestBody TestRegModel userModel) {
        return userService.testLogin(userModel);
    }

    @PostMapping("registration/testEmail")
    public boolean testEmail(@RequestBody TestRegModel userModel) {
        return userService.testEmail(userModel);
    }

    @PostMapping("/setRole")
    public void setRole(@RequestBody RoleModel model) {
        userService.setRole(model);
    }

    @GetMapping("/getUsername")
    public String getUsername() {
        return userService.getUsername();
    }

    @GetMapping("/getUserroles")
    public Collection<? extends GrantedAuthority> getUserroles() {
        return userService.getUserrole();
    }

    @GetMapping("/getUserinfo")
    public JSONObject getUserinfo() {
        return userService.getUserinfo();
    }

    @PostMapping("/getProfile")
    public ProfileModel getProlile(@RequestBody GetTourIdModel model) {
        return userService.getProfile(model);
    }

}
