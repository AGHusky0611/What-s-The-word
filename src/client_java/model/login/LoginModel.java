package client_java.model.login;

import GameServer.GameServerInterface;
import client_java.utils.CORBAConnector;

import javax.swing.*;
import java.util.Arrays;

public class LoginModel {
    private GameServerInterface server;
    private String authToken; // Store the authentication token

    public LoginModel() {
        try {
            server = CORBAConnector.getGameServerRef();

            if (server == null) {
                throw new Exception("Failed to connect to server");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean authenticate(String username, char[] password) {
        try {
            if (server == null) {
                throw new Exception("Not connected to server");
            }else {
                System.out.println("authenticating user " + username + " with password " + Arrays.toString(password));
            }

            // Call the remote authentication method
            String token = server.authenticate(username, new String(password));

            // Clear password from memory immediately after use
            Arrays.fill(password, '0');

            // Check if authentication was successful
            if (token == null || token.isEmpty() || token.equals("authentication failed")) {
                return false;
            }

            // Store the token for future authenticated calls
            this.authToken = token;
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Authentication error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsernameForToken() {
        try {
            if (server == null || authToken == null) {
                return null;
            }
            return server.validateToken(authToken);
        } catch (Exception e) {
            return null;
        }
    }

    public void logout() {
        try {
            if (server != null && authToken != null) {
                server.logout(authToken);
                authToken = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Logout error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}