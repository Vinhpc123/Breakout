package com.mycompany.brick;

import java.awt.*;

// Lớp MapGenerator được sử dụng để tạo và quản lý bản đồ các viên gạch trong trò chơi
public class MapGenerator {
    public int[][] map; // Mảng 2 chiều để lưu trạng thái của các viên gạch (1: còn tồn tại, 0: bị phá hủy)
    public int bricksWidth; // Chiều rộng của mỗi viên gạch
    public int bricksHeight; // Chiều cao của mỗi viên gạch

    // Constructor để tạo bản đồ gạch với số hàng (row) và số cột (col)
    public MapGenerator(int row, int col) {
        map = new int[row][col]; // Khởi tạo mảng 2 chiều theo kích thước hàng và cột
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 1; // Gán giá trị 1 cho mỗi viên gạch (tất cả các viên gạch ban đầu đều tồn tại)
            }
        }
        // Tính toán kích thước của từng viên gạch dựa trên kích thước màn hình
        bricksWidth = 540 / col; // Tổng chiều rộng là 540, chia đều cho số cột
        bricksHeight = 150 / row; // Tổng chiều cao là 150, chia đều cho số hàng
    }

    // Phương thức để vẽ bản đồ các viên gạch lên màn hình
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) { // Lặp qua từng hàng
            for (int j = 0; j < map[0].length; j++) { // Lặp qua từng cột
                if (map[i][j] > 0) { // Chỉ vẽ những viên gạch có giá trị lớn hơn 0 (tồn tại)
                    
                    // Hiệu ứng gradient (màu chuyển đổi từ đỏ sang cam)
                    GradientPaint gradient = new GradientPaint(
                        j * bricksWidth + 80, // Tọa độ x của viên gạch
                        i * bricksHeight + 50, // Tọa độ y của viên gạch
                        Color.RED, // Màu bắt đầu (đỏ)
                        j * bricksWidth + 80 + bricksWidth, // Tọa độ x kết thúc của gradient
                        i * bricksHeight + 50 + bricksHeight, // Tọa độ y kết thúc của gradient
                        Color.ORANGE // Màu kết thúc (cam)
                    );
                    g.setPaint(gradient); // Thiết lập hiệu ứng gradient
                    g.fillRoundRect(j * bricksWidth + 80, i * bricksHeight + 50, bricksWidth, bricksHeight, 10, 10);
                    // Vẽ viên gạch với các góc bo tròn (bán kính 10)

                    // Vẽ đường viền cho mỗi viên gạch
                    g.setStroke(new BasicStroke(3)); // Độ dày của đường viền là 3px
                    g.setColor(Color.BLACK); // Màu của đường viền là đen
                    g.drawRoundRect(j * bricksWidth + 80, i * bricksHeight + 50, bricksWidth, bricksHeight, 10, 10);

                    // Vẽ bóng đổ cho viên gạch
                    g.setColor(new Color(0, 0, 0, 50)); // Màu đen trong suốt (alpha 50)
                    g.fillRoundRect(
                        j * bricksWidth + 82, // Tọa độ x của bóng đổ (dời sang phải 2px)
                        i * bricksHeight + 52, // Tọa độ y của bóng đổ (dời xuống 2px)
                        bricksWidth - 4, // Chiều rộng nhỏ hơn viên gạch 4px
                        bricksHeight - 4, // Chiều cao nhỏ hơn viên gạch 4px
                        10, // Bán kính góc bo tròn
                        10 // Bán kính góc bo tròn
                    );
                }
            }
        }
    }

    // Phương thức để cập nhật giá trị của một viên gạch cụ thể
    public void setBricksValue(int value, int row, int col) {
        map[row][col] = value; // Thay đổi giá trị của viên gạch tại hàng `row` và cột `col`
    }
}
