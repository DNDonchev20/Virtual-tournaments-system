package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameSyncController {
    /**
     * WebSocket endpoint for game updates.
     * When a client sends a Game object to /app/game/update, this method is triggered.
     * It “updates” the game state (here, simply appending the current timestamp)
     * and broadcasts the updated game to all clients subscribed to /topic/gameState.
     */
    @MessageMapping("/game/update")
    @SendTo("/topic/gameState")
    public Game processGameUpdate(Game game) {
        // Simple algorithm: update game state with a timestamp
        game.setState("Updated at " + System.currentTimeMillis());
        return game;
    }
}
