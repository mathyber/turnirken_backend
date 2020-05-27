package com.example.turnirken.service;

import com.example.turnirken.dto.CreateGameModel;
import com.example.turnirken.entity.Game;
import com.example.turnirken.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository ) {
        this.gameRepository = gameRepository;
    }

    public Game create(Game game) {
        return gameRepository.save(game);
    }
}
