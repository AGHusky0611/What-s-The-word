package client_java.controller.login;

import client_java.model.login.LoginModel;
import client_java.view.login.Login;
import client_java.view.player.HomeScreenUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private Login view;
    private LoginModel model;
    private String authToken;

    public LoginController(Login view, LoginModel model) {
        this.view = view;
        this.model = model;
        this.authToken = null;

        setupListeners();
    }

    public LoginController(Login view) {
        this.view = view;
        this.authToken = null;
        setupListeners();
    }

    private void setupListeners() {
        view.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = view.getUsername();
                char[] password = view.getPassword();

                // Validate input
                if (username.isEmpty() || username.equals("Username..") ||
                        password.length == 0 || new String(password).equals("Password..")) {
                    JOptionPane.showMessageDialog(view, "Please enter both username and password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Attempt login via model
                boolean loginSuccess = model.authenticate(username, password);

                if (loginSuccess) {
                    // Get and store the authentication token
                    authToken = model.getAuthToken();

                    // Verify the token is valid and get the username
                    String validatedUsername = model.getUsernameForToken();

                    if (validatedUsername != null && !validatedUsername.isEmpty()) {
                        view.dispose();
                        // Pass the token and username to the home screen
                        new HomeScreenUI(authToken, validatedUsername);
                    } else {
                        JOptionPane.showMessageDialog(view, "Authentication failed",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Invalid username or password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add window close listener to handle cleanup
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (authToken != null) {
                    model.logout();
                }
            }
        });
    }

    public String getAuthToken() {
        return authToken;
    }
}