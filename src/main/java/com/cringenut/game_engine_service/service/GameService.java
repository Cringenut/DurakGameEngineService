package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.feign.SessionService;
import com.cringenut.game_engine_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    SessionService sessionService;

    public GameState createGame(Integer sessionId) {
        Session session = sessionService.getSession(sessionId);
        System.out.println("Retrieved session: " + session.toString());

        return null;
    }
}
