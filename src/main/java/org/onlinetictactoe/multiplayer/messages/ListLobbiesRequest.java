package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;

public class ListLobbiesRequest implements Serializable {
    public Player player;
    public ListLobbiesRequest(Player player) {
        this.player = player;
    }
}
