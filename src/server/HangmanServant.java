package server;

// ================= ORB ==================== //
import GameServer.GameServerInterfacePOA;
import org.omg.CORBA.ORB;

// ================ SQL ==================== //
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class HangmanServant extends GameServerInterfacePOA {
    private ORB orb;

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hangman";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection connection;

    // Store active tokens and their associated usernames
    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();
    // Store username to token mapping for quick lookup
    private final Map<String, String> usernameToToken = new ConcurrentHashMap<>();

    public HangmanServant() {
        super();
        try {
            // Initialize database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
        orb.shutdown(false);
    }

    @Override
    public String authenticate(String username, String password) {
        boolean authenticated = verifyCredentials(username, password);

        if (!authenticated) {
            return "authentication failed";
        }

        // If user is already logged in, log them out first
        if (usernameToToken.containsKey(username)) {
            String oldToken = usernameToToken.get(username);
            tokenToUsername.remove(oldToken);
            usernameToToken.remove(username);
        }

        // Generate new token
        String token = generateUniqueToken();

        // Store the mappings
        tokenToUsername.put(token, username);
        usernameToToken.put(username, token);

        return token;
    }

    @Override
    public void logout(String token) {
        String username = tokenToUsername.get(token);
        if (username != null) {
            tokenToUsername.remove(token);
            usernameToToken.remove(username);
        }
    }

    @Override
    public boolean verifyCredentials(String username, String password) {
        try (CallableStatement stmt = connection.prepareCall("{CALL retrieveAllPlayers()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                if (dbUsername.equals(username) && dbPassword.equals(password)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying credentials: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String validateToken(String token) {
        return tokenToUsername.getOrDefault(token, "");
    }

    @Override
    public String createGame(String username) {
        String gameId = "game_" + System.currentTimeMillis();
        try (CallableStatement stmt = connection.prepareCall("{CALL createLobby(?)}")) {
            stmt.setString(1, gameId);
            stmt.execute();

            // Join the creator to the lobby
            try (CallableStatement joinStmt = connection.prepareCall("{CALL joinLobby(?, ?)}")) {
                joinStmt.setString(1, username);
                joinStmt.setString(2, gameId);
                joinStmt.execute();
            }
            return gameId;
        } catch (SQLException e) {
            System.err.println("Error creating game: " + e.getMessage());
            return "";
        }
    }

    @Override
    public boolean joinGame(String username, String gameId) {
        try (CallableStatement stmt = connection.prepareCall("{CALL joinLobby(?, ?)}")) {
            stmt.setString(1, username);
            stmt.setString(2, gameId);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error joining game: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void cancelGame(String username, String gameId) {
        try (CallableStatement stmt = connection.prepareCall("{CALL closeLobby(?)}")) {
            stmt.setString(1, gameId);
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Error canceling game: " + e.getMessage());
        }
    }

    @Override
    public boolean submitGuess(String username, String gameId, char letter) {
        // This would need more complex logic to check if the guess is correct
        // For now, we'll just increment the score
        try (CallableStatement stmt = connection.prepareCall("{CALL incrementScoreCount(?, ?)}")) {
            stmt.setString(1, username);
            stmt.setString(2, gameId);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error submitting guess: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getGameStatus(String username, String gameId) {
        // This should return the current game state (word, guesses, etc.)
        // Simplified for this example
        return "Game in progress";
    }

    @Override
    public String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder();
        try (CallableStatement stmt = connection.prepareCall("{CALL retrieveLeaderboard()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                leaderboard.append(rs.getString("username"))
                        .append(": ")
                        .append(rs.getInt("winCount"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Error getting leaderboard: " + e.getMessage());
            return "Error retrieving leaderboard";
        }
        return leaderboard.toString();
    }

    @Override
    public boolean addPlayer(String adminUsername, String username, String password) {
        // First verify admin credentials
        if (!isAdmin(adminUsername)) return false;

        try (CallableStatement stmt = connection.prepareCall("{CALL createPlayer(?, ?)}")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding player: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removePlayer(String adminUsername, String username) {
        if (!isAdmin(adminUsername)) return false;

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM player WHERE username = ?")) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing player: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePlayer(String adminUsername, String username, String newPassword) {
        if (!isAdmin(adminUsername)) return false;

        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE player SET password = ? WHERE username = ?")) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating player: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String searchPlayer(String adminUsername, String searchTerm) {
        if (!isAdmin(adminUsername)) return "Unauthorized";

        StringBuilder result = new StringBuilder();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT username FROM player WHERE username LIKE ?")) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.append(rs.getString("username")).append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Error searching players: " + e.getMessage());
            return "Error searching players";
        }
        return result.toString();
    }

    @Override
    public boolean setWaitingTime(String adminUsername, int seconds) {
        // This would be stored in a configuration table
        if (!isAdmin(adminUsername)) return false;
        return true;
    }

    @Override
    public boolean setRoundDuration(String adminUsername, int seconds) {
        // This would be stored in a configuration table
        if (!isAdmin(adminUsername)) return false;
        return true;
    }

    private boolean isAdmin(String username) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM admin WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if admin exists
        } catch (SQLException e) {
            System.err.println("Error verifying admin: " + e.getMessage());
            return false;
        }
    }
}