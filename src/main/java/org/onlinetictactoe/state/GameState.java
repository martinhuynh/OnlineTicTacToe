package org.onlinetictactoe.state;


import org.onlinetictactoe.multiplayer.Client;
import org.onlinetictactoe.player.Player;

import javax.swing.*;
import java.util.UUID;

public abstract class GameState extends JPanel {
    protected static GameStateManager gsm;
    protected static Client client;
    protected static Player player;
    protected boolean running = false;
    public GameState(GameStateManager gsm) {
        if (gsm != null) this.gsm = gsm;
        if (client == null) {
            client = new Client("localhost", 4001);
            client.start();
        }
        setFocusable(true);
    }

    public abstract void escape();
    public abstract void update();
}
