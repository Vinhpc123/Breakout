package com.mycompany.brick;

// Import các thư viện cần thiết
import java.awt.BorderLayout;  // Quản lý bố cục theo các vùng (North, South, Center, etc.)
import java.awt.Color;         // Xử lý màu sắc
import java.awt.Font;          // Xử lý kiểu chữ
import java.io.BufferedReader; // Đọc dữ liệu từ tệp
import java.io.FileReader;     // Đọc tệp văn bản
import java.io.FileWriter;     // Ghi dữ liệu vào tệp
import java.io.IOException;    // Xử lý lỗi vào/ra

// Import các thành phần giao diện
import javax.swing.JButton;    // Nút bấm
import javax.swing.JFrame;     // Khung giao diện chính
import javax.swing.JLabel;     // Nhãn hiển thị văn bản
import javax.swing.JList;      // Danh sách hiển thị
import javax.swing.JPanel;     // Bảng chứa các thành phần giao diện
import javax.swing.JScrollPane; // Cuộn nội dung trong giao diện
import javax.swing.SwingConstants; // Định dạng vị trí văn bản trong nhãn

// Lớp hiển thị màn hình điểm cao
public class HighScoreScreen extends JPanel {

    // Đường dẫn tệp lưu điểm cao
    private static final String SCORE_FILE = "highscores.txt";

    // Biến lưu số lần chơi, hiện tại không được sử dụng
    private static int playCount = 0;

    // Constructor: Tạo màn hình hiển thị điểm cao
    public HighScoreScreen(JFrame frame) {
        setLayout(new BorderLayout()); // Thiết lập bố cục cho bảng (BorderLayout)
        setBackground(new Color(20, 20, 40)); // Đặt màu nền tối (xanh đậm)

        // Tiêu đề của bảng điểm
        JLabel titleLabel = new JLabel("Lịch Sử Trò Chơi", SwingConstants.CENTER);
        // Đặt kiểu chữ, kích thước và màu sắc cho tiêu đề
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        titleLabel.setForeground(new Color(250, 247, 240));
        add(titleLabel, BorderLayout.NORTH); // Thêm tiêu đề vào vùng phía Bắc của giao diện

        // Lấy danh sách điểm số từ tệp
        String[] highScores = readScoresFromFile();

        // Tạo danh sách hiển thị điểm số
        JList<String> scoreList = new JList<>(highScores);
        scoreList.setFont(new Font("Monospaced", Font.PLAIN, 24)); // Đặt kiểu chữ, kích thước
        scoreList.setForeground(Color.WHITE); // Màu chữ trắng
        scoreList.setBackground(new Color(20, 20, 40)); // Nền giống với nền giao diện
        scoreList.setSelectionBackground(new Color(30, 130, 230)); // Nền khi được chọn

        // Thêm danh sách điểm số vào bảng cuộn
        JScrollPane scrollPane = new JScrollPane(scoreList);
        add(scrollPane, BorderLayout.CENTER); // Thêm bảng cuộn vào vùng giữa giao diện

        // Tạo nút quay lại menu chính
        JButton backButton = new JButton("Trang Chủ");
        backButton.setFont(new Font("Serif", Font.BOLD, 24)); // Đặt kiểu chữ và kích thước
        backButton.setBackground(new Color(154, 126, 111)); // Màu nền nút
        backButton.setForeground(Color.WHITE); // Màu chữ của nút
        backButton.setFocusPainted(false); // Loại bỏ viền khi nhấn vào nút

        // Xử lý sự kiện khi nhấn nút "Trang Chủ"
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll(); // Xóa toàn bộ nội dung hiện tại
            frame.add(new StartScreen(frame)); // Thêm màn hình khởi động
            frame.revalidate(); // Cập nhật lại giao diện
            frame.repaint(); // Vẽ lại giao diện
        });

        // Tạo bảng chứa nút "Trang Chủ"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 40)); // Cùng màu với nền chính
        buttonPanel.add(backButton); // Thêm nút vào bảng
        add(buttonPanel, BorderLayout.SOUTH); // Thêm bảng nút vào vùng phía Nam
    }

    // Phương thức đọc điểm số từ tệp và trả về mảng chuỗi
    private String[] readScoresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            StringBuilder scoreBuilder = new StringBuilder(); // Dùng để xây dựng danh sách chuỗi
            String line;
            while ((line = reader.readLine()) != null) { // Đọc từng dòng trong tệp
                scoreBuilder.append(line).append("\n"); // Thêm dòng đó vào danh sách
            }
            return scoreBuilder.toString().split("\n"); // Tách chuỗi thành mảng theo dòng
        } catch (IOException e) { // Bắt lỗi nếu không thể đọc tệp
            e.printStackTrace(); // In thông báo lỗi ra console
            return new String[]{"No scores available."}; // Trả về thông báo khi không có dữ liệu
        }
    }

    // Phương thức ghi điểm số vào tệp (hiện chưa hoàn chỉnh)
    public void saveScore(int score) {
        try (FileWriter writer = new FileWriter(SCORE_FILE, true)) { // Mở tệp để ghi thêm dữ liệu
            writer.write(score + "\n"); // Ghi điểm số vào tệp và xuống dòng
        } catch (IOException e) { // Bắt lỗi nếu không thể ghi
            e.printStackTrace(); // In thông báo lỗi ra console
        }
    }
}
