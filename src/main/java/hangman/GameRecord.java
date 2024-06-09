package hangman;

public class GameRecord {
    private int gameID;
    private String username;
    private String word;
    private int wrongGuesses;
    private int time;
    private boolean win;

    public GameRecord(int gameID, String username, String word, int wrongGuesses, int time, boolean win) {
        this.gameID = gameID;
        this.username = username;
        this.word = word;
        this.wrongGuesses = wrongGuesses;
        this.time = time;
        this.win = win;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
