package com.example.turnirken.controller;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.Tournament;
import com.example.turnirken.service.TournamentService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/tournaments")
class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {

        this.tournamentService = tournamentService;
    }

    @PostMapping("/createTournament")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament create(@RequestBody CreateTournamentModel model) {
        return tournamentService.create(model);
    }

    @PostMapping("/saveTournamentGrid")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody SaveTourGridModel model) {

        if (tournamentService.tournamentGrid(model.getId())){
            tournamentService.gridSave(model);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } else
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }

    @PostMapping("/getTournamentId")
    public Optional<Tournament> getTourId(@RequestBody GetTourIdModel model) {
        return tournamentService.getTourId((long) model.getId());
    }

    @PostMapping("/goParticipate")
    public ResponseEntity<?> goParticipate(@RequestBody SaveTourGridModel model) {

        if (tournamentService.tournamentParticipant(model.getId())){
            tournamentService.createParticipate(model.getId());
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } else
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }
/*
    @PostMapping("/goParticipate")
    public void goParticipate(@RequestBody GetTourIdModel model){
        tournamentService.createParticipate(model.getId());
    }*/

    @PostMapping("/getParticipants")
    public Set<GetParticipantsModel> getParticipants(@RequestBody GetTourIdModel model){
        return tournamentService.getParticipants(model.getId());
    }

    @PostMapping("/getGroups")
    public Set<GroupModel> getGroups(@RequestBody GetTourIdModel model){
        return tournamentService.getGroups(model.getId());
    }

    @PostMapping("/getMatches")
    public Set<MatchModel> getMatches(@RequestBody GetTourIdModel model){
        return tournamentService.getMatches(model.getId());
    }

    @GetMapping("/getTournaments")
    public List<Tournament> getTour() {
        return tournamentService.getTours();
    }
}
