package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "match_results")
public class MatchResult {

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "player_id", nullable = false)
    private AppUser resultCreator;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "datetime")
    private Date date;

    @Column(name = "result_of_player1")
    private int resultPlayer1;

    @Column(name = "result_of_player2")
    private int resultPlayer2;

    @Column(name = "info")
    private String info;
}
