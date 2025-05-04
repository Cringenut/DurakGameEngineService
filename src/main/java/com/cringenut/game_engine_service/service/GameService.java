package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.feign.DeckManagementService;
import com.cringenut.game_engine_service.feign.SessionService;
import com.cringenut.game_engine_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    SessionService sessionService;

    @Autowired
    DeckManagementService deckManagementService;

    public GameState createGame(Integer sessionId) {
        Session session = sessionService.getSession(sessionId);
        GameSetup gameSetup = deckManagementService.createGame(54, session.getPlayerIds().length);

        System.out.println("Game setup: " + gameSetup.toString());

        return null;
    }
}
