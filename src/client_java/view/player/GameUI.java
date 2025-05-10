package client_java.view.player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GAME INTERFACE

public class GameUI extends JFrame {

    private String targetWord = "plane";  // placeholder
    private int currentRow = 0; // which row user is in
    private int wordLength = 5; // placeholder
    private int maxRows = 5;

    public GameUI() {
        // set up frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // temporary
        setIconImage(new ImageIcon("src/client_java/res/logo/Hangman_Logo.jpg").getImage());
        setTitle("What's The Word?");
        setSize(2000, 1000);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

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

        JLabel roundNum = new JLabel("Round " + "1"); // placeholder
        roundNum.setBounds(80, 50, 300, 40);
        roundNum.setFont(new Font("Segoe UI", Font.PLAIN, 35));
        roundNum.setForeground(Color.white);

        JLabel countdown = new JLabel("30"); // placeholder
        countdown.setBounds(950, 45, 200, 80);
        countdown.setFont(new Font("Segoe UI", Font.PLAIN, 70));
        countdown.setForeground(Color.white);

        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new GridLayout(maxRows, wordLength, 10, 10));
        wordPanel.setBounds(100, 160, wordLength * 100, maxRows * 100);
        wordPanel.setOpaque(false);

        int frameWidth = getWidth();
        int wordPanelWidth = wordLength * 70;
        int wordPanelHeight = maxRows * 70;
        int x = (frameWidth - wordPanelWidth) / 2;

        wordPanel.setBounds(x, 220, wordPanelWidth, wordPanelHeight);

        // letter boxes
        JLabel[][] letterBoxes = new JLabel[maxRows][wordLength];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < wordLength; j++) {
                JLabel letterBox = new JLabel("", SwingConstants.CENTER);
                letterBox.setPreferredSize(new Dimension(50, 100));
                letterBox.setOpaque(true);
                letterBox.setBackground(Color.WHITE);
                letterBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                letterBox.setFont(new Font("Segoe UI", Font.BOLD, 40));
                letterBoxes[i][j] = letterBox;
                wordPanel.add(letterBox);
            }
        }

        // where the player will input their answer
        JTextField answerField = (JTextField) createRoundedField(false, 15, 40);
        answerField.setBounds(440, 700, 1120, 60);
        answerField.setText("ANSWER..");
        answerField.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        answerField.setForeground(Color.GRAY);
        answerField.setBackground(new Color(30, 30, 30, 255));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (answerField.getText().equals("ANSWER..")) {
                    answerField.setText("");
                    answerField.setForeground(Color.white);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (answerField.getText().isEmpty()) {
                    answerField.setText("ANSWER..");
                    answerField.setForeground(Color.GRAY);
                }
            }
        });
        answerField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String guess = answerField.getText().trim().toLowerCase();

                if (guess.length() != targetWord.length()) {
                    JOptionPane.showMessageDialog(null, "Guess must be " + targetWord.length() + " letters.");
                    return;
                }

                updateLetterBoxes(guess, targetWord, currentRow, letterBoxes);

                if (guess.equalsIgnoreCase(targetWord)) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You guessed the word.");
                    answerField.setEditable(false);
                } else {
                    currentRow++;
                    if (currentRow == maxRows) {
                        JOptionPane.showMessageDialog(null, "Game Over! The word was: " + targetWord);
                        answerField.setEditable(false);
                    }
                }
                answerField.setText("");
            }
        });

        // to make input automatic uppercase
        ((AbstractDocument) answerField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text != null) {
                    super.insertString(fb, offset, text.toUpperCase(), attr);
                }
            }

            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ImageIcon image = new ImageIcon("Client_Java/src/main/res/images/stage1.png"); // stage 1 hangman
        JLabel stage1 = new JLabel(image);
        stage1.setBounds(1550, 100, 450, 650);

        JLabel triesLeft = new JLabel("Tries left: " + "5"); // placeholder
        triesLeft.setBounds(90, 750, 300, 45);
        triesLeft.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        triesLeft.setForeground(Color.white);

        JLabel roundsWon = new JLabel("Rounds won: " + "0"); // placeholder
        roundsWon.setBounds(90, 800, 400, 45);
        roundsWon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        roundsWon.setForeground(Color.white);

        JLabel tip = new JLabel("Tip: be the first to win 3 rounds");
        tip.setBounds(1600, 850, 400, 40);
        tip.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        tip.setForeground(new Color(141, 141, 141, 255));

        panel.add(roundNum);
        panel.add(countdown);
        panel.add(wordPanel);
        panel.add(answerField);
        panel.add(stage1);
        panel.add(triesLeft);
        panel.add(roundsWon);
        panel.add(tip);

        // when user clicks on the background, it loses focus on other elements
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });
        setContentPane(panel);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            getContentPane().requestFocusInWindow();
        });
    }

    // change colors of boxes
    public void updateLetterBoxes(String guessWord, String targetWord, int currentRow, JLabel[][] letterBoxes) {
        guessWord = guessWord.toLowerCase();
        targetWord = targetWord.toLowerCase();

        boolean[] letterMatched = new boolean[targetWord.length()];

        // GREEN -> correct letter and position
        for (int i = 0; i < guessWord.length(); i++) {
            char guessedChar = guessWord.charAt(i);
            JLabel box = letterBoxes[currentRow][i];
            box.setText(String.valueOf(Character.toUpperCase(guessedChar)));

            if (guessedChar == targetWord.charAt(i)) {
                box.setBackground(Color.GREEN);
                letterMatched[i] = true;
            } else {
                box.setBackground(Color.LIGHT_GRAY);
            }
        }

        // YELLOW -> correct letter but wrong position
        for (int i = 0; i < guessWord.length(); i++) {
            char guessedChar = guessWord.charAt(i);
            JLabel box = letterBoxes[currentRow][i];

            if (box.getBackground() == Color.GREEN) continue;

            for (int j = 0; j < targetWord.length(); j++) {
                if (!letterMatched[j] && guessedChar == targetWord.charAt(j)) {
                    box.setBackground(Color.YELLOW);
                    letterMatched[j] = true;
                    break;
                }
            }
        }
    }

    // a design for textfields and password fields
    private JComponent createRoundedField(boolean isPassword, int columns, int radius) {
        JComponent field;

        if (isPassword) {
            // password field
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
            // textfield
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

    // temporary
    public static void main(String[] args) {
        GameUI run = new GameUI();
    }
}

// the changing of images for every wrong guess
// congrats joption not yet created (for winning a game)
// nice try joption not yet created (for winning a game)