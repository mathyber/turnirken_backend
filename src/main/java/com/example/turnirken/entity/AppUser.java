package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Column(name = "login")
    String login;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;
}
