package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MatchResModel {
    private Long id;
    private String playoffStage;
    private TournamentModel tournament;
    private int round;
    private String groupName;
    private int groupId;
    private GetParticipantsModel player1;
    private GetParticipantsModel player2;
    private int resPlayer1;
    private int resPlayer2;
    private boolean finish;
}
