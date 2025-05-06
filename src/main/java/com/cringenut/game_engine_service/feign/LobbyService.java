package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.model.Lobby;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("LOBBY-SERVICE")
public interface LobbyService {
    @GetMapping("lobby/{lobbyId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Lobby getLobby(@PathVariable Integer lobbyId);

}
