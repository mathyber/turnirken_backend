package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateResultMatchModel;
import com.example.turnirken.dto.GetTourIdModel;
import com.example.turnirken.dto.GroupResModel;
import com.example.turnirken.dto.MatchResModel;
import com.example.turnirken.service.GroupService;
import com.example.turnirken.service.MatchService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    private final MatchService matchService;
    private final GroupService groupService;

    public GroupController(MatchService matchService, GroupService groupService) {
        this.matchService = matchService;
        this.groupService = groupService;
    }

    @PostMapping("/getGroupsTour")
    public Set<GroupResModel> getGroupsTour(@RequestBody GetTourIdModel model){
        return groupService.getGroupsTour(model.getId());
    }

    @PostMapping("/getGroup")
    public GroupResModel getGroup(@RequestBody GetTourIdModel model){
        return groupService.getGroup(model.getId());
    }
/*
    @PostMapping("/setResult")
    public ResponseEntity<?> goParticipate(@RequestBody CreateResultMatchModel model) {
        if (matchService.creatorResult(model.getIdMatch().intValue())){
            matchService.setResultMatch(model);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } else
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }*/
}
