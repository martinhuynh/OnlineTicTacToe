package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.UUID;

public class LobbyInfo implements Serializable {
        public UUID lobbyId;

        public String lobbyName;

        public int playerCount;

        public int maxPlayerCount;

        public LobbyInfo(UUID lobbyId, String lobbyName, int playerCount, int maxPlayerCount) {
            this.lobbyId = lobbyId;
            this.lobbyName = lobbyName;
            this.playerCount = playerCount;
            this.maxPlayerCount = maxPlayerCount;
        }

    }
