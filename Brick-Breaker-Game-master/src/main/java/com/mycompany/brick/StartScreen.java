package com.mycompany.brick;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartScreen extends JPanel {
    public StartScreen(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Brick Breaker", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(new Color(255, 140, 0));
        add(title, gbc);

        JButton startButton = createStyledButton("Start Game", frame);
        gbc.gridy = 1;
        add(startButton, gbc);

        JButton exitButton = createStyledExitButton("Exit Game");
        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    private JButton createStyledButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            GamePlay gameplay = new GamePlay(frame);
            frame.getContentPane().removeAll();
            frame.add(gameplay);
            frame.revalidate();
            frame.repaint();
            gameplay.requestFocusInWindow();
        });
        return button;
    }

    private JButton createStyledExitButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 24));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 170, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 250));
            }
        });
    }
}
