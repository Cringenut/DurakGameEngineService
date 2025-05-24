package com.cringenut.game_engine_service.dto;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;

import java.util.Stack;

@Data
public class DeckDTO {

    private Stack<CardDTO> cards = new Stack<>();
    private Suit trumpSuit = Suit.SPADES;

}
