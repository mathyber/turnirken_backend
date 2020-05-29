package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateTournamentModel;
import com.example.turnirken.entity.Tournament;
import com.example.turnirken.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getTournaments")
    public List<Tournament> getTour() {
        return tournamentService.getTours();
    }
}
