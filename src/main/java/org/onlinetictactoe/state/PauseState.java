package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;

public class PauseState extends GameState {
    public PauseState(GameStateManager gsm) {
        super(gsm);
        setup();
    }

    public void setup() {
        JLabel label = new JLabel("Pause State");
        label.setLocation(100, 100);
        add(label);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 140));
    }

    @Override
    public void escape() {

    }
}
