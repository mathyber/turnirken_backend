package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GridElemementModel {
    private String id;
    private Set<GridElementLinkModel> linksin;
    private Set<GridElementLinkModel> linksout;
    private String name;
    private String stage;
    private String place;
}
