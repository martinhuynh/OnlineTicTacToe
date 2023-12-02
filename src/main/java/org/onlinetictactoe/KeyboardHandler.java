package org.onlinetictactoe;


import org.onlinetictactoe.state.GameStateManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyboardHandler implements KeyEventDispatcher {
    private GameStateManager gsm;

    public KeyboardHandler(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_ESCAPE -> {
                    gsm.escape();
                }
            }
        }
        return false;
    }
}
