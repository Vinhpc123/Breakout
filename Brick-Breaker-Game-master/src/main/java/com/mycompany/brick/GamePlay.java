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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
    private long lastHitTime = 0;  // Thời gian của lần phát âm thanh trước
    private static final long SOUND_DELAY = 200;  // Đặt thời gian tối thiểu giữa các lần phát âm thanh (200 ms)

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;
    private Image backgroundImage;
    private Clip ballHitSound;
    private Clip backgroundMusic;
    private JButton backButton; // Nút Back

    private boolean isMusicOn = true;

    public GamePlay(JFrame frame) {
        this.frame = frame;

        // Tắt layout manager để sử dụng tọa độ thủ công
        setLayout(null);

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

        backButton = new JButton("Back");
        backButton.setBounds(20, 510, 100, 30); // Kích thước và vị trí
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToStartScreen(); // Quay lại màn hình chính
            }
        });
        backButton.setFocusable(false);

        // Tùy chỉnh giao diện nút
        backButton.setOpaque(true); // Bắt buộc nút không trong suốt
        setLayout(null); // Tắt layout manager

        backButton.setBackground(new Color(255, 255, 255, 200)); // Màu đen với độ trong suốt
        backButton.setForeground(Color.WHITE); // Màu chữ trắng
        backButton.setFont(new Font("Arial", Font.BOLD, 14)); // Đặt font chữ
        backButton.setBorderPainted(true); // Hiển thị viền
        backButton.setBorder(javax.swing.BorderFactory.createLineBorder(Color.YELLOW, 2)); // Viền trắng dày 2px

        // Thêm nút vào JPanel
        this.add(backButton);
        // Đảm bảo gọi setComponentZOrder sau khi thêm backButton
        this.setComponentZOrder(backButton, 0); // Đảm bảo nút nằm trên cùng
    }

    private void goBackToStartScreen() {
        stopMusic(); // Dừng nhạc nền nếu đang phát
        if (timer != null) {
            timer.stop();  // Dừng bộ hẹn giờ trước khi chuyển màn hình
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        StartScreen startScreen = new StartScreen(frame); // Tạo đối tượng StartScreen
        frame.getContentPane().removeAll(); // Xóa nội dung hiện tại
        frame.add(startScreen); // Thêm StartScreen vào frame
        frame.revalidate(); // Cập nhật lại layout
        frame.repaint(); // Vẽ lại frame
    }

    public GamePlay(JFrame frame2, int nextLevel, int score2) {
        //TODO Auto-generated constructor stub
    }

    private void initLevel(int level) {
        int rows = level + 2; // Số hàng gạch tăng theo level
        int cols = 3;         // Số cột gạch cố định

        // Điều chỉnh độ trễ (delay) dựa trên level
        if (level == 1) {
            delay = 15; // Tốc độ chậm nhất ở màn 1
        } else if (level == 2) {
            delay = 12;  // Tăng nhẹ tốc độ ở màn 2
        } else {
            delay = 10;  // Tăng tốc độ nhanh nhất ở màn 3
        }

        // // Tăng tốc độ bóng theo màn chơi
        // if (level == 1) {
        //     ballXDir = -1; // Tốc độ bóng ở màn 1
        //     ballYDir = -1; // Tốc độ bóng ở màn 1
        // } else if (level == 2) {
        //     ballXDir = -2; // Tăng tốc độ bóng nhẹ ở màn 2
        //     ballYDir = -2; // Tăng tốc độ bóng nhẹ ở màn 2
        // } else {
        //     ballXDir = -3; // Tăng tốc độ bóng nhanh ở màn 3
        //     ballYDir = -3; // Tăng tốc độ bóng nhanh ở màn 3
        // }
        totalBricks = rows * cols;  // Tổng số gạch
        map = new MapGenerator(rows, cols);  // Khởi tạo bản đồ gạch
        timer = new Timer(delay, this);  // Tạo lại bộ hẹn giờ mới với tốc độ hiện tại
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
        super.paint(g); // Đảm bảo vẽ lại các thành phần giao diện
        if (!this.isAncestorOf(backButton)) {
            this.add(backButton);
        }

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
            showGameOverScreen();
        }

        if (totalBricks == 0) {
            if (level == 3) {
                // Chiến thắng màn cuối
                play = false;
                showWinScreen();

            } else {
                // Hoàn thành 1 màn chơi
                play = false;
                g.setColor(new Color(0, 255, 0));
                g.setFont(new Font("Serif", Font.BOLD, 40));
                g.drawString(" Level " + level + " Hoàn Thành!", 180, 300);
                g.setFont(new Font("Arial", Font.BOLD, 20
                ));
                g.drawString("Nhấn Phím Enter Để Chơi Màn Tiếp Theo!", 175, 350);
            }
        }

        if (paused) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, 50));
            g.drawString("PAUSED", 260, 300);
        }

        g.dispose();
    }

    private void showWinScreen() {
        stopMusic(); // Dừng nhạc nền nếu đang phát
        if (timer != null) {
            timer.stop();  // Dừng bộ hẹn giờ trước khi chuyển màn hình
        }
        // Hiển thị màn hình "You Win" khi người chơi hoàn thành màn chơi
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        YouWin youWinScreen = new YouWin(frame, score, ballPosX); // Chuyển điểm số đến màn hình "You Win"
        frame.getContentPane().removeAll();  // Xóa nội dung cũ
        frame.add(youWinScreen);  // Thêm màn hình "You Win" vào frame
        frame.revalidate();  // Cập nhật lại layout
        frame.repaint();  // Vẽ lại màn hình
    }

    private void showGameOverScreen() {
        stopMusic(); // Dừng nhạc nền nếu đang phát
        if (timer != null) {
            timer.stop();  // Dừng bộ hẹn giờ trước khi chuyển màn hình
        }
        // Chuyển màn hình sang GameOver
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GameOver gameOverScreen = new GameOver(frame, score, 0); // Truyền điểm số hiện tại vào GameOver
        frame.getContentPane().removeAll(); // Xóa các thành phần hiện tại
        frame.add(gameOverScreen); // Thêm GameOver
        frame.revalidate(); // Cập nhật lại giao diện
        frame.repaint();
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

                            // Kiểm tra và phát âm thanh nếu đủ thời gian
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastHitTime > SOUND_DELAY) {
                                if (ballHitSound != null) {
                                    ballHitSound.setFramePosition(0);  // Đặt lại vị trí âm thanh
                                    ballHitSound.start();  // Phát âm thanh
                                }
                                lastHitTime = currentTime;  // Cập nhật thời gian phát âm thanh
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

    public void restartGame() {
        // Reset toàn bộ trạng thái trò chơi
        level = 1;  // Quay lại màn đầu tiên
        score = 0;  // Reset điểm số
        totalBricks = 21;  // Đặt lại tổng số gạch
        map = new MapGenerator(3, 3);  // Tạo lại bản đồ gạch
        ballPosX = 120;  // Vị trí ban đầu của bóng
        ballPosY = 350;
        ballXDir = -1;  // Hướng ban đầu của bóng
        ballYDir = -2;
        playerX = 310;  // Đặt thanh ngang về vị trí ban đầu

        // Đặt lại trạng thái chơi và bộ đếm thời gian
        play = true;
        paused = false;  // Không tạm dừng

        // Khởi tạo lại bộ hẹn giờ (timer) và tốc độ trò chơi
        if (timer != null) {
            timer.stop();  // Dừng bộ hẹn giờ cũ trước khi tạo mới
        }

        initLevel(level);  // Khởi tạo lại màn chơi mới

        requestFocus(); // Đảm bảo JPanel nhận input từ bàn phím

        // Đảm bảo rằng bộ hẹn giờ được khởi động lại với tốc độ mới
        timer = new Timer(delay, this);  // Tạo lại bộ hẹn giờ với độ trễ mới
        timer.start();  // Bắt đầu bộ hẹn giờ mới
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

                if (totalBricks == 0) {
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
