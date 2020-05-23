package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserModel {

    private String login;

    private String email;

    private String password;
}
