package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
public class GroupResModel {
    private Long idGroup;
    private Long idTour;
    private String groupName;
    private ArrayList<GroupPartResModel> results;
    private boolean finish;
}
