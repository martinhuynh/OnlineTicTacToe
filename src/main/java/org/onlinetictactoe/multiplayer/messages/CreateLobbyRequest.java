package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.UUID;

public class CreateLobbyRequest implements Serializable {
    public String lobbyName;
    public UUID lobbyId;
    public int maxPlayers;

    public CreateLobbyRequest(String lobbyName, UUID lobbyId, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.lobbyId = lobbyId;
        this.maxPlayers = maxPlayers;
    }
}
