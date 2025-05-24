package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.config.FeignConfig;
import com.cringenut.game_engine_service.dto.LobbyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "LOBBY-SERVICE", configuration = FeignConfig.class)
public interface LobbyClient {
    @GetMapping("lobby/{lobbyId}")
    LobbyDTO getLobby(@PathVariable Integer lobbyId);

    @DeleteMapping("lobby/{lobbyId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable Integer lobbyId);

}
