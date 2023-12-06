package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;

public class StartGameMessage implements Serializable {
    public char playerMark;

    public String opponentName;

    public StartGameMessage(char playerMark, String opponentName) {
        this.playerMark = playerMark;
        this.opponentName = opponentName;
    }
}
