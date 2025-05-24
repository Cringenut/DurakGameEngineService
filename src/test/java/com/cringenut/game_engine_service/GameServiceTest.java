package com.cringenut.game_engine_service;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.dto.CardDTO;
import com.cringenut.game_engine_service.dto.DeckDTO;
import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.dto.TurnDTO;
import com.cringenut.game_engine_service.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService gameService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testDealCardsToPlayers_shouldDealUpToSixCardsEach() {
        Game game = new Game();
        DeckDTO deck = new DeckDTO();

        // Create 20 cards in deck
        Stack<CardDTO> cards = new Stack<>();
        for (int i = 0; i < 20; i++) {
            cards.add(new CardDTO(Rank.values()[i % Rank.values().length], Suit.HEARTS));
        }
        deck.setCards(cards);
        game.setDeck(deck);

        // Setup players
        LinkedHashMap<Suit, ArrayList<CardDTO>> hand1 = new LinkedHashMap<>();
        LinkedHashMap<Suit, ArrayList<CardDTO>> hand2 = new LinkedHashMap<>();
        Map<Integer, LinkedHashMap<Suit, ArrayList<CardDTO>>> playerHands = new HashMap<>();
        playerHands.put(1, hand1);
        playerHands.put(2, hand2);
        game.setPlayerHands(playerHands);

        GameService gameService = new GameService();
        gameService.dealCardsToPlayers(game, 1, 2);

        int totalCards1 = gameService.getTotalCardsForPlayer(game, 1);
        int totalCards2 = gameService.getTotalCardsForPlayer(game, 2);

        assertEquals(6, totalCards1, "Player 1 should have 6 cards");
        assertEquals(6, totalCards2, "Player 2 should have 6 cards");
    }

    @Test
    public void testApplyTurnToGame_defenseWins_noCardsReturned() {
        // Setup game
        Game game = new Game();
        LinkedHashMap<Suit, ArrayList<CardDTO>> defenseHand = new LinkedHashMap<>();
        Map<Integer, LinkedHashMap<Suit, ArrayList<CardDTO>>> playerHands = new HashMap<>();
        playerHands.put(2, defenseHand);
        game.setPlayerHands(playerHands);
        game.setDeck(new DeckDTO());

        // Create Turn where defense defended all cards
        TurnDTO turn = new TurnDTO();
        turn.setId(1);
        turn.setDefenseId(2);

        LinkedHashMap<CardDTO, CardDTO> tableCards = new LinkedHashMap<>();
        tableCards.put(new CardDTO(Rank.TEN, Suit.CLUBS), new CardDTO(Rank.JACK, Suit.CLUBS)); // defended
        tableCards.put(new CardDTO(Rank.TWO, Suit.SPADES), new CardDTO(Rank.THREE, Suit.SPADES)); // defended
        turn.setTableCards(tableCards);

        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();
        cache.put(1, game);

        Game result = gameService.updateGame(turn);

        // Defense hand should still be empty
        assertEquals(0, result.getPlayerHands().get(2).values().stream().mapToInt(List::size).sum());
    }

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
        TurnDTO turn = new TurnDTO();
        turn.setId(2);
        turn.setDefenseId(5);
        CardDTO attack = new CardDTO(Rank.NINE, Suit.HEARTS);
        turn.getTableCards().put(attack, null);

        LinkedHashMap<Suit, ArrayList<CardDTO>> hand = new LinkedHashMap<>();
        hand.put(Suit.HEARTS, new ArrayList<>());

        Map<Integer, LinkedHashMap<Suit, ArrayList<CardDTO>>> hands = new HashMap<>();
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
        TurnDTO turn = new TurnDTO();
        turn.setId(999);

        Cache cache = cacheManager.getCache("GAME_CACHE");
        assertThat(cache).isNotNull();
        cache.evict(999); // Ensure it’s missing

        assertThrows(NullPointerException.class, () -> gameService.updateGame(turn));
    }
}
