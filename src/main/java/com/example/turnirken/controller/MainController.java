package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    //@RequestMapping(method = RequestMethod.GET, value = "/greeting")
    @GetMapping("greeting")
    public String getGreeting() {
        return mainService.getGreeting();
    }

   // @RequestMapping(method = RequestMethod.POST, value = "/registration")
    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser create(@RequestBody CreateUserModel userModel) {

        return mainService.create(userModel);
    }

    @GetMapping("/getUsername")
  //  @RequestMapping(value="/login", method = RequestMethod.GET)
    public String getUsername() {
        return mainService.getUsername();
    }
}
