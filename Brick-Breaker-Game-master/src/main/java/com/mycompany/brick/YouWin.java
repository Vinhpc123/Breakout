package com.mycompany.brick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.brick.GamePlay; // Import GamePlay
import com.mycompany.brick.StartScreen; // Import StartScreen

public class YouWin extends JPanel {

    private int score;
    private Image youWinImage; // Hình ảnh chiến thắng
    private int level;

    private JButton restartButton;
    private JButton homeButton;
    private JButton exitButton;

    public YouWin(JFrame frame, int score, int level) {
        this.score = score;

        setLayout(null); // Sử dụng layout null để dễ dàng điều chỉnh vị trí nút

        // Tải hình ảnh chiến thắng
        youWinImage = loadImage("resources/images/WinBackground.png");

        // Khởi tạo các nút
        restartButton = createButton("Chơi Lại", 250, 350, 200, 50);
        homeButton = createButton("Trang Chủ", 250, 425, 200, 50);
        exitButton = createButton("Thoát", 250, 500, 200, 50);

        // Thêm các nút vào panel
        add(restartButton);
        add(homeButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ nền mờ
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ hình ảnh Game Over
        if (youWinImage != null) {
            g.drawImage(youWinImage, getWidth() / 3, getHeight() / 14, 225, 200, this);
        }

        // Chỉnh sửa thiết kế của Score
        String scoreText = "Score: " + score;

        // Chuyển đổi Graphics thành Graphics2D để sử dụng các hiệu ứng nâng cao
        Graphics2D g2d = (Graphics2D) g;

        // Sử dụng anti-aliasing cho chữ để mượt mà hơn
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo một Gradient cho màu chữ
        GradientPaint gradient = new GradientPaint(0, 0, Color.YELLOW, 0, getHeight(), Color.RED);
        g2d.setPaint(gradient);

        // Tăng kích thước và thay đổi font chữ
        Font scoreFont = new Font("Arial", Font.BOLD, 50);
        g2d.setFont(scoreFont);

        // Tính toán vị trí Y của Score sao cho nằm giữa "Game w" và "Play Again"
        int imageBottomY = getHeight() / 14 + 200; // Dưới cùng của hình ảnh Game Over
        int buttonTopY = 380; // Vị trí của nút "Play Again"
        int scoreY = (imageBottomY + buttonTopY) / 2; // Trung bình giữa hình ảnh và nút Play Again

        // Vẽ chữ Score với bóng
        int x = getWidth() / 3;  // 1/3 of the width to center the score
        int shadowOffset = 5; // Độ offset cho bóng

        // Vẽ bóng cho chữ Score
        g2d.setColor(Color.BLACK);
        g2d.drawString(scoreText, x + shadowOffset, scoreY + shadowOffset);

        // Vẽ chữ Score chính
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreText, x, scoreY);
    }

    // Phương thức tạo nút
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(new Color(154, 126, 111));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Chơi Lại")) {
                    restartGame(); // Tiến đến màn chơi tiếp theo
                } else if (text.equals("Trang Chủ")) {
                    goHome();
                } else if (text.equals("Thoát")) {
                    System.exit(0);
                }
            }
        });
        return button;
    }

    // Tiến đến màn chơi tiếp theo
    private void restartGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GamePlay gamePlay2 = new GamePlay(frame);  // Tạo đối tượng GamePlay mới

        frame.getContentPane().removeAll();  // Xóa nội dung cũ
        frame.add(gamePlay2);  // Thêm gamePlay mới vào frame
        frame.revalidate();  // Cập nhật lại layout
        frame.repaint();  // Repaint màn hình

        gamePlay2.requestFocus();  // Đảm bảo GamePlay nhận sự kiện bàn phím
    }

    private void goHome() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Xóa mọi thành phần của GameOver trước khi quay về màn hình chính
        frame.getContentPane().removeAll();

        // Thêm màn hình chính StartScreen
        StartScreen startScreen = new StartScreen(frame);
        frame.add(startScreen);

        // Cập nhật và vẽ lại giao diện
        frame.revalidate();
        frame.repaint();
    }

    // Phương thức tải hình ảnh
    private Image loadImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }
}
