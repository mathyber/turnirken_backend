package com.example.turnirken.dto;

import com.example.turnirken.entity.Next;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateResultMatchModel {
    private Long idMatch;
    private int meRes;
    private int player2Res;
    private boolean finish;
    private String info;
}
