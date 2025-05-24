package com.cringenut.game_engine_service.config;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.dto.CardDTO;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class CardKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.split("_");
        if (parts.length != 2) {
            throw new IOException("Invalid Card key format: " + key);
        }
        return new CardDTO(Rank.valueOf(parts[0]), Suit.valueOf(parts[1]));
    }
}
