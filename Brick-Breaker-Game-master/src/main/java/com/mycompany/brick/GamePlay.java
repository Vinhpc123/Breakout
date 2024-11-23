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
    private int totalBricks;
    private Timer timer;
    private int delay;
    private int level = 1; // Level of the game
    private JFrame frame;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;

    public GamePlay(JFrame frame) {
        this.frame = frame;
        initLevel(level);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    private void initLevel(int level) {
        // Tăng số lượng hàng và cột của bricks theo level
        int rows = level + 2; // Tăng rows theo level
        int cols = 7;
        
        // Giảm delay theo level để bóng bay nhanh hơn
        delay = 8 - level;
        if (delay < 3) {
            delay = 3; // Đảm bảo delay không quá thấp
        }
    
        totalBricks = rows * cols;
        map = new MapGenerator(rows, cols);
    
        // Tăng tốc độ bóng theo level bằng cách thay đổi ballXDir và ballYDir
        // Chỉ thay đổi ballXDir và ballYDir khi màn chơi (level) thay đổi, không thay đổi trong mỗi lần game loop
        if (level == 1) {
            ballXDir = -1; // Thiết lập tốc độ mặc định ở level 1
            ballYDir = -2;
        } else {
            ballXDir = -1 - (level / 2);  // Tăng tốc độ X của bóng theo level
            ballYDir = -2 - (level / 2);  // Tăng tốc độ Y của bóng theo level
        }
    
        // Tạo lại Timer với delay mới
        timer = new Timer(delay, this);
        timer.start();  // Khởi động lại Timer
    }
    
    

    @Override
    public void paint(Graphics g) {
        // Background
        g.setColor(new Color(20, 20, 40));
        g.fillRect(1, 1, 692, 592);

        // Draw bricks
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores and level
        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 540, 30);
                g.drawString("Level: " + level, 20, 30);


        // Paddle
        g.setColor(new Color(70, 130, 180));
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(new Color(255, 69, 0));
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Game over
        if (ballPosY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;

            g.setColor(new Color(255, 69, 0));
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString("Game Over", 230, 300);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Next level
        if (totalBricks == 0) {
            play = false;
            g.setColor(new Color(0, 255, 0));
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString("Level " + level + " Complete!", 180, 300);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Continue", 200, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            // Brick collision
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        Rectangle rect = new Rectangle(brickX, brickY, map.bricksWidth, map.bricksHeight);

                        if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(rect)) {
                            map.setBricksValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + map.bricksWidth) {
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
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 600) {
            playerX += 20;
            play = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10) {
            playerX -= 20;
            play = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !play) {
            if (totalBricks == 0) {
                level++;
                initLevel(level);
            } else {
                level = 1;
                initLevel(level);
                score = 0;
            }
            ballPosX = 120;
            ballPosY = 350;
            ballXDir = -1;
            ballYDir = -2;
            playerX = 310;
            play = true;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
