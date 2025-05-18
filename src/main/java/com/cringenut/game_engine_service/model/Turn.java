package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor // Needed for JSON deserialization
public class Turn implements Serializable {

    private Integer id;

    public Turn(Integer attackedId, Integer defenseId, Suit trumpSuit) {
        this.attackedId = attackedId;
        this.defenseId = defenseId;
        this.trumpSuit = trumpSuit;
    }

    private Integer attackedId;
    private Integer defenseId;

    private Suit trumpSuit;

    private final HashMap<Card, Card> tableCards =
            new HashMap<>();


    @Override
    public String toString() {
        return "Turn{" +
                "attackedId=" + attackedId +
                ", defenseId=" + defenseId +
                ", tableCards=" + tableCards +
                '}';
    }
}
