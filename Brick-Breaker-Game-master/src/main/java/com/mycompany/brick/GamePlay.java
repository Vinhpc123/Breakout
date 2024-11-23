package com.mycompany.brick;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    private Image backgroundImage; // Image background
    private Clip ballHitSound; // Sound when ball hits a brick
    private Clip backgroundMusic; // Background music for the game

    private boolean isMusicOn = true;

    public GamePlay(JFrame frame) {
        this.frame = frame;
        initLevel(level);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        // Load background image for the game
        backgroundImage = loadImage("resources/images/BG_Lvl2.gif");

        // Play background music
        playBackgroundMusic("resources/sounds/WinBGM.wav");

        // Load sound for ball hitting a brick
        loadBallHitSound("resources/sounds/Click.wav");
    }

    private void initLevel(int level) {
        int rows = level + 2;
        int cols = 7;
        delay = 8 - level;
        if (delay < 3) {
            delay = 3;
        }

        totalBricks = rows * cols;
        map = new MapGenerator(rows, cols);

        // Increase ball speed based on the level
        if (level == 1) {
            ballXDir = -1;
            ballYDir = -1;
        } else {
            ballXDir = -1 - (level / 2);
            ballYDir = -2 - (level / 2);
        }

        // Create a new timer with updated delay
        timer = new Timer(delay, this);
        timer.start();
    }

    private void playBackgroundMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Music file not found: " + musicFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadBallHitSound(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            ballHitSound = AudioSystem.getClip();
            ballHitSound.open(audioStream);
        } catch (Exception e) {
            System.err.println("Error loading ball hit sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Image loadImage(String path) {
        Image img = Toolkit.getDefaultToolkit().getImage(path);
        if (img == null) {
            System.err.println("Image not found: " + path);
        }
        return img;
    }

    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    private void toggleMusic() {
        if (isMusicOn) {
            stopMusic();
            isMusicOn = false;
        } else {
            playBackgroundMusic("resources/sounds/WinBGM.wav");
            isMusicOn = true;
        }
        repaint(); // Redraw to update icon
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

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

                            // Kiểm tra nếu ballHitSound không phải null trước khi phát âm thanh
                            if (ballHitSound != null) {
                                ballHitSound.setFramePosition(0); // Đảm bảo âm thanh phát từ đầu
                                ballHitSound.start();
                            }

                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width) {
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

            if (ballPosX < 0) {
                ballXDir = -ballXDir;
            }

            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }

            if (ballPosX > 670) {
                ballXDir = -ballXDir;
            }
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                if (ballPosY > 570) {
                    level = 1; // Quay lại cấp độ 1
                    score = 0; // Reset điểm số
                    initLevel(level); // Khởi tạo lại cấp độ
                    ballPosX = 120;
                    ballPosY = 350;
                    ballXDir = -1;  // Reset hướng bóng
                    ballYDir = -2;  // Reset hướng bóng
                    play = true; // Bắt đầu trò chơi
                    repaint();
                } else {
                    level++;
                    initLevel(level);
                    score = 0;
                    ballPosX = 120;
                    ballPosY = 350;
                    ballXDir = -1;
                    ballYDir = -2;
                    play = true;
                    repaint();
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            toggleMusic();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
