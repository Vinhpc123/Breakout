package com.mycompany.brick;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MyApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame obj = new JFrame();
            obj.setBounds(10, 10, 700, 600);
            obj.setTitle("BrickBreaker");
            obj.setResizable(false);
            obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            StartScreen startScreen = new StartScreen(obj);
            obj.add(startScreen);
            
            obj.setVisible(true);
            obj.revalidate();
            obj.repaint();
        });
    }
}
