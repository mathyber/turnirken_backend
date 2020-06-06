package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TournamentModel {

    private int id;

    private int idOrg;

    private String name;

    private String season;

    private String game;

}
