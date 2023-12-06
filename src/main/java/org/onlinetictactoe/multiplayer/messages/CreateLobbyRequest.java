package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.UUID;
import org.onlinetictactoe.player.Player;

public class CreateLobbyRequest implements Serializable {
    public String lobbyName;
    public UUID lobbyId;

    public Player player;

    public int maxPlayers;

    public CreateLobbyRequest(String lobbyName, UUID lobbyId, int maxPlayers, Player player) {
        this.lobbyName = lobbyName;
        this.lobbyId = lobbyId;
        this.maxPlayers = maxPlayers;
        this.player = player;
    }
}
