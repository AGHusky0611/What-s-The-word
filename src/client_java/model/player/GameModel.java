package client_java.model.player;

public class GameModel {
    /*
    FOLLOWING BLOCK OF CODE IS PSEUDOCODE FOR GAME

    private String word;
    private int mistakes;
    private char[] shownWord;
    private String player;

    public GameModel(String word, String player) {
        this.word = word;
        this.player = player;
        mistakes = 0;
        shownWord = new char[word.length()];
        char guessChar;
        String checkWord;

        for(int i = 0; i < shownWord.length; i++)
            shownWord[i] = '_';

        while(mistakes < 5) {
            guessChar = take input from user through view (or controller?? idk)
            guess(guessChar);
            checkWord = new String(shownWord);
            if (!checkWord.contains("_"))
                endRound(player);
        }
        closeRound();
    }

    public void guess(char c) {
        if (word.contains(""+c))
            for(int i = 0; i < shownWord.length; i++)
                if (word.charAt(i) == c)
                    shownWord[i] = c;
        else
            mistakes++;
    }

    public void closeRound() {
        display correct word for 4secs
        close game instance
    }

     */
}
