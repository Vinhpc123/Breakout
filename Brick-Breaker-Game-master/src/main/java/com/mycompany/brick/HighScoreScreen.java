package com.mycompany.brick;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class HighScoreScreen extends JPanel {

    private static final String SCORE_FILE = "highscores.txt"; // Tên tệp chứa điểm số

    public HighScoreScreen(JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 40)); // Đặt màu nền
        
        // Tiêu đề bảng xếp hạng
        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        titleLabel.setForeground(new Color(250, 247, 240));
        add(titleLabel, BorderLayout.NORTH);

        // Lấy danh sách điểm số từ tệp
        String[] highScores = readScoresFromFile();

        // Tạo một danh sách điểm số
        JList<String> scoreList = new JList<>(highScores);
        scoreList.setFont(new Font("Monospaced", Font.PLAIN, 24));
        scoreList.setForeground(Color.WHITE);
        scoreList.setBackground(new Color(20, 20, 40));
        scoreList.setSelectionBackground(new Color(30, 130, 230));

        // Đặt JList vào JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(scoreList);
        add(scrollPane, BorderLayout.CENTER);

        // Nút quay lại menu chính
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Serif", Font.BOLD, 24));
        backButton.setBackground(new Color(154, 126, 111));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            // Quay lại màn hình menu chính
            frame.getContentPane().removeAll();
            frame.add(new StartScreen(frame)); // Thêm lại màn hình chính
            frame.revalidate(); // Cập nhật lại giao diện
            frame.repaint(); // Vẽ lại giao diện
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 40));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Phương thức đọc điểm số từ tệp và trả về một mảng các điểm số
    private String[] readScoresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            StringBuilder scoreBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                scoreBuilder.append(line).append("\n");
            }
            return scoreBuilder.toString().split("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{"No scores available."}; // Trả về thông báo lỗi nếu không thể đọc tệp
        }
    }
}
