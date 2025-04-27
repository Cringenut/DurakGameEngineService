package com.cringenut.game_engine_service.model;

import com.cringenut.session_service.enums.Suit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Deck {

    private List<Card> cards = new ArrayList<>();
    private Suit trumpSuit = Suit.SPADES;

}
