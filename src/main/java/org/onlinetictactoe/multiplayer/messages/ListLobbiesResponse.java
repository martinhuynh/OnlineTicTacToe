package org.onlinetictactoe.multiplayer.messages;


import org.onlinetictactoe.state.Lobby;

import java.io.Serializable;
import java.util.ArrayList;

public class ListLobbiesResponse implements Serializable {

    public ArrayList<Lobby> lobby;

    public ListLobbiesResponse(ArrayList<Lobby> lobby) {
        this.lobby = lobby;
    }
}
