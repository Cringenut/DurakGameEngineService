package com.cringenut.game_engine_service.controller;

import com.cringenut.game_engine_service.model.GameState;
import com.cringenut.game_engine_service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start/{sessionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GameState startGame(
            @PathVariable Integer sessionId) {
        return gameService.createGame(sessionId);
    }

}
