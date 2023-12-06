package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;

public class JoinLobbyResponse implements Serializable {
    boolean success;
    public JoinLobbyResponse(boolean success) {
        this.success = success;
    }
}
