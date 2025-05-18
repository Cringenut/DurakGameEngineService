package com.cringenut.game_engine_service;

import com.cringenut.game_engine_service.enums.Rank;
import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.model.Card;
import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    void testPlaceCardsIntoDeck_SortedByRankWithinSuit() {
        // Setup hand
        LinkedHashMap<Suit, ArrayList<Card>> hand = new LinkedHashMap<>();
        hand.put(Suit.HEARTS, new ArrayList<>(List.of(
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        )));

        Card newCard = new Card(Rank.EIGHT, Suit.HEARTS);

        // Act
        gameService.placeCardsIntoDeck(newCard, hand);

        // Assert
        ArrayList<Card> expected = new ArrayList<>(List.of(
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        ));

        assertEquals(expected, hand.get(Suit.HEARTS), "Cards should be sorted by rank within suit");
    }

    @Test
    void testPlaceCardIntoEmptySuitList() {
        LinkedHashMap<Suit, ArrayList<Card>> hand = new LinkedHashMap<>();

        Card card = new Card(Rank.TEN, Suit.SPADES);

        gameService.placeCardsIntoDeck(card, hand);

        assertEquals(1, hand.get(Suit.SPADES).size());
        assertEquals(card, hand.get(Suit.SPADES).get(0));
    }

    @Test
    public void testGameInitialization_PlayerHandsTrumpSuitFirst() {
        Suit trumpSuit = Suit.CLUBS;
        ArrayList<Integer> playerIds = new ArrayList<>(Arrays.asList(1, 2, 3));

        Game game = new Game(1, null, trumpSuit, playerIds);

        for (Integer playerId : playerIds) {
            Map<Suit, ArrayList<Card>> hand = game.getPlayerHands().get(playerId);
            assertNotNull(hand, "Player hand should not be null");

            // Verify it's a LinkedHashMap
            assertTrue(hand instanceof LinkedHashMap, "Suit map should maintain insertion order");

            // Verify first suit is the trump suit
            Iterator<Suit> suitIterator = hand.keySet().iterator();
            assertTrue(suitIterator.hasNext(), "Suit map should contain suits");
            Suit firstSuit = suitIterator.next();
            assertEquals(trumpSuit, firstSuit, "Trump suit should be first in the hand");

            // Verify all suits are present
            Set<Suit> expectedSuits = EnumSet.allOf(Suit.class);
            assertEquals(expectedSuits, hand.keySet(), "All suits should be present");
        }
    }

}
