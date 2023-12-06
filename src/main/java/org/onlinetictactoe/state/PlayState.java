package org.onlinetictactoe.state;

import javax.swing.*;

public class PlayState extends GameState {
    private GameStateManager gsm;
    private PauseState pauseState;

    private boolean pause = false;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        pauseState = new PauseState(gsm);
        setup();
    }

    public void setup() {
        JLabel label = new JLabel("Play State");
        label.setLocation(100, 100);
        add(label);
    }


    @Override
    public void escape() {
        pause = !pause;
        pauseState.setVisible(pause);
        getRootPane().
        validate();
        repaint();

        System.out.println(pause);
    }

    @Override
    public void update() {

    }
}