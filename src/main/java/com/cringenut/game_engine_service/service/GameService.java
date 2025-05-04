package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.enums.GamePhase;
import com.cringenut.game_engine_service.feign.DeckManagementService;
import com.cringenut.game_engine_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameService {

    @Autowired
    DeckManagementService deckManagementService;

    public GameState createGame(Integer sessionId) {
        return null;
    }
}
