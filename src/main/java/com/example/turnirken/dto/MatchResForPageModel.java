package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MatchResForPageModel {
    private Long id;
    private int resPlayer1;
    private int resPlayer2;
    private int resCreator;
    private boolean finish;
    private String info;
    private Date date;
}
