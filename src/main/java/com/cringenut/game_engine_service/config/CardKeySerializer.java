package com.cringenut.game_engine_service.config;

import com.cringenut.game_engine_service.dto.CardDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CardKeySerializer extends JsonSerializer<CardDTO> {
    @Override
    public void serialize(CardDTO card, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String key = card.getRank().name() + "_" + card.getSuit().name(); // e.g. "ACE_HEARTS"
        gen.writeFieldName(key);
    }
}
