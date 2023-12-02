package org.onlinetictactoe.state;

import java.awt.*;

public class LobbyState extends GameState {
    public LobbyState(GameStateManager gsm) {
        super(gsm);
        setup();
    }

    public void setup() {
        setBackground(Color.CYAN);
    }

    @Override
    public void escape() {
        gsm.setState(GameStateManager.State.MULTIPLAYER);
    }
}
