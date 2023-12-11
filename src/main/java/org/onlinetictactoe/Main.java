package org.onlinetictactoe;

import org.onlinetictactoe.multiplayer.Server;
import org.onlinetictactoe.state.GameStateManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;

    private BufferedImage img;

    private Graphics2D g;

    private GameStateManager gsm;
    private KeyboardHandler keyboardHandler;

    public static final int WIDTH = 1280, HEIGHT = 720;
    public static final String TITLE = "TicTacToe";

    public Main() {
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        gsm = new GameStateManager(this, WIDTH, HEIGHT);
        g = (Graphics2D) img.getGraphics();

        keyboardHandler = new KeyboardHandler(gsm);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyboardHandler);

        pack();
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
//        setResizable(false);
        setVisible(true);
        setFocusable(true);
        requestFocus();
    }

    public static String ipAddress;
    public static int port;

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 3) {
            System.err.println("Invalid arguments. Require 'client ipAddress port' or 'server port'");
            return;
        }
        if (args[0].equalsIgnoreCase("server")) {
            new Server(Integer.parseInt(args[1])).start();
        } else {
            ipAddress = args[1];
            port = Integer.parseInt(args[2]);
            Main game = new Main();
        }
    }
}