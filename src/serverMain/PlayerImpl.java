package serverMain;

import Server.CommonInterface.CallBackInterface;
import Server.CommonObjects.*;
import Server.Exceptions.*;
import Server.PlayerSide.*;
import Server.PlayerSide.PlayerInterfacePOA;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerImpl extends PlayerInterfacePOA {
    private static final Logger logger = Logger.getLogger(PlayerImpl.class.getName());
    private Connection connection;
    private final Map<String, GameSession> activeLobbies = new ConcurrentHashMap<>();
    private final Map<String, User> activeUsers = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> bannedUsers = new ConcurrentHashMap<>();

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hangman";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public PlayerImpl() {
        initializeDatabaseConnection();
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    DB_URL + "?useSSL=false&autoReconnect=true",
                    DB_USER, DB_PASSWORD);
            logger.info("Database connection established");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    @Override
    public String login(CallBackInterface cb, String username, String password)
            throws LostConnectionException, AlreadyLoggedInException {
        try {
            // Check if user is already logged in
            if (activeUsers.values().stream().anyMatch(u -> u.displayName.equals(username))) {
                throw new AlreadyLoggedInException("User already logged in");
            }

            // Authenticate against database
            User user = authenticateUser(username, password);
            if (user != null) {
                String token = UUID.randomUUID().toString();
                user.userId = token; // Store token as userId
                activeUsers.put(token, user);
                logger.log(Level.INFO, "User {0} logged in successfully", username);
                return token;
            }
            throw new AlreadyLoggedInException("Invalid credentials");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during login", e);
            throw new LostConnectionException("Database connection issue");
        }
    }

    private User authenticateUser(String username, String password) throws SQLException {
        // todo - implement user authentication against database
        String query = "SELECT username FROM player WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(username, "", password, "player");
            }
            return null;
        }
    }

    @Override
    public GameSession createLobby(User host, String lobbyName, GameRules rules) throws LostConnectionException, NotLoggedInException, SessionAlreadyMadeException {

        //todo - implement lobby creation against database
//        validateUser(host.userId);
//
//        synchronized (activeLobbies) {
//            if (activeLobbies.containsKey(lobbyName)) {
//                throw new SessionAlreadyMadeException("Lobby " + lobbyName + " already exists");
//            }
//
//            GameImpl newGame = new GameImpl(lobbyName, host, rules);
//            activeLobbies.put(lobbyName, newGame);
//            logger.log(Level.INFO, "Lobby {0} created by {1}", new Object[]{lobbyName, host.displayName});
//            return newGame;
//        }
        return null;
    }

    @Override
    public GameSession joinLobby(String lobbyId, User player)
            throws LostConnectionException, SessionNotFoundException,
            SessionFullException, PlayerAlreadyInSessionException,
            UserIsBannedException, NotLoggedInException {

        validateUser(player.userId);

        synchronized (activeLobbies) {
            GameSession session = activeLobbies.get(lobbyId);
            if (session == null) {
                throw new SessionNotFoundException("Lobby not found");
            }

            if (isBanned(player.userId, lobbyId)) {
                throw new UserIsBannedException("User is banned from this lobby");
            }

            try {
                session.addPlayer(player);
                return session;
            } catch (Exception e) {
                throw new LostConnectionException("Error joining lobby: " + e.getMessage());
            }
        }
    }

    @Override
    public LobbyInfo[] getAvailableLobbies() throws LostConnectionException {
        //todo - implement lobby listing against database

//        try {
//            return activeLobbies.entrySet().stream()
//                    .map(entry -> {
//                        GameImpl game = (GameImpl) entry.getValue();
//                        return new LobbyInfo(
//                                entry.getKey(),
//                                game.getHost().displayName,
//                                game.getPlayerCount(),
//                                game.getRules().maxPlayers,
//                                false, // isPrivate
//                                false  // gameStarted
//                        );
//                    })
//                    .toArray(LobbyInfo[]::new);
//        } catch (Exception e) {
//            throw new LostConnectionException("Error getting lobbies: " + e.getMessage());
//        }
        return new LobbyInfo[0];
    }

    @Override
    public String getWinRatio(String playerId) throws LostConnectionException, NoSuchUserFoundException, NotLoggedInException {
        //todo - implement win ratio against database
        validateUser(playerId);
        try {
            String query = "SELECT " +
                    "(SELECT COUNT(*) FROM lobby WHERE winner = ?) AS wins, " +
                    "(SELECT COUNT(*) FROM playerinlobby WHERE username = ?) AS total";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, activeUsers.get(playerId).displayName);
                stmt.setString(2, activeUsers.get(playerId).displayName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int wins = rs.getInt("wins");
                    int total = rs.getInt("total");
                    if (total == 0) return "0%";
                    return String.format("%.0f%%", (wins * 100.0 / total));
                }
            }
            return "0%";
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error getting win ratio", e);
            throw new LostConnectionException("Database connection issue");
        }
    }

    @Override
    public GameResult[] getGameHistory(String playerId) throws LostConnectionException, NotLoggedInException, NoSuchUserFoundException {
    //todo - implement game history against database
        //        validateUser(playerId);
//        try {
//            String query = "SELECT l.lobbyID, l.winner FROM lobby l " +
//                    "JOIN playerinlobby p ON l.lobbyID = p.lobbyID " +
//                    "WHERE p.username = ? AND l.gameStarted = TRUE";
//            try (PreparedStatement stmt = connection.prepareStatement(query)) {
//                stmt.setString(1, activeUsers.get(playerId).displayName);
//                ResultSet rs = stmt.executeQuery();
//                List<GameResult> results = new ArrayList<>();
//                while (rs.next()) {
//                    String[] players = getPlayersInLobby(rs.getInt("lobbyID"));
//                    results.add(new GameResult(
//                            String.valueOf(rs.getInt("lobbyID")),
//                            rs.getString("winner"),
//                            players,
//                            0, // roundsPlayed - would need to track this
//                            0, // totalGuessesMade
//                            0  // duration
//                    ));
//                }
//                return results.toArray(new GameResult[0]);
//            }
//        } catch (SQLException e) {
//            logger.log(Level.SEVERE, "Database error getting game history", e);
//            throw new LostConnectionException("Database connection issue");
//        }
        return new GameResult[0];
    }


    @Override
    public void logout(String userId) throws LostConnectionException, NotLoggedInException {
        validateUser(userId);
        activeUsers.remove(userId);
        logger.log(Level.INFO, "User {0} logged out", userId);
    }

    @Override
    public String ping(String userId) throws LostConnectionException, NotLoggedInException {
        validateUser(userId);
        return "pong";
    }

    // Helper methods
    private void validateUser(String userId) throws NotLoggedInException {
        if (!activeUsers.containsKey(userId)) {
            throw new NotLoggedInException("User not logged in");
        }
    }

    private boolean isBanned(String userId, String lobbyId) {
        return bannedUsers.getOrDefault(lobbyId, Collections.emptySet()).contains(userId);
    }

    // Other implemented methods
    @Override
    public GameSession randomConnect(User userId) throws LostConnectionException,
            NoSessionsFoundException, SessionFullException,
            PlayerAlreadyInSessionException, NotLoggedInException {
        // Implementation for random lobby joining
        return null;
    }

    @Override
    public void disconnect(String sessionId, String playerId)
            throws LostConnectionException, SessionNotFoundException, NotLoggedInException {
        // todo - Implementation for disconnecting from lobby
    }
}