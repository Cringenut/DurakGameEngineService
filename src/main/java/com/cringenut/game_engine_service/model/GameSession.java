package com.cringenut.game_engine_service.model;

import lombok.Data;

import java.util.List;

@Data
public class GameSession {

    private List<Player> players;
    private Deck deck;

}
