package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;

import java.util.Stack;

@Data
public class Deck {

    private Stack<Card> cards = new Stack<>();
    private Suit trumpSuit = Suit.SPADES;

}
