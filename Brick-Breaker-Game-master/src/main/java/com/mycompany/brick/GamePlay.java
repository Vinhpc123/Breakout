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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private JFrame frame; // Reference to the JFrame

    private int playerX = 310; // Starting position of paddle
    private int ballPosX = 120; // Ball X position
    private int ballPosY = 350; // Ball Y position
    private int ballXDir = -1; // Ball X direction
    private int ballYDir = -2; // Ball Y direction

    private MapGenerator map;

    public GamePlay(JFrame frame) {
        this.frame = frame;
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
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Game over screen
        if (ballPosY > 570 || totalBricks == 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(new Color(255, 69, 0));
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString(totalBricks == 0 ? "You Win!" : "Game Over", 230, 300);

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
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
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
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBricksValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + bricksWidth) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;

            if (ballPosX < 0 || ballPosX > 670) ballXDir = -ballXDir;
            if (ballPosY < 0) ballYDir = -ballYDir;
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
            frame.getContentPane().removeAll();
            frame.add(new StartScreen(frame));
            frame.revalidate();
            frame.repaint();
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
        ballPosX = 120;
        ballPosY = 350;
        ballXDir = -1;
        ballYDir = -2;
        score = 0;
        playerX = 310;
        totalBricks = 21;
        map = new MapGenerator(3, 7);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
