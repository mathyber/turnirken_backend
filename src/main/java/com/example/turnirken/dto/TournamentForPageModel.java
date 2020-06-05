package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
public class TournamentForPageModel {

    private Long id;

    private String tournamentName;

    private String gameName;

    private String season;

    private GetParticipantsModel organizer;

    private Date dateStartReg;

    private Date dateFinishReg;

    private Date dateStart;

    private Date dateFinish;

    private int maxParticipants;

    private int participants;

    private int game;

    private int numToWin;

    private boolean onlyAdminResult;

    private boolean status;

    private String info;

    private String logo;

    private String grid;

    private boolean userReg;

}
