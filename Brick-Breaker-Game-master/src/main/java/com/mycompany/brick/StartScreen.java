package com.mycompany.brick;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartScreen extends JPanel {

    private Image backgroundImage; // Hình nền
    private Image musicOnIcon; // Icon bật nhạc
    private Image musicOffIcon; // Icon tắt nhạc
    private Clip backgroundMusic; // Nhạc nền
    private boolean isMusicOn = true; // Trạng thái nhạc (bật/tắt)
    private Rectangle musicToggleArea; // Vùng để click bật/tắt nhạc

    public StartScreen(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 40)); // Đặt màu nền

        // Tải hình nền từ thư mục "resources"
        backgroundImage = loadImage("resources/images/backgr.png");

        // Tải icon bật/tắt nhạc
        musicOnIcon = loadImage("resources/images/musicon.png");
        musicOffIcon = loadImage("resources/images/musicoff.png");

        // Định nghĩa vùng để click bật/tắt nhạc
        musicToggleArea = new Rectangle(650, 10, 30, 30);

        // Phát nhạc nền từ tệp trong thư mục "resources"
        playMusic("resources/sounds/MainMenuBGM.wav");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Khoảng cách giữa các phần tử giao diện
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Tiêu đề game
        JLabel title = new JLabel("Brick Breaker", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 48)); // Đặt font chữ lớn
        title.setForeground(new Color(250, 247, 240)); // Đặt màu chữ
        add(title, gbc);

        // Nút "Start Game" để bắt đầu trò chơi
        JButton startButton = createStyledButton("Start Game", frame);
        gbc.gridy = 1;
        add(startButton, gbc);

        // Nút "Instructions" để xem hướng dẫn
        JButton instructionsButton = createStyledInstructionsButton("Instructions", frame);
        gbc.gridy = 2;
        add(instructionsButton, gbc);

        // Nút "Exit Game" để thoát game
        JButton exitButton = createStyledExitButton("Exit Game");
        gbc.gridy = 3;
        add(exitButton, gbc);

        // Thêm sự kiện lắng nghe click chuột
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Nếu click vào vùng bật/tắt nhạc, thực hiện chức năng toggle
                if (musicToggleArea.contains(e.getPoint())) {
                    toggleMusic();
                }
            }
        });
    }

    // Hàm tạo nút "Start Game"
    private JButton createStyledButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button); // Áp dụng phong cách cho nút
        button.setPreferredSize(new Dimension(170, 55)); // Đặt kích thước cho nút
        button.addActionListener(e -> {
            stopMusic(); // Dừng nhạc nền khi vào trò chơi
            GamePlay gameplay = new GamePlay(frame);
            frame.getContentPane().removeAll(); // Xóa giao diện hiện tại
            frame.add(gameplay); // Thêm giao diện chơi game
            frame.revalidate(); // Cập nhật lại giao diện
            frame.repaint(); // Vẽ lại giao diện
            gameplay.requestFocusInWindow(); // Đưa con trỏ vào vùng game
        });
        return button;
    }

    // Hàm tạo nút "Instructions"
    private JButton createStyledInstructionsButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button); // Áp dụng phong cách cho nút
        button.setPreferredSize(new Dimension(170, 55)); // Đặt kích thước cho nút
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    // Thông báo hướng dẫn chơi game
                    "Sử dụng các phím mũi tên Trái và Phải để di chuyển thanh ngang.\n"
                            + "Phá vỡ tất cả các viên gạch để giành chiến thắng!\n"
                            + "Nhấn phím P nếu muốn tạm dừng trò chơi!\n"
                            + "Nhấn phím M nếu bạn muốn tắt nhạc nền lúc chơi!",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });
        return button;
    }

    // Hàm tạo nút "Exit Game"
    private JButton createStyledExitButton(String text) {
        JButton button = new JButton(text);
        styleButton(button); // Áp dụng phong cách cho nút
        button.setPreferredSize(new Dimension(170, 55)); // Đặt kích thước cho nút
        button.addActionListener(e -> {
            stopMusic(); // Dừng nhạc trước khi thoát game
            System.exit(0); // Thoát chương trình
        });
        return button;
    }

    // Phong cách cho các nút
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 24)); // Đặt font chữ
        button.setBackground(new Color(154, 126, 111)); // Đặt màu nền
        button.setForeground(Color.WHITE); // Đặt màu chữ
        button.setFocusPainted(false); // Ẩn viền focus
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(154, 126, 111), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Đặt viền và padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Đổi con trỏ chuột

        // Hiệu ứng hover (khi chuột di qua)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 130, 230)); // Đổi màu khi hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(154, 126, 111)); // Trả lại màu cũ
            }
        });
    }

    // Phát nhạc nền
    private void playMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Không tìm thấy file nhạc: " + musicFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Phát lặp lại liên tục
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

    // Bật/tắt nhạc
    private void toggleMusic() {
        if (isMusicOn) {
            stopMusic(); // Dừng nhạc nếu đang bật
            isMusicOn = false;
        } else {
            playMusic("resources/sounds/MainMenuBGM.wav"); // Phát lại nhạc nếu đang tắt
            isMusicOn = true;
        }
        repaint(); // Cập nhật lại giao diện
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

        // Vẽ icon bật/tắt nhạc
        if (isMusicOn) {
            g.drawImage(musicOnIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        } else {
            g.drawImage(musicOffIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        }
    }
}
