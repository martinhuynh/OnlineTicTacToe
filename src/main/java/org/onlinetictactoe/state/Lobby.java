package org.onlinetictactoe.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Lobby implements Serializable {

    public char currentTurn;

    public String lobbyName;

    public UUID lobbyId;

    public int readyPlayers;
    public int numberOfPlayers;
    public int maxPlayers;

    private ArrayList<String> playerNames;

    public Lobby(String lobbyName, UUID lobbyId, int numberOfPlayers, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.numberOfPlayers = numberOfPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyId = lobbyId;
    }

    public boolean isFull () {
        return numberOfPlayers == maxPlayers;
    }

    public void addPlayer(String name) {
        playerNames.add(name);
    }
}

