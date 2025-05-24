package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.enums.Suit;
import com.cringenut.game_engine_service.model.*;
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
                placeCardIntoPlayerDeck(entry.getKey(), lostHand);
                // If card not defended
                if (entry.getValue() != null)
                    placeCardIntoPlayerDeck(entry.getValue(), lostHand);
            }

            game.getPlayerHands().put(turn.getDefenseId(), lostHand);
        }

        dealCardsToPlayers(game, turn.getAttackId(), turn.getDefenseId());

        return game;
    }

    public void placeCardIntoPlayerDeck(Card card, LinkedHashMap<Suit, ArrayList<Card>> hand) {
        Suit suit = card.getSuit();
        ArrayList<Card> cardsOfSuit = hand.getOrDefault(suit, new ArrayList<>());
        cardsOfSuit.add(card);
        cardsOfSuit.sort(Comparator.comparingInt(c -> c.getRank().ordinal()));
        hand.put(suit, cardsOfSuit);
    }

    public int getTotalCardsForPlayer(Game game, int playerId) {
        Map<Suit, ArrayList<Card>> hand = game.getPlayerHands().get(playerId);
        if (hand == null) return 0;
        return hand.values().stream().mapToInt(List::size).sum();
    }

    public boolean fillHand(int cardsToFill, Deck deck, LinkedHashMap<Suit, ArrayList<Card>> hand) {
        for (int i = 0; i < cardsToFill; i++) {
            if (deck.getCards().isEmpty()) return false;
            placeCardIntoPlayerDeck(deck.getCards().pop(), hand);
        }
        return true;
    }

    private boolean dealCardsToPlayer(Game game, int playerId, Deck deck) {
        Map<Integer, LinkedHashMap<Suit, ArrayList<Card>>> playerHands = game.getPlayerHands();
        LinkedHashMap<Suit, ArrayList<Card>> hand = playerHands.get(playerId);

        if (hand == null) {
            // Optionally initialize a new hand or throw exception
            return false;
        }

        int cardsToDeal = Math.max(0, Math.min(6, 6 - getTotalCardsForPlayer(game, playerId)));
        return fillHand(cardsToDeal, deck, hand);
    }

    public void dealCardsToPlayers(Game game, Integer attackId, Integer defendId) {
        Deck deck = game.getDeck();
        if (deck == null || deck.getCards().isEmpty()) return;

        Set<Integer> playersToDeal = new LinkedHashSet<>();

        if (attackId != null) playersToDeal.add(attackId);
        if (defendId != null) playersToDeal.add(defendId);

        // If both attacker and defender are null, deal to all
        if (playersToDeal.isEmpty()) {
            playersToDeal.addAll(game.getPlayerHands().keySet());
        }

        for (Integer playerId : playersToDeal) {
            if (!dealCardsToPlayer(game, playerId, deck)) break;
        }
    }

    public Game createGame(Lobby lobby) {

        return null;
    }
}
