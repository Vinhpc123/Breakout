package com.mycompany.brick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.brick.GamePlay; // Import lớp GamePlay
import com.mycompany.brick.StartScreen; // Import lớp StartScreen

// Lớp GameOver kế thừa từ JPanel, dùng để hiển thị màn hình Game Over
public class GameOver extends JPanel {

    private int score; // Biến lưu điểm số của người chơi

    private Image gameOverImage; // Hình ảnh hiển thị khi Game Over

    private JButton restartButton; // Nút để chơi lại
    private JButton homeButton; // Nút để quay về màn hình chính
    private JButton exitButton; // Nút để thoát game

    // Constructor cho GameOver, nhận JFrame, điểm số và một số tham số khác (cps)
    public GameOver(JFrame frame, int score, int cps) {
        this.score = score; // Gán điểm số cho biến score

        setLayout(null); // Sử dụng layout null để dễ dàng điều chỉnh vị trí các nút

        // Tải hình nền Game Over
        gameOverImage = loadImage("resources/images/GameOverBackground.gif");

        // Khởi tạo các nút
        restartButton = createButton("Chơi Lại", 250, 350, 200, 50); // Nút chơi lại
        homeButton = createButton("Trang Chủ", 250, 425, 200, 50); // Nút về màn hình chính
        exitButton = createButton("Thoát", 250, 500, 200, 50); // Nút thoát game

        // Thêm các nút vào panel
        add(restartButton);
        add(homeButton);
        add(exitButton);
    }

    // Constructor khác (tạm thời không sử dụng)
    public GameOver(JFrame frame, int score2) {
        // TODO Auto-generated constructor stub
    }

    // Ghi đè phương thức paintComponent để vẽ giao diện Game Over
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ nền mờ phía sau
        g.setColor(new Color(0, 0, 0, 150)); // Màu đen với độ trong suốt
        g.fillRect(0, 0, getWidth(), getHeight()); // Vẽ hình chữ nhật phủ toàn màn hình

        // Vẽ hình ảnh Game Over
        if (gameOverImage != null) {
            g.drawImage(gameOverImage, getWidth() / 3, getHeight() / 14, 225, 200, this);
        }

        // Tạo chuỗi hiển thị điểm số
        String scoreText = "Điểm: " + score;

        // Sử dụng Graphics2D để vẽ chữ với hiệu ứng
        Graphics2D g2d = (Graphics2D) g;

        // Bật anti-aliasing để làm mịn chữ
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo hiệu ứng gradient cho chữ (màu chuyển từ vàng sang đỏ)
        GradientPaint gradient = new GradientPaint(0, 0, Color.YELLOW, 0, getHeight(), Color.RED);
        g2d.setPaint(gradient);

        // Thay đổi font chữ và kích thước
        Font scoreFont = new Font("Arial", Font.BOLD, 50);
        g2d.setFont(scoreFont);

        // Tính toán vị trí Y để đặt điểm số nằm giữa hình ảnh Game Over và nút
        int imageBottomY = getHeight() / 14 + 200; // Điểm dưới cùng của hình ảnh Game Over
        int buttonTopY = 380; // Điểm trên cùng của nút "Play Again"
        int scoreY = (imageBottomY + buttonTopY) / 2; // Vị trí trung bình

        // Vẽ bóng cho chữ điểm số
        int x = getWidth() / 3; // Tọa độ X để căn giữa
        int shadowOffset = 5; // Khoảng cách bóng đổ

        // Vẽ bóng cho chữ
        g2d.setColor(Color.BLACK);
        g2d.drawString(scoreText, x + shadowOffset, scoreY + shadowOffset);

        // Vẽ chữ điểm số chính
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreText, x, scoreY);
    }

    // Phương thức tạo nút với hiệu ứng đặc biệt
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Thay đổi màu sắc nút khi hover hoặc nhấn
                if (getModel().isPressed()) {
                    g.setColor(new Color(154, 126, 111).darker()); // Màu tối hơn khi nhấn
                } else if (getModel().isRollover()) {
                    g.setColor(new Color(154, 126, 111).brighter()); // Màu sáng hơn khi hover
                } else {
                    g.setColor(new Color(154, 126, 111)); // Màu mặc định
                }

                // Vẽ nền nút với góc bo tròn
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillRoundRect(0, 0, width, height, 30, 30); // Góc bo tròn 30px

                super.paintComponent(g); // Gọi phương thức paint mặc định để hiển thị chữ
            }
        };

        // Cấu hình thuộc tính của nút
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false); // Không viền
        button.setContentAreaFilled(false); // Không tô nền mặc định

        // Thêm sự kiện cho nút
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Chơi Lại")) { // Nếu bấm nút "Play Again"
                    restartGame();
                } else if (text.equals("Trang Chủ")) { // Nếu bấm nút "Home"
                    goHome();
                } else if (text.equals("Thoát")) { // Nếu bấm nút "Exit"
                    System.exit(0); // Thoát chương trình
                }
            }
        });

        return button;
    }

    // Phương thức để chơi lại game
    private void restartGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GamePlay gamePlay1 = new GamePlay(frame); // Tạo đối tượng GamePlay mới

        frame.getContentPane().removeAll(); // Xóa nội dung hiện tại
        frame.add(gamePlay1); // Thêm màn hình game mới
        frame.revalidate(); // Cập nhật layout
        frame.repaint(); // Vẽ lại giao diện

        gamePlay1.requestFocus(); // Đảm bảo GamePlay nhận sự kiện bàn phím
    }

    // Phương thức để quay về màn hình chính
    private void goHome() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        frame.getContentPane().removeAll(); // Xóa nội dung hiện tại
        StartScreen startScreen = new StartScreen(frame); // Tạo màn hình chính
        frame.add(startScreen); // Thêm màn hình chính vào frame

        frame.revalidate(); // Cập nhật giao diện
        frame.repaint(); // Vẽ lại giao diện
    }

    // Phương thức tải hình ảnh từ đường dẫn
    private Image loadImage(String path) {
        ImageIcon icon = new ImageIcon(path); // Tạo đối tượng ImageIcon từ đường dẫn
        return icon.getImage(); // Lấy đối tượng Image từ ImageIcon
    }
}
