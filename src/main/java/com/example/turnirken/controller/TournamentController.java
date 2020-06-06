package com.example.turnirken.controller;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.Tournament;
import com.example.turnirken.service.TournamentService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> saveTourGr(@RequestBody SaveTourGridModel model) {

            if(tournamentService.gridSave(model)) return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
            else return new ResponseEntity<Error>(HttpStatus.CONFLICT);

    }

    @PostMapping("/getTournamentId")
    public TournamentForPageModel getTourId(@RequestBody GetTourIdModel model) {
        return tournamentService.getTourId((long) model.getId());
    }

    @PostMapping("/goParticipate")
    public ResponseEntity<?> goParticipate(@RequestBody SaveTourGridModel model) {

        if (tournamentService.tournamentParticipant(model.getId())){
            if (tournamentService.createParticipate(model.getId()))
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
            else return new ResponseEntity<Error>(HttpStatus.CONFLICT);

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
        return tournamentService.getMatches(model.getId(),true);
       // return tournamentService.getMatches(model.getId(),false);
    }

    @PostMapping("/setMatchesAndGroups")
    public ResponseEntity<?> getMatchesAndGroups(@RequestBody GroupMatchModel model){
        try {
            tournamentService.saveGroups(model.getGroups());
            tournamentService.saveMatches(model.getMatches());
            tournamentService.setDateStart(model);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);}
    }

    @PostMapping("/searchTournaments")
    public List<TournamentForPageModel> searchTournaments(@RequestBody SearchTournamentsModel model) {
        return tournamentService.searchTournaments(model);
    }

    @PostMapping("/searchTournamentsNameGame")
    public List<TournamentForPageModel> searchTournamentsNameGame(@RequestBody SearchTournamentsModel model) {
        return tournamentService.searchTournamentsGameName(model);
    }

    @GetMapping("/getTournaments")
    public List<TournamentForPageModel> getTour() {
        return tournamentService.getTours();
    }
}
