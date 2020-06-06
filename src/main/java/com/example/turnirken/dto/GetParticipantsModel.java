package com.example.turnirken.dto;

import com.example.turnirken.entity.NextType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetParticipantsModel {
    private String login;
    private String info;
    private Long user_id;
    private Long id;
}
