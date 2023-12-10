package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;
import java.util.UUID;

public class ChatMessage implements Serializable {
    public UUID lobbyId;
    public String message;

    public Player player;

    public ChatMessage(UUID lobbyId, String message, Player player) {
        this.lobbyId = lobbyId;
        this.message = message;
        this.player = player;
    }
}
