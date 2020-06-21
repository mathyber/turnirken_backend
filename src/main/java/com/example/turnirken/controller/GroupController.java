package com.example.turnirken.controller;

import com.example.turnirken.dto.*;
import com.example.turnirken.repository.TournamentGroupRepository;
import com.example.turnirken.service.GroupService;
import com.example.turnirken.service.MatchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    private final MatchService matchService;
    private final GroupService groupService;
    private final TournamentGroupRepository tournamentGroupRepository;

    public GroupController(MatchService matchService, GroupService groupService, TournamentGroupRepository tournamentGroupRepository) {
        this.matchService = matchService;
        this.groupService = groupService;
        this.tournamentGroupRepository = tournamentGroupRepository;
    }
/*
    @PostMapping("/getGroupMatchesCreate")
    public void createGroupMatches(@RequestBody GetTourIdModel model){
        groupService.createGroupMatches(tournamentGroupRepository.findById((long)model.getId()).get());
    }*/

    @PostMapping("/getGroupsTour")
    public ArrayList<GroupResModel> getGroupsTour(@RequestBody GetTourIdModel model){
        return groupService.getGroupsTour(model.getId());
    }

    @PostMapping("/getGroup")
    public GroupResModel getGroup(@RequestBody GetTourIdModel model){
        return groupService.getGroup(model.getId());
    }

    @PostMapping("/getGroupsPoints")
    public void getGroupsPoints(@RequestBody GroupPointsModel model){
        groupService.getGroupsPoints(model);
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
