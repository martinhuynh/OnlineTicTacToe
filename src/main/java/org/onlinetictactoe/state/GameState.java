package org.onlinetictactoe.state;


import org.onlinetictactoe.Main;
import org.onlinetictactoe.multiplayer.Client;
import org.onlinetictactoe.player.Player;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

public abstract class GameState extends JPanel {
    protected static GameStateManager gsm;
    protected static Client client;
    protected static Player player;
    protected boolean running = false;
    public GameState(GameStateManager gsm) {
        if (gsm != null) this.gsm = gsm;
        if (client == null) {
            client = new Client(Main.ipAddress, Main.port);
            client.start();
        }
        setupFocusable();
        setFocusable(true);
    }

    private void setupFocusable() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
    }

    public abstract void escape();
    public abstract void update();
}
