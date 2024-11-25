package com.mycompany.brick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.brick.GamePlay; // Thêm import cho GamePlay
import com.mycompany.brick.StartScreen; // Thêm import cho StartScreen

public class GameOver extends JPanel {

    private int score;

    private Image gameOverImage; // Hình ảnh Game Over

    private JButton restartButton;
    private JButton homeButton;
    private JButton exitButton;

    public GameOver(JFrame frame, int score, int cps) {
        this.score = score;

        setLayout(null); // Layout null để dễ dàng điều chỉnh các nút

        // Tải hình ảnh Game Over
        gameOverImage = loadImage("resources/images/GameOverBackground.gif");

        // Khởi tạo các nút
        restartButton = createButton("Play Again", 250, 350, 200, 50);
        homeButton = createButton("Home", 250, 425, 200, 50);
        exitButton = createButton("Exit", 250, 500, 200, 50);

        // Thêm các nút vào panel
        add(restartButton);
        add(homeButton);
        add(exitButton);
    }

    public GameOver(JFrame frame, int score2) {
        //TODO Auto-generated constructor stub
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Vẽ nền mờ
    g.setColor(new Color(0, 0, 0, 150));
    g.fillRect(0, 0, getWidth(), getHeight());

    // Vẽ hình ảnh Game Over
    if (gameOverImage != null) {
        g.drawImage(gameOverImage, getWidth() / 3, getHeight() / 14, 225, 200, this);
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

    // Tính toán vị trí Y của Score sao cho nằm giữa "Game Over" và "Play Again"
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


private JButton createButton(String text, int x, int y, int width, int height) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(new Color(154, 126, 111).darker()); // Darker color on press
            } else if (getModel().isRollover()) {
                g.setColor(new Color(154, 126, 111).brighter()); // Brighter color on hover
            } else {
                g.setColor(new Color(154, 126, 111)); // Normal color
            }
            
            // Draw rounded button background with gradient
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, width, height, 30, 30); // Round corners with radius 30

            super.paintComponent(g); // Call the default paint to render text
        }
    };
    
    button.setBounds(x, y, width, height);
    button.setFont(new Font("Serif", Font.BOLD, 20));
    button.setForeground(Color.BLACK);
    button.setFocusPainted(false);
    button.setBorderPainted(false); // No border
    button.setContentAreaFilled(false); // To make background transparent

    // Add action listener for button functionality
    button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (text.equals("Play Again")) {
                restartGame();
            } else if (text.equals("Home")) {
                goHome();
            } else if (text.equals("Exit")) {
                System.exit(0);
            }
        }
    });
    
    return button;
}

    private void restartGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GamePlay gamePlay = new GamePlay(frame);  // Tạo đối tượng GamePlay mới

        frame.getContentPane().removeAll();  // Xóa nội dung cũ
        frame.add(gamePlay);  // Thêm gamePlay mới vào frame
        frame.revalidate();  // Cập nhật lại layout
        frame.repaint();  // Repaint màn hình

        gamePlay.requestFocus();  // Đảm bảo GamePlay nhận sự kiện bàn phím
    }

    // Quay lại màn hình chính (Home)
    private void goHome() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        StartScreen startScreen = new StartScreen(frame);
        frame.getContentPane().removeAll();
        frame.add(startScreen);
        frame.revalidate();
        frame.repaint();
    }

    // Phương thức tải hình ảnh
    private Image loadImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }
}
