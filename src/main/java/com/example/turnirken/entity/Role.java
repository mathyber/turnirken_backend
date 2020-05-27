package com.example.turnirken.entity;

import com.example.turnirken.entity.enums.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "app_roles")
public class Role {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum name;

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;

}
