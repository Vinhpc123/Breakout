package com.mycompany.brick;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartScreen extends JPanel {

    private Image backgroundImage; // Background image
    private Image musicOnIcon; // Music on icon
    private Image musicOffIcon; // Music off icon
    private Clip backgroundMusic; // Background music
    private boolean isMusicOn = true; // Music status (on/off)
    private Rectangle musicToggleArea; // Area to click to toggle music

    public StartScreen(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 40));

        // Load background image from resources
        backgroundImage = loadImage("resources/images/backgr.png");

        // Load music on/off icons
        musicOnIcon = loadImage("resources/images/musicon.png");
        musicOffIcon = loadImage("resources/images/musicoff.png");

        // Click area to toggle music
        musicToggleArea = new Rectangle(650, 10, 30, 30);

        // Play background music from resources
        playMusic("resources/sounds/MainMenuBGM.wav");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel title = new JLabel("Brick Breaker", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 48));
        title.setForeground(new Color(250, 247, 240));
        add(title, gbc);

        // "Start Game" button
        JButton startButton = createStyledButton("Start Game", frame);
        gbc.gridy = 1;
        add(startButton, gbc);

        // "Instructions" button
        JButton instructionsButton = createStyledInstructionsButton("Instructions", frame);
        gbc.gridy = 2;
        add(instructionsButton, gbc);

        // "Exit Game" button
        JButton exitButton = createStyledExitButton("Exit Game");
        gbc.gridy = 3;
        add(exitButton, gbc);

        // Listen for mouse events
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (musicToggleArea.contains(e.getPoint())) {
                    toggleMusic();
                }
            }
        });
    }

    // Create "Start Game" button
    private JButton createStyledButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button);
        button.setPreferredSize(new Dimension(170, 55));
        button.addActionListener(e -> {
            stopMusic();
            GamePlay gameplay = new GamePlay(frame);
            frame.getContentPane().removeAll();
            frame.add(gameplay);
            frame.revalidate();
            frame.repaint();
            gameplay.requestFocusInWindow();
        });
        return button;
    }

    // Create "Instructions" button
    private JButton createStyledInstructionsButton(String text, JFrame frame) {
        JButton button = new JButton(text);
        styleButton(button);
        button.setPreferredSize(new Dimension(170, 55));
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Use the Left and Right arrow keys to move the paddle.\nBreak all the bricks to win!",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });
        return button;
    }

    // Create "Exit Game" button
    private JButton createStyledExitButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        button.setPreferredSize(new Dimension(170, 55));
        button.addActionListener(e -> {
            stopMusic();
            System.exit(0);
        });
        return button;
    }

    // Style button
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 24));
        button.setBackground(new Color(154, 126, 111));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(154, 126, 111), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 130, 230));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(154, 126, 111));
            }
        });
    }

    // Play background music
    private void playMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Music file not found: " + musicFile.getAbsolutePath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (Exception e) {
            System.err.println("Error playing music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Stop background music
    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    // Toggle background music
    private void toggleMusic() {
        if (isMusicOn) {
            stopMusic();
            isMusicOn = false;
        } else {
            playMusic("D://Java/Brick-Breaker-Game-master/resources/sounds/MainMenuBGM.wav");
            isMusicOn = true;
        }
        repaint(); // Redraw to update icon
    }

    // Load image
    private Image loadImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            return Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
        } else {
            System.err.println("Image not found: " + path);
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw music on/off icon
        if (isMusicOn) {
            g.drawImage(musicOnIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        } else {
            g.drawImage(musicOffIcon, musicToggleArea.x, musicToggleArea.y, musicToggleArea.width, musicToggleArea.height, this);
        }
    }
}
