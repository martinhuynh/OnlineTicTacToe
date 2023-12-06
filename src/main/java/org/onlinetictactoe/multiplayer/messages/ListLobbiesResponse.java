package org.onlinetictactoe.multiplayer.messages;


import java.io.Serializable;
import java.util.ArrayList;

public class ListLobbiesResponse implements Serializable {

    public ArrayList<LobbyInfo> lobbyInfo;

    public ListLobbiesResponse(ArrayList<LobbyInfo> lobbyInfo) {
        this.lobbyInfo = lobbyInfo;
    }
}
