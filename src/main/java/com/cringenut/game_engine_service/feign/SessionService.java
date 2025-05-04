package com.cringenut.game_engine_service.feign;

import com.cringenut.game_engine_service.model.Session;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("SESSION-SERVICE")
public interface SessionService {
    @GetMapping("session/{sessionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Session getSession(@PathVariable Integer sessionId);

}
