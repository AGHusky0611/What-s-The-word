package serverMain;

import Server.CommonObjects.*;
import Server.Exceptions.*;
import Server.PlayerSide.GameSessionPOA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameImpl extends GameSessionPOA {
    private final String lobbyId;
    private final User host;
    private final GameRules rules;
    private final List<User> players = new ArrayList<>();
    private boolean gameStarted = false;
    private String currentWord;
    private String hiddenWord;
    private int remainingGuesses;
    private int currentPlayerIndex = 0;
    private final List<Character> guessedLetters = new ArrayList<>();

    public GameImpl(String lobbyId, User host, GameRules rules) {
        this.lobbyId = lobbyId;
        this.host = host;
        this.rules = rules;
        this.players.add(host);
        this.remainingGuesses = rules.maxGuesses;
    }

    @Override
    public void startGame() throws LostConnectionException, NotLoggedInException, NotEnoughPlayersException {
        if (players.size() < rules.minPlayers) {
            throw new NotEnoughPlayersException("Need at least " + rules.minPlayers + " players to start");
        }
        this.gameStarted = true;
        this.currentWord = selectRandomWord();
        this.hiddenWord = currentWord.replaceAll(".", "_");
        this.currentPlayerIndex = 0;
        this.guessedLetters.clear();
    }

    @Override
    public void endGame() throws SessionNotFoundException, LostConnectionException, NotLoggedInException {

    }

    private String selectRandomWord() {
        // Todo - implement random word selection from database

        return "HANGMAN"; // Placeholder
    }

    @Override
    public void guess(String guessingPlayer, char letter)
            throws WrongGuessException, LostConnectionException, NotLoggedInException {
        if (!gameStarted) {
            throw new WrongGuessException("Game hasn't started yet");
        }

        if (guessedLetters.contains(Character.toUpperCase(letter))) {
            throw new WrongGuessException("Letter already guessed");
        }

        guessedLetters.add(Character.toUpperCase(letter));

        boolean correctGuess = currentWord.toUpperCase().indexOf(Character.toUpperCase(letter)) >= 0;

        if (!correctGuess) {
            remainingGuesses--;
        } else {
            updateHiddenWord(letter);
        }

        if (hiddenWord.equals(currentWord)) {
            endGame(guessingPlayer);
        } else if (remainingGuesses <= 0) {
            endGame(null);
        } else {
            nextPlayerTurn();
        }
    }

    private void updateHiddenWord(char letter) {
        StringBuilder newHidden = new StringBuilder(hiddenWord);
        for (int i = 0; i < currentWord.length(); i++) {
            if (Character.toUpperCase(currentWord.charAt(i)) == Character.toUpperCase(letter)) {
                newHidden.setCharAt(i, currentWord.charAt(i));
            }
        }
        hiddenWord = newHidden.toString();
    }

    @Override
    public String getCurrentWordState() throws LostConnectionException, NotLoggedInException {
        return hiddenWord;
    }

    @Override
    public String selectWord() throws LostConnectionException, NotLoggedInException {
        return currentWord;
    }

    @Override
    public void addPlayer(User user) throws SessionFullException, PlayerAlreadyInSessionException,
            UserIsBannedException, LostConnectionException {
        if (players.size() >= rules.maxPlayers) {
            throw new SessionFullException("Lobby is full");
        }
        if (players.stream().anyMatch(p -> p.userId.equals(user.userId))) {
            throw new PlayerAlreadyInSessionException("Player already in lobby");
        }
        players.add(user);
    }

    @Override
    public void removePlayer(String playerId) throws NoSuchUserFoundException, LostConnectionException {
        players.removeIf(p -> p.userId.equals(playerId));
    }

    @Override
    public String ping(User user) throws LostConnectionException, NoSuchUserFoundException {
        return "pong";
    }

    private void nextPlayerTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void endGame(String winner) {
        // Game end logic
    }
}