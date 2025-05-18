package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.model.Card;
import com.cringenut.game_engine_service.model.Game;
import com.cringenut.game_engine_service.model.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    @Autowired
    private CacheManager cacheManager;

    public void placeCardsIntoDeck(Card card, LinkedHashMap<Suit, ArrayList<Card>> hand) {
        Suit suit = card.getSuit();

        // Get the list for the specific suit
        ArrayList<Card> cardsOfSuit = hand.getOrDefault(suit, new ArrayList<>());

        // Insert the card and sort the list by rank
        cardsOfSuit.add(card);
        cardsOfSuit.sort(Comparator.comparingInt(c -> c.getRank().ordinal()));

        // Put the sorted list back in the map (optional since it's the same reference)
        hand.put(suit, cardsOfSuit);
    }

    @CachePut(value = "GAME_CACHE", key = "#result.getId()")
    public Game updateGame(Turn turn) {
        Cache.ValueWrapper wrapper = cacheManager.getCache("GAME_CACHE").get(turn.getId());
        if (wrapper == null) {
            throw new NullPointerException("Game not found in cache for turn ID: " + turn.getId());
        }
        Game game = (Game) wrapper.get();

        // Check if defense lost
        Optional<Map.Entry<Card, Card>> nullValueEntry
                = turn.getTableCards()
                .entrySet().stream()
                .filter(pair -> pair.getValue() == null).findFirst();
        boolean defenseLost = nullValueEntry.isPresent();

        if (defenseLost) {
            LinkedHashMap<Suit, ArrayList<Card>> lostHand
                    = game.getPlayerHands().get(turn.getDefenseId());

            // Place all cards from table into lost hand
            for (Map.Entry<Card, Card> entry : turn.getTableCards().entrySet()) {
                placeCardsIntoDeck(entry.getKey(), lostHand);
                // If card not defended
                if (entry.getValue() != null)
                    placeCardsIntoDeck(entry.getValue(), lostHand);
            }

            game.getPlayerHands().put(turn.getDefenseId(), lostHand);
        }


        return game;
    }
}
