package com.example.turnirken.entity;

import com.example.turnirken.entity.enums.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {

    @Column(name = "name")
    private String name;

    @Column(name = "onDisplay")
    private boolean onDisplay;

    @Column(name = "info")
    private String info;

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;

}
