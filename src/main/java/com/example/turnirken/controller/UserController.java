package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.dto.TestRegModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
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

    //@RequestMapping(method = RequestMethod.GET, value = "/greeting")
  //  @GetMapping("greeting")
   // public String getGreeting() {
  //      return mainService.getGreeting();
  //  }

   // @RequestMapping(method = RequestMethod.POST, value = "/registration")
    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser create(@RequestBody CreateUserModel userModel) {
        return userService.create(userModel);
    }

    @PostMapping("registration/testLogin")
    public boolean testLogin(@RequestBody TestRegModel userModel) {
        return userService.testLogin(userModel);
    }

    @PostMapping("registration/testEmail")
    public boolean testEmail(@RequestBody TestRegModel userModel) {
        return userService.testEmail(userModel);
    }

    @GetMapping("/getUsername")
  //  @RequestMapping(value="/login", method = RequestMethod.GET)
    public String getUsername() {
        return userService.getUsername();
    }

    @GetMapping("/getUserroles")
    //  @RequestMapping(value="/login", method = RequestMethod.GET)
    public Collection<? extends GrantedAuthority> getUserroles() {
        return userService.getUserrole();
    }

    @GetMapping("/getUserinfo")
    //  @RequestMapping(value="/login", method = RequestMethod.GET)
    public JSONObject getUserinfo() {
        return userService.getUserinfo();
    }

}
