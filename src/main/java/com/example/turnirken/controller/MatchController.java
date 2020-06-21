package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateResultMatchModel;
import com.example.turnirken.dto.GetTourIdModel;
import com.example.turnirken.dto.MatchResModel;
import com.example.turnirken.service.MatchService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/getAllMatchesTournament/{id}")
    public ArrayList<MatchResModel> getAllMatchesTournament(@PathVariable String id){
        return matchService.getAllMatchesTournament(Integer.parseInt(id));
    }

    @PostMapping("/getMatchesGroup")
    public ArrayList<MatchResModel> getMatchesGroup(@RequestBody GetTourIdModel model){
        return matchService.getMatchesGroup(model.getId());
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
