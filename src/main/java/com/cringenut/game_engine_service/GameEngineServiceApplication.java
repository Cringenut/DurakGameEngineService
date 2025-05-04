package com.cringenut.game_engine_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GameEngineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameEngineServiceApplication.class, args);
	}

}
