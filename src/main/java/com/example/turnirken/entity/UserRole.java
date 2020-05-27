package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class UserRole {

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;
}
