package com.example.turnirken.dto;

import com.example.turnirken.entity.NextType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEntityModel {
    private String idFromModel;
    private NextType type;
    private Long id;
}
