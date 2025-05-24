package com.cringenut.game_engine_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LobbyDTO {

    private Integer id;
    private Integer[] playerIds;

}
