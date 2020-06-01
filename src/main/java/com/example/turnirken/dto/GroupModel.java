package com.example.turnirken.dto;

import com.example.turnirken.entity.Next;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupModel {
    private Long id;
    private String name;
    private int numberOfPlayers;
    private int numberOfPlayersPlayoff;
    private int pointsWin;
    private int pointsDraw;
    private Set<GetParticipantsModel> participants;
    private Set<Next> nexts;
    private Set<Next> lasts;
}
