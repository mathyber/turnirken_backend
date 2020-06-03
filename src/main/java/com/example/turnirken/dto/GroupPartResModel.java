package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupPartResModel {
    private ParticipantsModel Part;
    private int Wins;
    private int WinPoints;
    private int Draw;
    private int Losing;
    private int LosingPoints;
    private int Points;
    private int Place;
    private boolean win;

}
