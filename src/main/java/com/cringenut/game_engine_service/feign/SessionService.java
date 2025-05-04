package com.cringenut.game_engine_service.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("DECK-MANAGEMENT-SERVICE")
public interface SessionService {
}
