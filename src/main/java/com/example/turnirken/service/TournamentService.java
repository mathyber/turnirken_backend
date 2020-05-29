package com.example.turnirken.service;

import com.example.turnirken.dto.CreateTournamentModel;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.GameRepository;
import com.example.turnirken.repository.TournamentRepository;
import com.example.turnirken.repository.TournamentSystemRepository;
import com.example.turnirken.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {
    private TournamentRepository tournamentRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private TournamentSystemRepository tournamentSystemRepository;

    public TournamentService(TournamentRepository tournamentRepository, GameRepository gameRepository, UserRepository userRepository, TournamentSystemRepository tournamentSystemRepository){
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.tournamentSystemRepository = tournamentSystemRepository;
    }

    public Tournament create(CreateTournamentModel model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        TournamentName name = new TournamentName();
        name.setName(model.getName());
        name.setCreator(user);

        Game game = gameRepository.findByName(model.getGame());
        name.setGame(game);

        Tournament t = new Tournament();
        t.setTournamentName(name);
        t.setSeason(model.getSeason());
        t.setOrganizer(user);
        t.setLogo(model.getLogo());
        t.setMaxParticipants(model.getMaxParticipants());
        t.setDateFinish(model.getDateFinish());
        t.setDateStart(model.getDateStart());
        t.setDateFinishReg(model.getDateFinishReg());
        t.setDateStartReg(model.getDateStartReg());
        t.setNumToWin(model.getNumToWin());
        t.setOnlyAdminResult(model.isOnlyAdminResult());
        t.setInfo(model.getInfo());
        //zaglushki
        TournamentSystem ts = tournamentSystemRepository.findById(1L).get();
        t.setTournamentSystem(ts);

        return tournamentRepository.save(t);
    }

    public List<Tournament> getTours() {
        return tournamentRepository.findAll();
    }
}
