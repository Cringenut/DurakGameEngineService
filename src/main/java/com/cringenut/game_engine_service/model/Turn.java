package com.cringenut.game_engine_service.model;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Data
@NoArgsConstructor // Needed for JSON deserialization
public class Turn implements Serializable {

    private Integer id;

    public Turn(Integer attackId, Integer defenseId, Suit trumpSuit) {
        this.attackId = attackId;
        this.defenseId = defenseId;
        this.trumpSuit = trumpSuit;
    }

    private Integer attackId;
    private Integer defenseId;

    private Suit trumpSuit;

    private LinkedHashMap<Card, Card> tableCards =
            new LinkedHashMap<>();


    @Override
    public String toString() {
        return "Turn{" +
                "attackedId=" + attackId +
                ", defenseId=" + defenseId +
                ", tableCards=" + tableCards +
                '}';
    }
}
