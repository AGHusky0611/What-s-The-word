package client_java.view.player;

import client_java.view.login.Login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// HOME SCREEN/LOBBIES

public class HomeScreenUI extends JFrame {

    String username = "Abrelle"; // placeholder
    JPanel transparentPanel;
    private DefaultListModel<String> playerListModel; // placeholder
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int countdown = 10;
    JButton logoutButton;

    public HomeScreenUI(String authToken, String validatedUsername) {
        // set frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // temporary
        setIconImage(new ImageIcon("Client_Java/src/main/res/logo/Hangman_Logo.jpg").getImage());
        setTitle("What's The Word?");
        setSize(1000, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // background of the frame
        ImageIcon backgroundIcon = new ImageIcon("Client_Java/src/main/res/images/background.png");
        Image backgroundImage = backgroundIcon.getImage();

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // header label
        JLabel header = new JLabel("<html><div style='text-align: " +
                "left;'>WELCOME TO “WHAT’S THE WORD?”" +
                "<br>Please join or create a lobby to start!</div></html>");
        header.setBounds(70,3,500,100);
        header.setForeground(new Color(174, 174, 174));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        // logout button
        logoutButton = createRoundedButton("logout", 30);
        logoutButton.setBounds(775,25,180,45);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.setBackground(new Color(49, 111, 124));
        logoutButton.setForeground(Color.black);
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        logoutButton.addActionListener(e -> {
            new Login();
            dispose();
        });

        // search field
        JTextField searchField = (JTextField) createRoundedField(false, 15, 40);
        searchField.setBounds(70,100,840,37);
        searchField.setText(" Search a lobby..");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            // when user clicks on the field
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(" Search a lobby..")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            // when user clicks outside the field
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(" Search a lobby..");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        transparentPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setColor(new Color(0x40FFFFFF, true));
                graphics.fillRect(0, 0, getWidth(), getHeight());
                graphics.dispose();
            }
        };
        transparentPanel.setLayout(new BoxLayout(transparentPanel, BoxLayout.Y_AXIS));
        transparentPanel.setOpaque(false);

        // to enable scrolling the panel
        JScrollPane scrollPane = new JScrollPane(transparentPanel);
        scrollPane.setBounds(70, 150, 840, 420);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));

        // leaderboard button
        JButton viewLeaderboardButton = new JButton("View Leaderboard");
        viewLeaderboardButton.setBounds(70,570,420,40);
        viewLeaderboardButton.setBackground(new Color(49, 111, 124));
        viewLeaderboardButton.setForeground(Color.black);
        viewLeaderboardButton.setFocusPainted(false);
        viewLeaderboardButton.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        viewLeaderboardButton.addActionListener(e -> {
            new LeaderboardUI();
            dispose();
        });

        // create lobby button
        JButton createLobbyButton = new JButton("Create Lobby");
        createLobbyButton.setBounds(490, 570, 420, 40);
        createLobbyButton.setBackground(new Color(49, 111, 124));
        createLobbyButton.setForeground(Color.black);
        createLobbyButton.setFocusPainted(false);
        createLobbyButton.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        createLobbyButton.addActionListener(e -> {

            // confirm button
            JDialog dialog = new JDialog();
            dialog.setTitle("Confirm");
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(null);
            dialog.getContentPane().setBackground(new Color(192, 192, 192));
            dialog.setModal(true);

            // name of lobby entered by the user
            JTextField nameField = (JTextField) createRoundedField(false, 15, 40);
            nameField.setBounds(50, 30, 300, 40);
            nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            nameField.setHorizontalAlignment(JTextField.LEFT);
            nameField.setText("Enter a name..");
            nameField.setForeground(Color.GRAY);
            nameField.addFocusListener(new java.awt.event.FocusAdapter() {
                // when user clicks on the field
                public void focusGained(java.awt.event.FocusEvent e1) {
                    if (nameField.getText().equals("Enter a name..")) {
                        nameField.setText("");
                        nameField.setForeground(Color.BLACK);
                    }
                }
                // when user clicks outside the field
                public void focusLost(java.awt.event.FocusEvent e2) {
                    if (nameField.getText().isEmpty()) {
                        nameField.setText("Enter a name..");
                        nameField.setForeground(Color.GRAY);
                    }
                }
            });
            dialog.add(nameField);

            // label
            JLabel confirmLabel = new JLabel("Create lobby?");
            confirmLabel.setBounds(130, 90, 200, 30);
            confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            dialog.add(confirmLabel);

            // cancel button
            JButton cancelButton = createRoundedButton("Cancel", 30);
            cancelButton.setBounds(50, 150, 120, 40);
            cancelButton.setBackground(new Color(161, 62, 62));
            cancelButton.setForeground(Color.white);
            cancelButton.setFocusPainted(false);
            cancelButton.setOpaque(false);
            cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            cancelButton.setBorder(BorderFactory.createEmptyBorder());
            cancelButton.addActionListener(e2 -> dialog.dispose());
            dialog.add(cancelButton);

            // yes button
            JButton yesButton = createRoundedButton("Yes", 30);
            yesButton.setBounds(230, 150, 120, 40);
            yesButton.setBackground(new Color(47, 123, 61));
            yesButton.setForeground(Color.white);
            yesButton.setFocusPainted(false);
            yesButton.setOpaque(false);
            yesButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            yesButton.setBorder(BorderFactory.createEmptyBorder());
            yesButton.addActionListener(e3 -> {
                String lobbyName = nameField.getText();
                // when user(host) creates a lobby
                if (!lobbyName.equals("") && !lobbyName.equals("Enter a name..")) {
                    JPanel createdLobbyRow = addLobbyRow(username, lobbyName, 1);
                    dialog.dispose();

                    // set frame
                    JDialog lobbyOfHost = new JDialog();
                    lobbyOfHost.setTitle("Lobby: " + lobbyName);
                    lobbyOfHost.setSize(600, 400);
                    lobbyOfHost.getContentPane().setBackground(new Color(192, 192, 192));
                    lobbyOfHost.setLocationRelativeTo(null);
                    lobbyOfHost.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    lobbyOfHost.setLayout(null);
                    lobbyOfHost.setModal(true);

                    // label
                    JLabel playersText = new JLabel("Players");
                    playersText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    playersText.setBounds(20, 20, 350, 30);
                    lobbyOfHost.add(playersText);

                    // list of players in the lobby
                    playerListModel = new DefaultListModel<>(); // placeholder
                    playerListModel.addElement(username); // placeholder
                    JList<String> playerList = new JList<>(playerListModel);
                    playerList.setBackground(new Color(0, 0, 0, 0));

                    // enables scrolling the panel
                    JScrollPane playerScrollPane = new JScrollPane(playerList);
                    playerScrollPane.setBounds(20, 70, 550, 150);
                    playerScrollPane.setBackground(new Color(0, 0, 0, 0));

                    lobbyOfHost.add(playerScrollPane);

                    // label
                    countdownLabel = new JLabel("loading...", SwingConstants.CENTER);
                    countdownLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    countdownLabel.setBounds(130, 240, 340, 30);
                    lobbyOfHost.add(countdownLabel);

                    // placeholderrrrss
                    countdown = 10;
                    countdownTimer = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            countdown--;
                            countdownLabel.setText("Please wait for " + countdown + "...");
                            if (countdown <= 0) {
                                countdownTimer.stop();
                                countdownLabel.setText("Time's up! Starting the game...");
                                transparentPanel.remove(createdLobbyRow);
                                transparentPanel.revalidate();
                                transparentPanel.repaint();
                                lobbyOfHost.dispose();
                                dispose();
                                new GameUI();
                            }
                        }
                    });
                    countdownTimer.start();

                    // start button
                    JButton startButton = createRoundedButton("Start", 30);
                    startButton.setBounds(330, 300, 120, 40);
                    startButton.setBackground(new Color(47, 123, 61));
                    startButton.setForeground(Color.white);
                    startButton.setFocusPainted(false);
                    startButton.setOpaque(false);
                    startButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    startButton.addActionListener(e4 -> {
                        countdownTimer.stop();
                        lobbyOfHost.dispose();
                        transparentPanel.remove(createdLobbyRow);
                        transparentPanel.revalidate();
                        transparentPanel.repaint();
                        dispose();
                        new GameUI();
                    });
                    lobbyOfHost.add(startButton);

                    // leave button
                    JButton leaveButton = createRoundedButton("Leave", 30);
                    leaveButton.setBounds(140, 300, 120, 40);
                    leaveButton.setBackground(new Color(161, 62, 62));
                    leaveButton.setForeground(Color.white);
                    leaveButton.setFocusPainted(false);
                    leaveButton.setOpaque(false);
                    leaveButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    leaveButton.addActionListener(e5 -> {
                        countdownTimer.stop();
                        lobbyOfHost.dispose();
                        transparentPanel.remove(createdLobbyRow);
                        transparentPanel.revalidate();
                        transparentPanel.repaint();
                    });
                    lobbyOfHost.add(leaveButton);

                    lobbyOfHost.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid lobby name.");
                }
            });
            dialog.add(yesButton);

            // when user clicks on the background, it loses focus on other elements
            dialog.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    dialog.requestFocusInWindow();
                }
            });

            // focuses on window instead of other elements
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowOpened(java.awt.event.WindowEvent e) {
                    dialog.requestFocusInWindow();
                }
            });

            dialog.setVisible(true);
        });

        // when user clicks on the background, it loses focus on other elements
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });

        panel.add(header);
        panel.add(logoutButton);
        panel.add(searchField);
        panel.add(scrollPane);
        panel.add(viewLeaderboardButton);
        panel.add(createLobbyButton);
        setContentPane(panel);

        setVisible(true);
        // to not immediately focus on any elements in the frame
        SwingUtilities.invokeLater(() -> {
            getContentPane().requestFocusInWindow();
        });
    }

    // to add a lobby row in the panel
    private JPanel addLobbyRow(String username, String lobbyName, int playerCount) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new GridLayout(1, 2));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        rowPanel.setBackground(new Color(186, 185, 185, 255));
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 40, 5, 40)
        ));

        JLabel userAndLobbyLabel = new JLabel("<html><div style='text-align: left;'>" + username + "<br>Lobby: " + lobbyName + "</div></html>");
        userAndLobbyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userAndLobbyLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel countLabel = new JLabel("players: " + String.valueOf(playerCount));
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        rowPanel.add(userAndLobbyLabel);
        rowPanel.add(countLabel);

        transparentPanel.add(rowPanel);
        transparentPanel.revalidate();
        transparentPanel.repaint();

        return rowPanel;
    }

    // a design for textfields and password fields
    private JComponent createRoundedField(boolean isPassword, int columns, int radius) {
        JComponent field;

        if (isPassword) {
            field = new JPasswordField(columns) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D graphics = (Graphics2D) g.create();
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setColor(getBackground());
                    graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                    super.paintComponent(g);
                    graphics.dispose();
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D graphics = (Graphics2D) g.create();
                    graphics.setColor(Color.GRAY);
                    graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                    graphics.dispose();
                }
            };
        } else {
            field = new JTextField(columns) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D graphics = (Graphics2D) g.create();
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setColor(getBackground());
                    graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                    super.paintComponent(g);
                    graphics.dispose();
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D graphics = (Graphics2D) g.create();
                    graphics.setColor(Color.GRAY);
                    graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                    graphics.dispose();
                }
            };
        }

        field.setOpaque(false);
        ((JComponent) field).setBorder(new EmptyBorder(5, 10, 5, 10));
        field.setBackground(Color.WHITE);
        return field;
    }

    // a design for buttons
    private JButton createRoundedButton(String text, int radius) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                super.paintComponent(g);
                graphics.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setColor(getForeground());
                graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                graphics.dispose();
            }
        };
        return button;
    }

    // logout button
    public JButton getLogoutButton() {
        return logoutButton;
    }

    // search lobby
    // view leaderboard button
    // join a lobby row
    // create lobby button
    // enter a name field
    // cancel button
    // yes button
    // start button
    // leave button
}

// clicking row to join lobby - function and UI - not yet created
// "no other player joined" - joption - not yet created
// "lobby does not exist" - joption - not yet created
// "game is still ongoing return to game" - joption - not yet created