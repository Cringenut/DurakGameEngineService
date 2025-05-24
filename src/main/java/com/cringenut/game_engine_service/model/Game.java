package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.dto.CardDTO;
import com.cringenut.game_engine_service.dto.DeckDTO;
import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
public class Game implements Serializable {

    public Game(Integer id, DeckDTO deck, Suit trumpSuit, ArrayList<Integer> playerIds) {
        this.id = id;
        this.deck = deck;
        this.trumpSuit = trumpSuit;

        for (Integer playerId : playerIds) {
            // Use LinkedHashMap to preserve insertion order (trump first)
            LinkedHashMap<Suit, ArrayList<CardDTO>> suitMap = new LinkedHashMap<>();

            // First add trump suit
            suitMap.put(trumpSuit, new ArrayList<>());

            // Then add the rest of the suits
            for (Suit suit : Suit.values()) {
                if (suit != trumpSuit) {
                    suitMap.put(suit, new ArrayList<>());
                }
            }

            playerHands.put(playerId, suitMap);
        }
    }

    private Integer id;
    private DeckDTO deck;

    private Map<Integer, LinkedHashMap<Suit, ArrayList<CardDTO>>> playerHands
            = new LinkedHashMap<>();
    private Suit trumpSuit;

    private Integer attackId;
    private Integer defenseId;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        sb.append("  \"trumpSuit\": \"").append(trumpSuit).append("\",\n");

        sb.append("  \"deck\": ").append(deck != null ? deck.getCardDTOS().toString() : "null").append(",\n");

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
