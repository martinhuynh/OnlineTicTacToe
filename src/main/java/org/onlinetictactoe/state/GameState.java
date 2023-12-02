package org.onlinetictactoe.state;


import javax.swing.*;

public abstract class GameState extends JPanel {
    protected GameStateManager gsm;
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        setFocusable(true);
    }

    public abstract void escape();
}
