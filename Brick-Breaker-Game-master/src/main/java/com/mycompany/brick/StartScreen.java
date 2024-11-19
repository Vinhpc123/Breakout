package com.mycompany.brick;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import javax.swing.*;

public class StartScreen extends JPanel {

    private Image backgroundImage; // Hình nền
    private Image musicOnIcon; // Biểu tượng bật nhạc
    private Image musicOffIcon; // Biểu tượng tắt nhạc
    private Clip backgroundMusic; // Nhạc nền
    private boolean isMusicOn = true; // Trạng thái nhạc (bật/tắt)
    private Rectangle musicToggleArea; // Khu vực click để bật/tắt nhạc

    public StartScreen(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 40));

        // Tải hình nền từ thư mục resources
        backgroundImage = loadImage("resources/images/Menu.gif");

        // Tải biểu tượng bật/tắt nhạc
        musicOnIcon = loadImage("resources/images/musicon.png");
        musicOffIcon = loadImage("resources/images/musicoff.png");

        // Khu vực click để bật/tắt nhạc
        musicToggleArea = new Rectangle(650, 10, 30, 30);

        // Phát nhạc nền từ thư mục resources
        playMusic("resources/sounds/MainMenuBGM.wav");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Tiêu đề
        JLabel title = new JLabel("Brick Breaker", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(new Color(255, 140, 0));
        add(title, gbc);

        // Nút "Start Game"
        JButton startButton = createStyledButton("Start Game", frame);
        gbc.gridy = 1;
        add(startButton, gbc);

        // Nút "Instructions" (Giới thiệu)
        JButton instructionsButton = createStyledInstructionsButton("Instructions", frame);
        gbc.gridy = 2;
        add(instructionsButton, gbc);

        // Nút "Exit Game"
        JButton exitButton = createStyledExitButton("Exit Game");
        gbc.gridy = 3;
        add(exitButton, gbc);

        // Lắng nghe sự kiện chuột
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (musicToggleArea.contains(e.getPoint())) {
                    toggleMusic();
                }
            }
        });
    }

    // Tạo nút "Start Game"
    private JButton createStyledButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            stopMusic();
            GamePlay gameplay = new GamePlay(frame);
            frame.getContentPane().removeAll();
            frame.add(gameplay);
            frame.revalidate();
            frame.repaint();
            gameplay.requestFocusInWindow();
        });
        return button;
    }

    // Tạo nút "Instructions"
    private JButton createStyledInstructionsButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Use the Left and Right arrow keys to move the paddle.\nBreak all the bricks to win!",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });
        return button;
    }

    // Tạo nút "Exit Game"
    private JButton createStyledExitButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            stopMusic();
            System.exit(0);
        });
        return button;
    }

    // Cài đặt kiểu cho nút
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 24));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Phát nhạc nền
    private void playMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Không tìm thấy tệp âm thanh: " + musicFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (Exception e) {
            System.err.println("Lỗi phát nhạc: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Dừng nhạc nền
    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    // Bật/Tắt nhạc nền
    private void toggleMusic() {
        if (isMusicOn) {
            stopMusic();
            isMusicOn = false;
        } else {
            playMusic("resources/sounds/MainMenuBGM.wav");
            isMusicOn = true;
        }
        repaint(); // Vẽ lại để cập nhật biểu tượng
    }

    // Tải hình ảnh
    private Image loadImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
        } else {
            System.err.println("Không tìm thấy hình ảnh: " + path);
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ hình nền
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Vẽ biểu tượng bật/tắt nhạc
        if (isMusicOn) {
            g.drawImage(musicOnIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        } else {
            g.drawImage(musicOffIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        }
    }
}
