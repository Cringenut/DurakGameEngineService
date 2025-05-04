package com.cringenut.game_engine_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("DECK-MANAGEMENT-SERVICE")
public interface DeckManagementService {

    @PostMapping
    public ResponseEntity<GameSetup> generateGame(
            @RequestParam Integer size,
            @RequestParam Integer playerAmount);

}
