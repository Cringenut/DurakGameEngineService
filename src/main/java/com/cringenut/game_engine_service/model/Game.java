package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Game {

    private Deck deck;
    private List<HashMap<Suit, List<Card>>> playerHands;
    private Suit trumpSuit;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        sb.append("  \"trumpSuit\": \"").append(trumpSuit).append("\",\n");

        sb.append("  \"deck\": ").append(deck != null ? deck.getCards().toString() : "null").append(",\n");

        sb.append("  \"playerHands\": [\n");
        if (playerHands != null) {
            for (int i = 0; i < playerHands.size(); i++) {
                sb.append("    ").append(playerHands.get(i));
                if (i < playerHands.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
        }
        sb.append("  ]\n");

        sb.append("}");

        return sb.toString();
    }

}
