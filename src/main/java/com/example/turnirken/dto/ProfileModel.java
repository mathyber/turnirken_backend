package com.example.turnirken.dto;

import com.example.turnirken.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProfileModel {
    private int id;
    private String login;
    private String email;
    private Set<Role> roles;
    private int numPs;
    private int numOrg;
    private int numW;
    private int num2;
    private int num3;
    private String bestGame;
    private List<MatchResModel> matches;
    private List<TournamentForPageModel> tournaments;

}
