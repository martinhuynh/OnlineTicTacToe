package org.onlinetictactoe.state;

import java.io.Serializable;
import java.util.UUID;

public class Lobby implements Serializable {

    public char currentTurn;

    public String lobbyName;

    public UUID lobbyId;

    public int numberOfPlayers;
    public int maxPlayers;

    public String otherPlayerName;

    public Lobby(String lobbyName, UUID lobbyId, int numberOfPlayers, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.numberOfPlayers = numberOfPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyId = lobbyId;
    }
}

