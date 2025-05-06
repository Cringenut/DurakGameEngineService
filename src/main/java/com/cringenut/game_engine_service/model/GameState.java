package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.dto.TableCardDto;
import com.cringenut.game_engine_service.enums.GamePhase;
import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GameState {

    // Ordered player list
    private List<Player> players;

    // Map of playerId -> their current hand of cards
    private Map<Integer, HashMap<Suit, List<Card>>> playerHands;

    // The main deck
    private Deck deck;

    // Trump suit
    private Suit trumpSuit;

    // Table contains attack/defend card pairs
    private List<TableCardDto> table;

    // Index of player who is attacking and defending
    private int currentAttackerIndex;
    private int currentDefenderIndex;

    // Current phase
    private GamePhase phase;

    // Game over flag and winner
    private boolean gameOver;
    private String winnerId;

    // Optional: move counter / round number
    private int turnNumber;

}
