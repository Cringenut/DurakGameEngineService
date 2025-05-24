package com.cringenut.game_engine_service.controller;

import com.cringenut.game_engine_service.feign.LobbyClient;
import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.dto.TurnDTO;
import com.cringenut.game_engine_service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyClient lobbyClient;

    @PostMapping
    @RequestMapping("/{id}")
    public Game createGame(@PathVariable Integer id) {
        lobbyClient.getLobby(id);
        return gameService.createGame(null);
    }

    @PostMapping
    @RequestMapping("turn/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Game updateGame(@RequestBody TurnDTO turn) {
        return gameService.updateGame(turn);
    }

}
