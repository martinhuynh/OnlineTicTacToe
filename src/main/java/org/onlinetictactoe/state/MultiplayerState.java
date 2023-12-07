package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.UUID;

public class MultiplayerState extends GameState {
    public static ArrayList<Lobby> lobbies = new ArrayList<>();
    private static int hoverDegree = 30;
    private static int clickDegree = 50;
    private static Color defaultColor = Color.green, hoverColor, clickColor;
    private static JPanel innerContainer = new JPanel();
    public MultiplayerState(GameStateManager gsm) {
        super(gsm);
        calculateColors();
        setup();
        pollLobbies();
    }

    private void pollLobbies() {
        new Thread(() -> {
            while (true) {
                try {
                    client.listLobbies();
                    refreshLobbies();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void refreshLobbies() {
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
        for(int i = 0; i < lobbies.size(); i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JPanel container = createLobbyPanel(lobbies.get(i));
//            lobbyContainers.add(container);
            innerContainer.add(container, gbc);
        }
        innerContainer.revalidate();
    }

    private void tempTestCreateLobbies() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = lobbies.size();
                    gbc.weighty = 0;
                    gbc.weightx = 1;
                    gbc.ipady = 0;
                    gbc.anchor = GridBagConstraints.NORTH;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    UUID uuid = UUID.randomUUID();
                    lobbies.add(new Lobby("Added", uuid,0, 2));

                    innerContainer.add(createLobbyPanel(lobbies.get(lobbies.size() - 1)), gbc);
                    innerContainer.revalidate();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void calculateColors() {
        hoverColor = darker(defaultColor, hoverDegree);
        clickColor = darker(defaultColor, clickDegree);
    }

    private void setup() {
        setBackground(Color.GRAY);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);
//
//        innerContainer = new JPanel();
//        innerContainer.setBackground(Color.CYAN);
//        innerContainer.setLayout(layout);
//        gbc.weighty = 0;
//        gbc.weightx = 1;
//        gbc.ipady = 0;
//        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        for(int i = 0; i < lobbies.size(); i++) {
//            gbc.gridx = 0;
//            gbc.gridy = i;
//            JPanel container = createLobbyPanel(lobbies.get(i));
//            lobbyContainers.add(container);
//            innerContainer.add(container, gbc);
//        }
        JPanel outerContainer = new JPanel();
        outerContainer.setLayout(layout);

        JLabel title = new JLabel("Multiplayer");
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
        JButton lobby = new JButton("Refresh");

        // Create Lobby action
//        lobby.addActionListener(e -> gsm.setState(GameStateManager.State.CREATE_LOBBY));
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
        JButton returnLobby = new JButton("Return to Main Menu");
        returnLobby.setFont(font2);
        returnLobby.addActionListener(e -> gsm.setState(GameStateManager.State.MENU));
        add(returnLobby, gbc);
    }

    public static JPanel createLobbyPanel(Lobby lobby) {
        JPanel container = new JPanel();
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
//                    System.out.println(lobby.lobbyName + " clicked");
                    LobbyState.loadLobby(lobby);
                    innerContainer.removeAll();
                    innerContainer.revalidate();
                    gsm.setState(GameStateManager.State.LOBBY);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
//                    System.out.println(lobby.lobbyName + " pressed");
                    container.setBackground(clickColor);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
//                container.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
//                container.setBackground(defaultColor);
            }
        });
        container.setMaximumSize(new Dimension(1000, 30));
        container.setBorder(BorderFactory.createLineBorder(Color.black));
        container.setBackground(Color.GREEN); // Temp

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        container.setLayout(layout);

        Font font = new Font("Arial", 0, 18);

        JLabel name = new JLabel(lobby.lobbyName);
        name.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 10;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.weightx = 1.0;
        container.add(name, gbc);

        JLabel playerCount = new JLabel(lobby.numberOfPlayers + "/" + lobby.maxPlayers);
        playerCount.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 10;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.weightx = 0.0;
        container.add(playerCount, gbc);
        return container;
    }

    public Color darker(Color color, int degree) {
        int red = color.getRed() - degree;
        red = (red < 0) ? 0 : red;

        int green = color.getGreen() - degree;
        green = (green < 0) ? 0 : green;

        int blue = color.getBlue() - degree;
        blue = (blue < 0) ? 0 : blue;

        Color newColor = new Color(red, green, blue, color.getAlpha());
        return newColor;
    }

    public Color lighter(Color color, int degree) {
        int red = color.getRed() + degree;
        red = (red > 255) ? 255 : red;

        int green = color.getGreen() + degree;
        green = (green > 255) ? 255 : green;

        int blue = color.getBlue() + degree;
        blue = (blue > 255) ? 255 : blue;

        Color newColor = new Color(red, green, blue, color.getAlpha());
        return newColor;
    }

    @Override
    public void escape() {
        gsm.setState(GameStateManager.State.MENU);
    }

    @Override
    public void update() {
        refreshLobbies();
    }

    public void updateLobbies() {

    }

    public void addRandomLobbies() {
        int n = (int) (Math.random() * 10) + 10;
        for (int i = 0; i < n; i++)
            lobbies.add(randomLobby(i));
    }

    public Lobby randomLobby(int num) {
        int n1 = (int) (Math.random() * 10);
        int n2 = (int) (Math.random() * 10);
        Lobby temp = new Lobby("Lobby " + ++num, UUID.randomUUID(), n1,  10);
        return temp;
    }
}
