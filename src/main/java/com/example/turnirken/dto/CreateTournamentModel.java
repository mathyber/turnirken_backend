package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
public class CreateTournamentModel {

    private String name;

    private String season;

    private String game;

    private String logo;

    private int maxParticipants;

    private boolean onlyAdminResult;

  //  private int numToWin;

    private Date dateStartReg;

    private Date dateFinishReg;

  //  private Date dateStart;

  //  private Date dateFinish;

    private String info;

}
