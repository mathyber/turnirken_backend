package com.example.turnirken.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class Tournament {

    @Column(name = "id", updatable = false, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_sequence")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "name_id", nullable = false)
    private TournamentName tournamentName;

    @Column(name = "season")
    private String season;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "organizer_id", nullable = false)
    private AppUser organizer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tournament_system_id", nullable = false)
    private TournamentSystem tournamentSystem;

    @Column(name = "date_start_reg")
    private Date dateStartReg;

    @Column(name = "date_finish_reg")
    private Date dateFinishReg;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_finish")
    private Date dateFinish;

    @Column(name = "max_participants")
    private int maxParticipants;

    @Column(name = "games_num")
    private int gamesNum;

    @Column(name = "num_to_win")
    private int numToWin;

    @Column(name = "only_admin_result")
    private boolean onlyAdminResult;

    @Column(name = "status")
    private boolean status;

    @Column(name = "info")
    private String info;

    @Column(name = "logo")
    private String logo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private Set<TournamentGroup> groups;
}
