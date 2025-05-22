package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.model.Lobby;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("LOBBY-SERVICE")
public interface LobbyClient {

    @GetMapping("/{lobbyId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Lobby getLobby(@PathVariable Integer lobbyId);

    @DeleteMapping("/{lobbyId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable Integer lobbyId);

}
