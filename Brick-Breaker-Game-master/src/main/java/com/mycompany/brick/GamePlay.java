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
    private boolean paused = false;  // Biến kiểm tra tạm dừng
    private int score = 0;
    private int totalBricks;
    private Timer timer;
    private int delay;
    private int level = 1; // Bắt đầu từ màn 1
    private JFrame frame;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;
    private Image backgroundImage;
    private Clip ballHitSound;
    private Clip backgroundMusic;

    private boolean isMusicOn = true;

    public GamePlay(JFrame frame) {
        this.frame = frame;
        initLevel(level); // Khởi tạo màn chơi đầu tiên
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        // Tải hình nền và âm thanh
        backgroundImage = loadImage("resources/images/BG_Lvl2.gif");
        playBackgroundMusic("resources/sounds/WinBGM.wav");
        loadBallHitSound("resources/sounds/Click.wav");
    }

    private void initLevel(int level) {
        int rows = level + 2; // Số hàng gạch tăng theo level
        int cols = 7;         // Số cột gạch cố định

        // Điều chỉnh độ trễ dựa trên level
        if (level == 1) {
            delay = 15; // Tốc độ chậm nhất ở màn 1
        } else if (level == 2) {
            delay = 15;  // Tăng nhẹ tốc độ ở màn 2
        } else {
            delay = Math.max(15 - level, 3); // Tăng tốc độ từ từ từ màn 3 trở đi
        }

        totalBricks = rows * cols;  // Tổng số gạch
        map = new MapGenerator(rows, cols);  // Khởi tạo bản đồ gạch

        // Tăng tốc độ bóng theo màn chơi
        if (level == 1) {
            ballXDir = -1; // Tốc độ chậm nhất ở màn 1
            ballYDir = -1;
        } else if (level == 2) {
            ballXDir = -1; // Tăng nhẹ tốc độ ở màn 2
            ballYDir = -1;
        } else {
            ballXDir = -1 - (level / 2); // Tăng tốc độ từ từ từ màn 3
            ballYDir = -1 - (level / 2);
        }

        timer = new Timer(delay, this);  // Tạo bộ hẹn giờ mới
        timer.start();  // Bắt đầu bộ hẹn giờ
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
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        map.draw((Graphics2D) g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 540, 30);
        g.drawString("Level: " + level, 20, 30);

        g.setColor(new Color(70, 130, 180));
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(new Color(255, 69, 0));
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (ballPosY > 570) {
            // Game over
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(new Color(255, 69, 0));
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString("Game Over", 230, 300);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if (totalBricks == 0) {
            if (level == 3) {
                // Chiến thắng màn cuối
                play = false;
                g.setColor(new Color(0, 255, 0));
                g.setFont(new Font("Serif", Font.BOLD, 40));
                g.drawString("You Win!", 230, 300);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Press Enter to Restart", 230, 350);
            } else {
                // Hoàn thành 1 màn chơi
                play = false;
                g.setColor(new Color(0, 255, 0));
                g.setFont(new Font("Serif", Font.BOLD, 40));
                g.drawString("Level " + level + " Complete!", 180, 300);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Press Enter to Continue", 200, 350);
            }
        }

        if (paused) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 50));
            g.drawString("PAUSED", 260, 300);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play && !paused) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        Rectangle rect = new Rectangle(brickX, brickY, map.bricksWidth, map.bricksHeight);

                        if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(rect)) {
                            // Gạch bị phá hủy, cập nhật bản đồ và số lượng gạch còn lại
                            map.setBricksValue(0, i, j);  // Đánh dấu gạch bị phá
                            totalBricks--;
                            score += 5;

                            // Phát âm thanh khi bóng va vào gạch
                            if (ballHitSound != null) {
                                ballHitSound.setFramePosition(0);  // Đặt lại vị trí âm thanh
                                ballHitSound.start();  // Phát âm thanh
                            }

                            // Thay đổi hướng bóng dựa trên phía va chạm
                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }
                            break A;  // Ra khỏi vòng lặp khi bóng đã phá gạch
                        }
                    }
                }
            }

            // Cập nhật vị trí của bóng
            ballPosX += ballXDir;
            ballPosY += ballYDir;

            // Nếu bóng chạm vào tường, thay đổi hướng
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
                    // Reset game
                    level = 1;
                    score = 0;
                    totalBricks = 21;
                    map = new MapGenerator(3, 7);
                    ballPosX = 120;
                    ballPosY = 350;
                    ballXDir = -1;
                    ballYDir = -2;
                    playerX = 310;
                } else if (totalBricks == 0) {
                    // Next level
                    if (level < 3) {
                        level++;
                        initLevel(level);
                    } else {
                        play = false;
                    }
                }
                play = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = !paused;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            toggleMusic();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
