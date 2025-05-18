package com.cringenut.game_engine_service.controller;

import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.model.Turn;
import com.cringenut.game_engine_service.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
public class GameController {

    @Autowired
    private GameService gameService;

//    @PostMapping
//    @RequestMapping("create/{id}")
//    public Game createGame(@RequestBody Turn turn) {
//        return gameService.createGame(turn);
//    }

    @PostMapping
    @RequestMapping("turn/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Game updateGame(@RequestBody Turn turn) {
        return gameService.updateGame(turn);
    }

}
