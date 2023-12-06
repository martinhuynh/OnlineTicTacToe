package org.onlinetictactoe.state;

import org.onlinetictactoe.game.TicTacToe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class PlayState extends GameState {
    private PauseState pauseState;
    public static TicTacToe ticTacToe;
    private static ArrayList<GameSquare> squares = new ArrayList<>();
    private JLabel player1, player2;
    private boolean pause = false;

    public static char mark;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        pauseState = new PauseState(gsm);
        ticTacToe = new TicTacToe();
        setup();
    }

    public static void setMark(char mark) {
        PlayState.mark = mark;
        toggleBoard(mark == ticTacToe.getMark());
    }

    public void changePlayer() {
        toggleBoard(mark == ticTacToe.getMark());
        ticTacToe.changePlayer();
        highlight(ticTacToe.getMark());
    }

    public void highlight(char letter) {
        Border blackline = BorderFactory.createLineBorder(Color.black);
        if (letter == 'X') {
            player1.setBorder(blackline);
            player2.setBorder(null);
        } else if (letter == 'O'){
            player2.setBorder(blackline);
            player1.setBorder(null);
        } else {
            player1.setBorder(null);
            player2.setBorder(null);
        }
    }

    private static void toggleBoard(boolean state) {
        for (GameSquare square : squares) {
            square.setEnabled(state);
        }
    }

    public JPanel grid() {
        JPanel board = new JPanel();
        GridLayout layout = new GridLayout();
        layout.setColumns(3);
        layout.setRows(3);
        board.setLayout(layout);
        for (int i = 0; i < 9; i++) {
            GameSquare square = new GameSquare(i/3, i%3);
            squares.add(square);
            square.addActionListener(e -> {
                if (!ticTacToe.makeMove(square.getPosX(), square.getPosY())) return;
                square.setText("" + ticTacToe.getMark());
                client.move(LobbyState.lobby.lobbyId, square.getPosX(), square.getPosY());
                changePlayer();
                // Disable board
                if (ticTacToe.isBoardFull()) {
                    toggleBoard(false);
                }
                if (ticTacToe.checkForWin()) {
                    for (GameSquare s : squares) {
                        if (ticTacToe.winningSquare(s.getPosX(), s.getPosY())) {
                            s.setBackground(Color.YELLOW);
                        }
                        s.setEnabled(false);
                        highlight('P');
                    }
                }
            });
            square.setFont(new Font("Arial", Font.BOLD, 100));
            square.setBackground(Color.WHITE);
            square.setText(" ");
            board.add(square);
        }
        return board;
    }

    public void setup() {
//        JLabel label = new JLabel("Play State");
//        label.setLocation(100, 100);
//        add(label);

        setBackground(Color.GREEN);
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

        gbc.fill = GridBagConstraints.EAST;
        gbc.weightx = 0.2;
//        gbc.weighty = 0.5;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 0;
        player1 = new JLabel("Player 1: O");
        Font font = new Font("Arial", Font.BOLD, 40);
        player1.setFont(font);
        add(player1, gbc);

        gbc.fill = GridBagConstraints.WEST;
        gbc.weightx = 0.2;
//        gbc.weighty = 0.5;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 2;
        gbc.gridy = 0;
        player2 = new JLabel("Player 2: X");
//        player2.setHorizontalAlignment(JLabel.EAST);
        player2.setFont(font);
        add(player2, gbc);

        gbc.weightx = 0.7;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(grid(), gbc);

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

        // Highlight player going first
        highlight(ticTacToe.getMark());
    }


    @Override
    public void escape() {
        pause = !pause;
        if (pause) {
            gsm.setState(GameStateManager.State.PAUSE);
        } else {
            gsm.setState(GameStateManager.State.PLAY);
        }
        validate();
        repaint();
        System.out.println(pause);
    }

    @Override
    public void update() {
        pause = false;
    }
}