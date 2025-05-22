package com.cringenut.game_engine_service.model;

import lombok.Data;

@Data
public class Lobby {

    private Integer ownerId;
    private Integer[] playerIds;

}
