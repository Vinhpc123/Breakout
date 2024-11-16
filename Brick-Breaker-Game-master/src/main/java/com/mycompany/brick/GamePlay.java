package com.mycompany.brick;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalbricks = 21;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310; // Starting position of paddle
    private int ballposX = 120; // Ball X position
    private int ballposY = 350; // Ball Y position
    private int ballXdir = -1; // Ball X direction
    private int ballYdir = -2; // Ball Y direction

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Background
        g.setColor(new Color(20, 20, 40));
        g.fillRect(1, 1, 692, 592);

        // Drawing the bricks
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Score
        g.setColor(new Color(255, 215, 0)); // Gold color
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " + score, 550, 30);

        // Paddle
        g.setColor(new Color(70, 130, 180)); // Steel blue paddle
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(new Color(255, 69, 0)); // Red-orange ball
        g.fillOval(ballposX, ballposY, 20, 20);

        // Game over screen
        if (ballposY > 570 || totalbricks == 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(new Color(255, 69, 0));
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString(totalbricks == 0 ? "You Win!" : "Game Over", 200, 300);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // Ball and paddle collision
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // Ball and bricks collision
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        int bricksWidth = map.bricksWidth;
                        int bricksHeight = map.bricksHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                        Rectangle ballrect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballrect.intersects(rect)) {
                            map.setBricksValue(0, i, j);
                            totalbricks--;
                            score += 5;

                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + bricksWidth) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0 || ballposX > 670) ballXdir = -ballXdir;
            if (ballposY < 0) ballYdir = -ballYdir;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) playerX = 600;
            else moveRight();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) playerX = 10;
            else moveLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER && !play) {
            resetGame();
        }
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    private void resetGame() {
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        score = 0;
        playerX = 310;
        totalbricks = 21;
        map = new MapGenerator(3, 7);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
