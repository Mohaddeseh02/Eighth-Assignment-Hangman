package hangman;

public class PlayerRecord {
    private String username;
    private int totalWins;

    public PlayerRecord(String username, int totalWins) {
        this.username = username;
        this.totalWins = totalWins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }
}
