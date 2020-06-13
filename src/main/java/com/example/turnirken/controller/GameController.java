package com.example.turnirken.controller;

import com.example.turnirken.dto.CreateGameModel;
import com.example.turnirken.entity.Game;
import com.example.turnirken.repository.GameRepository;
import com.example.turnirken.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
class GameController {
    private final GameService gameService;
    private final GameRepository gameRepository;
    public GameController(GameService gameService, GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/createGame")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody CreateGameModel model) {
        Game game = new Game();
        game.setName(model.getName());
        game.setInfo(model.getInfo());
       // game.setOnDisplay(false);

        return gameService.create(game);
    }

    @PostMapping("/moderator/createOnDisplayGame")
    @ResponseStatus(HttpStatus.CREATED)
    public Game createOnDisplay(@RequestBody CreateGameModel model) {
        Game game = new Game();
        game.setName(model.getName());
        game.setInfo(model.getInfo());
        //game.setOnDisplay(true);

        return gameService.create(game);
    }

    @PostMapping("/moderator/setOnDisplayGame")
    @ResponseStatus(HttpStatus.CREATED)
    public Game setOnDisplay(@RequestBody CreateGameModel model) {
        Game game = gameRepository.findByName(model.getName());
      //  game.setOnDisplay(true);
        return gameService.create(game);
    }
}
