package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;

public class MenuState extends GameState {
    public MenuState(GameStateManager gsm) {
        super(gsm);
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
        gbc.gridwidth = 1;
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
        add(playButton, gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton b3 = new JButton("Button 3");
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
        // Nothing
    }

    @Override
    public void update() {

    }
}
