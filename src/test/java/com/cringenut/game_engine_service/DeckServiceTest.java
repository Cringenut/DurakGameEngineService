package com.cringenut.game_engine_service;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.feign.DeckManagementService;
import com.cringenut.game_engine_service.feign.LobbyService;
import com.cringenut.game_engine_service.model.*;
import com.cringenut.game_engine_service.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class GameServiceTest {

    @Mock
    private LobbyService lobbyService;

    @Mock
    private DeckManagementService deckManagementService;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_shouldInitializeGameStateCorrectly() {
        // Given
        Integer sessionId = 1;
        Integer[] playerIds = {101, 102};

        Lobby mockLobby = new Lobby();
        mockLobby.setPlayerIds(playerIds);

        List<HashMap<Suit, List<Card>>> playerHands = new ArrayList<>();
        for (Integer id : playerIds) {
            HashMap<Suit, List<Card>> hand = new HashMap<>();
            hand.put(Suit.HEARTS, List.of(new Card(Rank.ACE, Suit.HEARTS))); // dummy card
            playerHands.add(hand);
        }

        Deck mockDeck = new Deck();
        mockDeck.setCards(new Stack<>());

        GameSetup mockSetup = new GameSetup();
        mockSetup.setPlayerHands(playerHands);
        mockSetup.setDeck(mockDeck);
        mockSetup.setTrumpSuit(Suit.CLUBS);

        when(lobbyService.getLobby(sessionId)).thenReturn(mockLobby);
        when(deckManagementService.createGame(54, 2)).thenReturn(mockSetup);

        // When
        GameState gameState = gameService.createGame(sessionId);

        // Then
        assertNotNull(gameState);
        assertEquals(2, gameState.getPlayers().size());
        assertEquals(2, gameState.getPlayerHands().size());
        assertEquals(mockDeck, gameState.getDeck());
        assertEquals(Suit.CLUBS, gameState.getTrumpSuit());

        for (Player player : gameState.getPlayers()) {
            assertTrue(gameState.getPlayerHands().containsKey(player.getId()));
        }

        Mockito.verify(lobbyService, times(1)).getLobby(sessionId);
        Mockito.verify(deckManagementService, times(1)).createGame(54, 2);

        printGameState(gameState);
    }

    public static void printGameState(GameState gameState) {
        System.out.println("Game State:");
        System.out.println("-----------");
        System.out.println("Players:");
        for (Player player : gameState.getPlayers()) {
            System.out.println("  - Player ID: " + player.getId());
        }

        System.out.println("\nPlayer Hands:");
        for (Map.Entry<Integer, HashMap<Suit, List<Card>>> entry : gameState.getPlayerHands().entrySet()) {
            System.out.println("  Player " + entry.getKey() + ":");
            for (Map.Entry<Suit, List<Card>> suitEntry : entry.getValue().entrySet()) {
                System.out.println("    " + suitEntry.getKey() + ": " + suitEntry.getValue());
            }
        }

        System.out.println("\nAttacker index: " + gameState.getCurrentAttackerIndex());
        System.out.println("Defender index: " + gameState.getCurrentDefenderIndex());

        System.out.println("\nTrump Suit: " + gameState.getTrumpSuit());
        System.out.println("Deck Size: " + (gameState.getDeck() != null ? gameState.getDeck().getCards().size() : 0));
        System.out.println("Game Over: " + gameState.isGameOver());
        System.out.println("Winner ID: " + gameState.getWinnerId());
    }
}
