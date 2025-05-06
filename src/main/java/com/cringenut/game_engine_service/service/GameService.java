package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.feign.DeckManagementService;
import com.cringenut.game_engine_service.feign.SessionService;
import com.cringenut.game_engine_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    @Autowired
    SessionService sessionService;

    @Autowired
    DeckManagementService deckManagementService;

    public GameState createGame(Integer sessionId) {
        GameState gameState = new GameState();

        Session session = sessionService.getSession(sessionId);
        GameSetup gameSetup = deckManagementService.createGame(54, session.getPlayerIds().length);

        Map<Integer, HashMap<Suit, List<Card>>> playerHands = new HashMap<>();
        List<Integer> playerIds = List.of(session.getPlayerIds());
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < playerIds.size(); i++) {
            HashMap<Suit, List<Card>> playerHand =
                    gameSetup.getPlayerHands().get(i);
            playerHands.put(playerIds.get(i), playerHand);
            players.add(new Player(playerIds.get(i)));
        }

        gameState.setPlayers(players); // Replace with Dto later
        gameState.setPlayerHands(playerHands);
        gameState.setDeck(gameSetup.getDeck());
        gameState.setTrumpSuit(gameSetup.getTrumpSuit());

        return gameState;
    }

}
