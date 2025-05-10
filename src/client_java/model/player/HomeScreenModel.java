package client_java.model.player;

public class HomeScreenModel {
    // todo - Lobby id

/*
    FOLLOWING BLOCK OF CODE IS PSEUDOCODE FOR LOBBY
    NOTE: any mentioned sql statements must be executed on the server, not in model classes

    private String lobbyName;
    private int[] scoreKeep;
    private ArrayList<String> usedWords;
    private ArrayList<String> players;
    private boolean gameOngoing;

    public LobbyModel(String lobbyName, String player)  {
        this.lobbyName = lobbyName;
        players.add(player); //player to be added is the one who opened the room
        scoreKeep = new int[players.length];
        gameOngoing = false;
    }

    public boolean isGameOngoing() {
        return gameOngoing;
    }

    public void playerJoin(String player) {
        players.add(player);
    }

    public void beginGame() { //might be easier to rename Game to Round so it's not confusing? one game contains multiple rounds until one person reaches 3 points
        gameOngoing = true; //will be used to stop other players from joining
        boolean continueGame = true;
        while (continueGame) {
            startRound();
            for (int i = 0; i < scoreKeep.length; i++) {
                if (scoreKeep[i] == 3) //this follows the "first to 3" point rule, can be changed
                    closeLobby(players[i]);
            }
        }
    }

    public void startRound() {
        int rand = randInt;
        Statement stmt = "SELECT * FROM words" +
                "ORDER BY word DESC (< words.txt is alr alphabetically arranged but this is just to make sure hahah)" +
                "LIMIT 1" +
                "OFFSET ?;";
        ? = rand - 1; //i forgor how to do prepared statements but yea. SQL STATEMENTS WILL BE PLACED IN THE SERVER
        String wordToGuess = stmt.execute();
        new GameController(wordToGuess); //there should be one instance of a lobby for all players in said lobby, but each player has their own instance of the Game for each round
    }

    //endRound() is an overloaded method
    public void endRound() { //called if timer runs out
        call closeRound() for all game instances
        i'm not sure how to have this return to the for loop in beginGame() but if that's possible that's what happens here
    }

    public void endRound(String player) { //called if player wins
        call closeRound() for all game instances
        scoreKeep[players.indexOf(player)]++; //or something that will increment the player that won idk
        i'm not sure how to have this return to the for loop in beginGame() but if that's possible that's what happens here
    }
    
    public void closeLobby(String player) {
        show "game is ending" message?
        sql update player's winCount
        close lobby instance
    }
    
 */

}
