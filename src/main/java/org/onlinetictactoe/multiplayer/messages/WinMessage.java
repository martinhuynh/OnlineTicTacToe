package org.onlinetictactoe.multiplayer.messages;

import org.onlinetictactoe.player.Player;

import java.io.Serializable;

public class WinMessage implements Serializable {
    public Player player;

    public WinMessage(Player player) {
        this.player = player;
    }
}
