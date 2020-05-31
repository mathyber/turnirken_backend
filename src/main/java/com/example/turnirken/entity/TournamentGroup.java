package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tournament_groups")
public class TournamentGroup {

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_players")
    private int numberOfPlayers;

    @Column(name = "number_of_players_playoff")
    private int numberOfPlayersPlayoff;

    @Column(name = "points_for_victory")
    private int pointsWin;

    @Column(name = "points_for_draw")
    private int pointsDraw;

}
