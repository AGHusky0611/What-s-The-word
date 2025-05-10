package client_java.view.player;

import client_java.controller.player.LeaderboardController;
import client_java.model.player.LeaderboardModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// LEADERBOARD

public class LeaderboardUI extends JFrame {

    JPanel transparentPanel;
    JButton backButton;

    public LeaderboardUI() {
        LeaderboardModel model = new LeaderboardModel();
//        LeaderboardController controller = new LeaderboardController(this, model);

        // set frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("src/client_java/res/logo/Hangman_Logo.jpg").getImage());
        setTitle("What's The Word?");
        setSize(1000, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // background of the frame
        ImageIcon backgroundIcon = new ImageIcon("src/client_java/res/images/background.png");
        Image backgroundImage = backgroundIcon.getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // back button
        backButton = new JButton("<");
        backButton.setBounds(30, 30, 60, 30);
        backButton.setFont(new Font("Arial", Font.PLAIN, 30));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.white);
//        controller.getBackButtonAction();

        // "leaderboard" label
        JLabel leaderboardLabel = new JLabel("LEADERBOARD");
        leaderboardLabel.setBounds(405, 120, 200, 40);
        leaderboardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        leaderboardLabel.setForeground(Color.white);

        transparentPanel = new JPanel();
        transparentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setColor(new Color(0x40FFFFFF, true));
                graphics.fillRect(0, 0, getWidth(), getHeight());
                graphics.dispose();
            }
        };
        transparentPanel.setLayout(new BoxLayout(transparentPanel, BoxLayout.Y_AXIS));
        transparentPanel.setBounds(170, 170, 640, 300);
        transparentPanel.setOpaque(false);

        panel.add(backButton);
        panel.add(leaderboardLabel);
        panel.add(transparentPanel);
        setContentPane(panel);
        setVisible(true);

        // to not immediately focus on any elements in the frame
        SwingUtilities.invokeLater(() -> getContentPane().requestFocusInWindow());
    }

    // leaderboard rows
    private void addRow(JPanel contentPanel, String rank, String player, String wins) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setPreferredSize(new Dimension(640, 60));
        row.setMaximumSize(new Dimension(640, 60));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel rankPanel = new JPanel();
        rankPanel.setPreferredSize(new Dimension(60, 60));
        rankPanel.setMaximumSize(new Dimension(60, 60));
        rankPanel.setMinimumSize(new Dimension(60, 60));
        rankPanel.setLayout(new BorderLayout());
        rankPanel.setBackground(new Color(186, 185, 185, 255));
        rankPanel.setOpaque(true);

        JLabel rankLabel = new JLabel(rank, SwingConstants.CENTER);
        rankLabel.setFont(new Font("Segoe UI", Font.PLAIN, 27));
        rankLabel.setForeground(Color.black);
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rankLabel.setVerticalAlignment(SwingConstants.CENTER);

        rankPanel.add(rankLabel, BorderLayout.CENTER);

        JLabel playerLabel = new JLabel(player);
        playerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 27));
        playerLabel.setForeground(Color.black);
        playerLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel winsLabel = new JLabel(wins, SwingConstants.RIGHT);
        winsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 27));
        winsLabel.setForeground(Color.black);
        winsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

        row.add(rankPanel);
        row.add(Box.createRigidArea(new Dimension(10, 0)));
        row.add(playerLabel);
        row.add(Box.createHorizontalGlue());
        row.add(winsLabel);

        contentPanel.add(row);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void setLeaderBoardRanking(List<String> players, List<Integer> wins) {
        transparentPanel.removeAll();

        int[] ranks = {1, 2, 3, 4, 5};
        for (int i = 0; i <= ranks.length; i++) {
            addRow(transparentPanel, String.valueOf(ranks[i]), players.get(i), "Wins: " + wins.get(i));
        }

        transparentPanel.revalidate();
        transparentPanel.repaint();
    }

    public JButton getBackButton() {
        return backButton;
    }

    // temporary
    public static void main(String[] args) {
        new LeaderboardUI();
    }
}