package org.onlinetictactoe.state;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameSquare extends JButton {
    private Color border, color;
    private int radius = 20;
    private int x, y;
    public GameSquare(int x, int y) {
        this.x = x;
        this.y = y;
        border = Color.BLACK;
        color = Color.WHITE;
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                super.mouseExited(e);
            }
        });
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(border);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius - 2, radius - 2);
        super.paintComponent(g);
    }
}
