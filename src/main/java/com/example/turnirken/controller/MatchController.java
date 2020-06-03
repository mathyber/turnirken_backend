package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateResultMatchModel;
import com.example.turnirken.dto.GetTourIdModel;
import com.example.turnirken.dto.MatchResModel;
import com.example.turnirken.dto.SaveTourGridModel;
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
@RequestMapping("api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/getMatchesForUser")
    public Set<MatchResModel> getMatchesForUser(@RequestBody GetTourIdModel model){
        return matchService.getMatchesForUser(model.getId());
    }

    @PostMapping("/getAllMatchesTournament")
    public Set<MatchResModel> getAllMatchesTournament(@RequestBody GetTourIdModel model){
        return matchService.getAllMatchesTournament(model.getId());
    }

    @PostMapping("/getMatchById")
    public MatchResModel getMatch(@RequestBody GetTourIdModel model){
        return matchService.getMatchResult(model.getId());
    }

    @PostMapping("/setResult")
    public ResponseEntity<?> goParticipate(@RequestBody CreateResultMatchModel model) {
        if (matchService.creatorResult(model.getIdMatch().intValue())){
            matchService.setResultMatch(model);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } else
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }
}
