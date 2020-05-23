package com.example.turnirken.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.service.MainService;

@RestController
@RequestMapping("main")
class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("greeting")
    public String getGreeting() {

        return mainService.getGreeting();
    }

    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser create(@RequestBody CreateUserModel userModel) {

        return mainService.create(userModel);
    }
}
