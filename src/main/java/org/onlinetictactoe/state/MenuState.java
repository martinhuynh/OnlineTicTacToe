package org.onlinetictactoe.state;

import org.onlinetictactoe.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

public class MenuState extends GameState {
    public MenuState(GameStateManager gsm) {
        super(gsm);
        setFocusable(true);
        setup();
    }

    public void setup() {
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
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel title = new JLabel("Tic Tac Toe");
        Font font = new Font("Arial", Font.BOLD, 40);
        title.setFont(font);
        add(title, gbc);

        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton playButton = new JButton("Multiplayer");
        playButton.addActionListener(e -> {
            gsm.setState(GameStateManager.State.MULTIPLAYER);
        });
        playButton.setEnabled(false);
        add(playButton, gbc);

        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton scoreboard = new JButton("Scoreboard");
        scoreboard.addActionListener(e -> {
            gsm.setState(GameStateManager.State.SCOREBOARD);
        });
        add(scoreboard, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton b3 = new JButton("Quit");
        b3.addActionListener(e -> {
//            gsm.setState(GameStateManager.State.PLAY);
            System.exit(1);
        });
        add(b3, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 00, 0);
        gbc.ipadx = 10;
        gbc.ipady = 0;
        gbc.gridx = 1;
        gbc.gridy = 4;
        JLabel username = new JLabel("Username");
        username.setFont(new Font("Arial", Font.PLAIN, 15));
        add(username, gbc);

        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 5;
        JTextField usernameField = new JTextField("Enter username");
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Enter username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(Color.GRAY);
                    usernameField.setText("Enter username");
                }
            }
        });

        JButton b4 = new JButton("Confirm");
        usernameField.addActionListener((e) -> {
            b4.setEnabled(false);
            usernameField.setEnabled(false);
            playButton.setEnabled(true);
            player = new Player(usernameField.getText(), UUID.randomUUID());
        });
        add(usernameField, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 2;
        gbc.gridy = 5;
        b4.addActionListener((e) -> {
            b4.setEnabled(false);
            usernameField.setEnabled(false);
            playButton.setEnabled(true);
            player = new Player(usernameField.getText(), UUID.randomUUID());
        });
        add(b4, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.8;
//        gbc.weighty = 0.5;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 3;
        gbc.gridy = 0;
        add(new JLabel(""), gbc);
    }

    @Override
    public void escape() {
        // Nothing
    }

    @Override
    public void update() {

    }
}
