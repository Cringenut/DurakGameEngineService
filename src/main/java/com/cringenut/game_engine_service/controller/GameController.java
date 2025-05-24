package com.cringenut.game_engine_service.controller;

import com.cringenut.game_engine_service.dto.DeckDTO;
import com.cringenut.game_engine_service.dto.LobbyDTO;
import com.cringenut.game_engine_service.feign.DeckManagementClient;
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

    @Autowired
    private DeckManagementClient deckManagementClient;

    @PostMapping
    @RequestMapping("/{id}")
    public Game createGame(@PathVariable Integer id) {
        LobbyDTO lobby = lobbyClient.getLobby(id);
        if (lobby == null)
            return null;

        DeckDTO initialDeck = deckManagementClient.createDeck(52, lobby.getPlayerIds().length);
        if (initialDeck == null)
            return null;

        Game game = gameService.createGame(lobby, initialDeck);

        return game;
    }

    @PostMapping
    @RequestMapping("turn/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Game updateGame(@RequestBody TurnDTO turn) {
        return gameService.updateGame(turn);
    }

}
