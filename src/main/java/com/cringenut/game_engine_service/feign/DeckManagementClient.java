package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.config.FeignConfig;
import com.cringenut.game_engine_service.dto.DeckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "DECK-MANAGEMENT-SERVICE", configuration = FeignConfig.class)
public interface DeckManagementClient {

    @PostMapping("deck")
    @ResponseStatus(HttpStatus.CREATED)
    DeckDTO createDeck(
            @RequestParam Integer size,
            @RequestParam Integer playerAmount);

}
