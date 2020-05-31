package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateTournamentModel;
import com.example.turnirken.dto.GetTourIdModel;
import com.example.turnirken.dto.SaveTourGridModel;
import com.example.turnirken.entity.Tournament;
import com.example.turnirken.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public void create(@RequestBody SaveTourGridModel model) {
        tournamentService.gridSave(model);
    }

    @PostMapping("/getTournamentId")
    public Optional<Tournament> getTourId(@RequestBody GetTourIdModel model) {
        return tournamentService.getTourId((long) model.getId());
    }

    @GetMapping("/getTournaments")
    public List<Tournament> getTour() {
        return tournamentService.getTours();
    }
}
