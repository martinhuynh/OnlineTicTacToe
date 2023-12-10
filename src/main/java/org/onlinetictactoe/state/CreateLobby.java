package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.UUID;

public class CreateLobby extends GameState {
    private JTextField lobbyName;
    public CreateLobby(GameStateManager gsm) {
        super(gsm);
        setup2();
    }

    private void setup2() {
        setBackground(Color.GRAY);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);

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
        JLabel title = new JLabel("Create Lobby");
        Font font = new Font("Arial", Font.BOLD, 40);
        title.setFont(font);
        add(title, gbc);

        Font font2 = new Font("Arial", Font.PLAIN, 20);
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 1;
        lobbyName = new JTextField("Enter lobby name");
        lobbyName.addActionListener((e) -> {
            UUID uuid = UUID.randomUUID();
            client.createLobby(uuid, lobbyName.getText());
            LobbyState.loadLobby(new Lobby(lobbyName.getText(), uuid, 0, 2));
            gsm.setState(GameStateManager.State.LOBBY);
        });
        lobbyName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (lobbyName.getText().equals("Enter lobby name")) {
                    lobbyName.setText("");
                    lobbyName.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (lobbyName.getText().isEmpty()) {
                    lobbyName.setForeground(Color.GRAY);
                    lobbyName.setText("Enter lobby name");
                }
            }
        });
        add(lobbyName, gbc);

        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton create = new JButton("Create");
        create.setFont(font2);
        create.addActionListener(e -> {
            UUID uuid = UUID.randomUUID();
            client.createLobby(uuid, lobbyName.getText());
            LobbyState.loadLobby(new Lobby(lobbyName.getText(), uuid, 0, 2));
            gsm.setState(GameStateManager.State.LOBBY);
        });

        add(create, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton b3 = new JButton("Cancel");
        b3.setFont(font2);
        b3.addActionListener(e -> {
            gsm.setState(GameStateManager.State.MULTIPLAYER);
//            System.exit(1);
        });
        add(b3, gbc);

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
    }

    @Override
    public void update() {
        lobbyName.setText("Enter lobby name");
    }
}
