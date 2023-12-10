package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;
import java.util.UUID;

public class WinMessage implements Serializable {
    public Player player;

    public UUID lobbyId;
    public WinMessage(UUID lobbyId, Player player) {
        this.lobbyId = lobbyId;
        this.player = player;
    }
}
