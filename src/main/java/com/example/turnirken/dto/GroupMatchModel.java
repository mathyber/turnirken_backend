package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupMatchModel {
    private Set<SaveGroupAndMatchesModel> groups;
    private Set<SaveGroupAndMatchesModel> matches;

}
