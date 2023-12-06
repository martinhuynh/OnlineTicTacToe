package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;

public class LobbyState extends GameState {
    public static Lobby lobby = null;
    private static JLabel title = new JLabel();
    private static JLabel countdown = new JLabel();
    private static JLabel player1, player2;

    public LobbyState(GameStateManager gsm) {
        super(gsm);
        setup();
    }

    public static void loadLobby(Lobby lobby) {
        LobbyState.lobby = lobby;
        title.setText("Lobby Name: " + lobby.lobbyName);
        client.joinLobby(lobby.lobbyId);
    }

    public static void updateOpponentName(String name) {
        player2.setText(name);
    }

    public static void startCountDown() {
        new Thread(() -> {
            for (int i = 3; i >= 0; i--) {
                countdown.setText("Starting in " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
            gsm.setState(GameStateManager.State.PLAY);
        }).start();
    }

    public void setup() {
        setBackground(Color.CYAN);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);

        Font font = new Font("Arial", Font.PLAIN, 25);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.8;
//        gbc.weighty = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(""), gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.2;
//        gbc.weighty = 0.5;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 0;

        Font font1 = new Font("Arial", Font.BOLD, 55);
        title.setFont(font1);
        add(title, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 1;
        player1 = new JLabel("player1");
        player1.setHorizontalAlignment(JLabel.CENTER);
        player1.setFont(font);
        add(player1, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel vs = new JLabel("VS");
        vs.setHorizontalAlignment(JLabel.CENTER);
        vs.setFont(font);
        add(vs, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 3;
        player2 = new JLabel("player2");
        player2.setHorizontalAlignment(JLabel.CENTER);
        player2.setFont(font);
        add(player2, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 4;
        countdown = new JLabel();
        countdown.setHorizontalAlignment(JLabel.CENTER);
        countdown.setFont(new Font("Arial", Font.PLAIN, 40));
        add(countdown, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.8;
//        gbc.weighty = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 2;
        gbc.gridy = 0;
        add(new JLabel(""), gbc);
    }

    @Override
    public void escape() {
        gsm.setState(GameStateManager.State.MULTIPLAYER);
        client.quit(lobby.lobbyId);
    }

    @Override
    public void update() {
        player1.setText(player.name);
        countdown.setText("Waiting for player to join");
    }
}
