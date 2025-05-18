package com.cringenut.game_engine_service.controller;

import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.model.Turn;
import com.cringenut.game_engine_service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    @RequestMapping("turn/{id}")
    public Game updateGame(@RequestBody Turn turn) {
        return gameService.updateGame(turn);
    }

}
