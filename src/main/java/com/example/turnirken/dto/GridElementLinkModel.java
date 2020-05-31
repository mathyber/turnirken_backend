package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GridElementLinkModel {
    private String id;
    private String type;
    private int place;
    private boolean win;
}
