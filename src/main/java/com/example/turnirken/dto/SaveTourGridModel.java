package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.Set;

@Getter
@Setter
public class SaveTourGridModel {

    private String grid;
    private Set<GridElemementModel> users;
    private Set<GridElemementModel> results;
    private Set<GridElemementModel> groups;
    private Set<GridElemementModel> matches;
    private int id;
}
