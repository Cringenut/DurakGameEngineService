package com.cringenut.game_engine_service;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.model.Card;
import com.cringenut.game_engine_service.model.Deck;
import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.model.Turn;
import com.cringenut.game_engine_service.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService gameService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testPutAndGetStringFromCache() {
        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();

        String key = "myKey";
        String value = "Hello Redis!";

        cache.put(key, value);

        String cachedValue = cache.get(key, String.class);

        assertThat(cachedValue).isEqualTo(value);
    }

    @Test
    public void testPutAndGetGameFromCache() {
        Game game = new Game();
        game.setId(2);

        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();
        cache.put(2, game);

        Game cachedGame = cache.get(2, Game.class);

        assertThat(cachedGame).isNotNull();
        assertThat(cachedGame.getId()).isEqualTo(2);
    }

    @Test
    void updateGame_defenseLost_cardsAdded() {
        Turn turn = new Turn();
        turn.setId(2);
        turn.setDefenseId(5);
        Card attack = new Card(Rank.NINE, Suit.HEARTS);
        turn.getTableCards().put(attack, null);

        LinkedHashMap<Suit, ArrayList<Card>> hand = new LinkedHashMap<>();
        hand.put(Suit.HEARTS, new ArrayList<>());

        Map<Integer, LinkedHashMap<Suit, ArrayList<Card>>> hands = new HashMap<>();
        hands.put(5, hand);

        Game game = new Game();
        game.setId(2);
        game.setPlayerHands(hands);

        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();
        cache.put(2, game);

        Game result = gameService.updateGame(turn);

        assertEquals(1, result.getPlayerHands().get(5).get(Suit.HEARTS).size());
        assertTrue(result.getPlayerHands().get(5).get(Suit.HEARTS).contains(attack));
    }

    @Test
    void testGameJacksonSerialization() throws Exception {
        Game game = new Game();
        game.setId(42);
        game.setTrumpSuit(Suit.HEARTS);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(game);
        System.out.println(json);
    }

    @Test
    void updateGame_gameMissingFromCache_shouldThrow() {
        Turn turn = new Turn();
        turn.setId(999);

        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();
        cache.evict(999); // Ensure it’s missing

        assertThrows(NullPointerException.class, () -> gameService.updateGame(turn));
    }
}
