package com.example.turnirken.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupResModel {
    private Long idGroup;
    private String groupName;
    private Set<GroupPartResModel> results;
    private boolean finish;
}
