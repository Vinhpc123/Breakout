package com.mycompany.brick;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

// Lớp MyApp là điểm bắt đầu (entry point) của ứng dụng
public class MyApp {

    public static void main(String[] args) {
        // Sử dụng SwingUtilities.invokeLater để đảm bảo các thành phần giao diện người dùng (UI) 
        // được tạo và xử lý trong luồng sự kiện (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Tạo JFrame, đây là cửa sổ chính của ứng dụng
            JFrame obj = new JFrame();

            // Thiết lập kích thước và vị trí của cửa sổ
            obj.setBounds(10, 10, 700, 600); // Xác định tọa độ (10, 10) và kích thước (700x600)
            
            // Thiết lập tiêu đề của cửa sổ
            obj.setTitle("BrickBreaker");

            // Vô hiệu hóa khả năng thay đổi kích thước của cửa sổ
            obj.setResizable(false);

            // Thiết lập hành động khi người dùng đóng cửa sổ
            obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Tạo màn hình bắt đầu (StartScreen) và thêm nó vào JFrame
            StartScreen startScreen = new StartScreen(obj);
            obj.add(startScreen);

            // Hiển thị cửa sổ lên màn hình
            obj.setVisible(true);

            // Cập nhật bố cục và vẽ lại giao diện để đảm bảo các thay đổi được áp dụng
            obj.revalidate();
            obj.repaint();
        });
    }

    // Phương thức `setVisible` (mặc định được thêm bởi IDE) 
    // nhưng chưa được sử dụng trong mã, gây ra ngoại lệ nếu gọi
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
}
