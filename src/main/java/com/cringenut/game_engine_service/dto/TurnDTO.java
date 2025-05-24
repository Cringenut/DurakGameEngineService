package com.cringenut.game_engine_service.dto;

import com.cringenut.game_engine_service.enums.Suit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Data
@NoArgsConstructor // Needed for JSON deserialization
public class TurnDTO implements Serializable {

    private Integer id;

    public TurnDTO(Integer attackId, Integer defenseId, Suit trumpSuit) {
        this.attackId = attackId;
        this.defenseId = defenseId;
        this.trumpSuit = trumpSuit;
    }

    private Integer attackId;
    private Integer defenseId;

    private Suit trumpSuit;

    // A pair of attacking card and defending card
    private LinkedHashMap<CardDTO, CardDTO> tableCards =
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
