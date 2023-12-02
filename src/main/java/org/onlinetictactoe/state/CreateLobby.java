package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;

public class CreateLobby extends GameState {
    public CreateLobby(GameStateManager gsm) {
        super(gsm);
        setup();
    }

    private void setup() {
        setBackground(Color.ORANGE);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);

        JPanel main = new JPanel();
        JTextField name = new JTextField("Lobby name", 16);
        main.add(name);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        add(new JLabel(""), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(main, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(""), gbc);

        Font font2 = new Font("Arial", 0, 20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        gbc.insets = new Insets(0, 0, 60, 0);
        gbc.anchor = GridBagConstraints.WEST;
        JButton lobby = new JButton("Create Lobby");
        lobby.addActionListener(e -> gsm.setState(GameStateManager.State.LOBBY));
        lobby.setFont(font2);
        add(lobby, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        gbc.insets = new Insets(0, 0, 60, 0);
        gbc.anchor = GridBagConstraints.EAST;
        JButton returnLobby = new JButton("Cancel");
        returnLobby.setFont(font2);
        returnLobby.addActionListener(e -> gsm.setState(GameStateManager.State.MULTIPLAYER));
        add(returnLobby, gbc);
    }

    @Override
    public void escape() {
        gsm.setState(GameStateManager.State.MULTIPLAYER);
    }
}
