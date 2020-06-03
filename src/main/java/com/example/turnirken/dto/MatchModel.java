package com.example.turnirken.dto;

import com.example.turnirken.entity.Next;
import com.example.turnirken.entity.Round;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MatchModel {
    private Long id;
    private Long id_playoff;
    private int round;
    private Long id_group;
    private String string;
    private Set<GetParticipantsModel> participants;
    private Set<Next> nexts;
    private Set<Next> lasts;
}
