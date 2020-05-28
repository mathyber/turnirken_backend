package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game")
    private Set<TournamentName> tournamentNames;

}
