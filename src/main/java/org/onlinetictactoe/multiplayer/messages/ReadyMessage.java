package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;
import java.util.UUID;

public class ReadyMessage implements Serializable {
    public UUID lobbyId;
    public Player player;

    public ReadyMessage(UUID lobbyId, Player player) {
        this.lobbyId = lobbyId;
        this.player = player;
    }
}
