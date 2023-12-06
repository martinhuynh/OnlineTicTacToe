package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.UUID;
import org.onlinetictactoe.player.Player;

public class JoinLobbyRequest implements Serializable {
    public UUID lobbyId;

    public Player player;
    public JoinLobbyRequest(UUID lobbyId, Player player) {
        this.lobbyId = lobbyId;
        this.player = player;
    }
}
