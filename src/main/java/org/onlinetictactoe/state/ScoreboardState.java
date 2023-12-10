package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ScoreboardState extends GameState {
    private JPanel innerContainer = new JPanel();
    public ScoreboardState(GameStateManager gsm) {
        super(gsm);
        setup();
        pollScores();
    }

    private void setup() {
        setBackground(Color.GRAY);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);
        JPanel outerContainer = new JPanel();
        outerContainer.setLayout(layout);

        JLabel title = new JLabel("Scoreboard");
        title.setHorizontalAlignment(JLabel.CENTER);
        Font font = new Font("Arial", 0, 40);
        title.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(title, gbc);

        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);;
        outerContainer.add(innerContainer, gbc);
        outerContainer.setBackground(Color.lightGray);

        JScrollPane scrollPane = new JScrollPane(outerContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        gbc = new GridBagConstraints();
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
        add(scrollPane, gbc);

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
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener((e) -> {
            refreshScores();
        });
        refresh.setFont(font2);
        add(refresh, gbc);

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
        JButton returnLobby = new JButton("Return to Main Menu");
        returnLobby.setFont(font2);
        returnLobby.addActionListener(e -> gsm.setState(GameStateManager.State.MENU));
        add(returnLobby, gbc);
    }

    private void pollScores() {
        new Thread(() -> {
            while (true) {
                try {
                    refreshScores();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void refreshScores() {
        TreeMap<Integer, ArrayList<String>> scores = new TreeMap<>();
        innerContainer.removeAll();
        innerContainer.revalidate();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        innerContainer.setBackground(Color.CYAN);
        innerContainer.setLayout(layout);
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.ipady = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int count = 0;
        for(Map.Entry<Integer, ArrayList<String>> entry : scores.entrySet()) {
            for (String s : entry.getValue()) {
                gbc.gridx = 0;
                gbc.gridy = count++;
                JPanel container = createScorePanel(s, entry.getKey());
                innerContainer.add(container, gbc);
            }
        }
        innerContainer.revalidate();
    }

    private JPanel createScorePanel(String name, int wins) {
        JPanel container = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        container.setLayout(layout);
        container.setBorder(BorderFactory.createLineBorder(Color.black));

        Font font = new Font("Arial", Font.PLAIN, 15);
        JLabel username = new JLabel(name);
        username.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 10;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.weightx = 1.0;
        container.add(username, gbc);

        JLabel win = new JLabel(wins + "");
        win.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 10;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.weightx = 0;
        container.add(win, gbc);

        return container;
    }

    @Override
    public void escape() {
        gsm.setState(GameStateManager.State.MENU);
    }

    @Override
    public void update() {

    }
}
