package com.cringenut.game_engine_service.service;

import com.cringenut.game_engine_service.dto.CardDTO;
import com.cringenut.game_engine_service.dto.DeckDTO;
import com.cringenut.game_engine_service.dto.LobbyDTO;
import com.cringenut.game_engine_service.dto.TurnDTO;
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
    public Game updateGame(TurnDTO turnDTO) {
        Cache.ValueWrapper wrapper = cacheManager.getCache("GAME_CACHE").get(turnDTO.getId());
        if (wrapper == null) {
            throw new NullPointerException("Game not found in cache for turn ID: " + turnDTO.getId());
        }
        Game game = (Game) wrapper.get();

        // Check if defense lost
        Optional<Map.Entry<CardDTO, CardDTO>> nullValueEntry
                = turnDTO.getTableCards()
                .entrySet().stream()
                .filter(pair -> pair.getValue() == null).findFirst();
        boolean defenseLost = nullValueEntry.isPresent();

        if (defenseLost) {
            LinkedHashMap<Suit, ArrayList<CardDTO>> lostHand
                    = game.getPlayerHands().get(turnDTO.getDefenseId());

            // Place all cards from table into lost hand
            for (Map.Entry<CardDTO, CardDTO> entry : turnDTO.getTableCards().entrySet()) {
                placeCardIntoPlayerDeck(entry.getKey(), lostHand);
                // If card not defended
                if (entry.getValue() != null)
                    placeCardIntoPlayerDeck(entry.getValue(), lostHand);
            }

            game.getPlayerHands().put(turnDTO.getDefenseId(), lostHand);
        }

        dealCardsToPlayers(game, turnDTO.getAttackId(), turnDTO.getDefenseId());

        return game;
    }

    public void placeCardIntoPlayerDeck(CardDTO cardDTO, LinkedHashMap<Suit, ArrayList<CardDTO>> hand) {
        Suit suit = cardDTO.getSuit();
        ArrayList<CardDTO> cardsOfSuit = hand.getOrDefault(suit, new ArrayList<>());
        cardsOfSuit.add(cardDTO);
        cardsOfSuit.sort(Comparator.comparingInt(c -> c.getRank().ordinal()));
        hand.put(suit, cardsOfSuit);
    }

    public int getTotalCardsForPlayer(Game game, int playerId) {
        Map<Suit, ArrayList<CardDTO>> hand = game.getPlayerHands().get(playerId);
        if (hand == null) return 0;
        return hand.values().stream().mapToInt(List::size).sum();
    }

    public boolean fillHand(int cardsToFill, DeckDTO deckDTO, LinkedHashMap<Suit, ArrayList<CardDTO>> hand) {
        for (int i = 0; i < cardsToFill; i++) {
            if (deckDTO.getCards().isEmpty()) return false;
            placeCardIntoPlayerDeck(deckDTO.getCards().pop(), hand);
        }
        return true;
    }

    private boolean dealCardsToPlayer(Game game, int playerId, DeckDTO deckDTO) {
        Map<Integer, LinkedHashMap<Suit, ArrayList<CardDTO>>> playerHands = game.getPlayerHands();
        LinkedHashMap<Suit, ArrayList<CardDTO>> hand = playerHands.get(playerId);

        if (hand == null) {
            // Optionally initialize a new hand or throw exception
            return false;
        }

        int cardsToDeal = Math.max(0, Math.min(6, 6 - getTotalCardsForPlayer(game, playerId)));
        return fillHand(cardsToDeal, deckDTO, hand);
    }

    public void dealCardsToPlayers(Game game, Integer attackId, Integer defendId) {
        DeckDTO deckDTO = game.getDeck();
        if (deckDTO == null || deckDTO.getCards().isEmpty()) return;

        Set<Integer> playersToDeal = new LinkedHashSet<>();

        if (attackId != null) playersToDeal.add(attackId);
        if (defendId != null) playersToDeal.add(defendId);

        // If both attacker and defender are null, deal to all
        if (playersToDeal.isEmpty()) {
            playersToDeal.addAll(game.getPlayerHands().keySet());
        }

        for (Integer playerId : playersToDeal) {
            if (!dealCardsToPlayer(game, playerId, deckDTO)) break;
        }
    }

    public Game createGame(LobbyDTO lobbyDTO) {

        return null;
    }
}
