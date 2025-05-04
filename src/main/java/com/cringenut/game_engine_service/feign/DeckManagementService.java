package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.model.GameSetup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("DECK-MANAGEMENT-SERVICE")
public interface DeckManagementService {
    @PostMapping("deck")
    @ResponseStatus(HttpStatus.CREATED)
    public GameSetup createGame(
            @RequestParam Integer size,
            @RequestParam Integer playerAmount);

}
