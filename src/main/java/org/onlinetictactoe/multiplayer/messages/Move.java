package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;
import java.util.UUID;

public class Move implements Serializable {
    public UUID lobbyId;

    public int x;
    public int y;

    public Player player;

    public Move(UUID lobbyId, int x, int y, Player player) {
        this.lobbyId = lobbyId;
        this.x = x;
        this.y = y;
        this.player = player;
    }
}
