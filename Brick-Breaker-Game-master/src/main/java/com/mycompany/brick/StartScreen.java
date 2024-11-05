package com.mycompany.brick;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartScreen extends JPanel {

    public StartScreen(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30)); // Background
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding around components
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Title label
        JLabel title = new JLabel("Brick Breaker", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(new Color(255, 215, 0)); // Gold color
        add(title, gbc);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Serif", Font.BOLD, 24));
        startButton.setBackground(new Color(70, 130, 180)); // Steel blue button color
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false); // Remove focus border
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Button action to start the game
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePlay gameplay = new GamePlay();
                frame.getContentPane().removeAll();
                frame.add(gameplay);
                frame.revalidate();
                frame.repaint();
                
                gameplay.requestFocusInWindow();
            }
        });
        
        gbc.gridy = 1;
        add(startButton, gbc);


        
    }
}
