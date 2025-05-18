package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;

import java.io.Serializable;

@Data
public class Card implements Serializable {

    // Needed for JSON deserialization
    public Card() {
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    private Rank rank;
    private Suit suit;

    @Override
    public String toString() {
        return (rank.getValue() + suit.getSymbol());
    }

}
