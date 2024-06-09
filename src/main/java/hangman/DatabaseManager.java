package hangman;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Use JDBC to connect to your database and run queries
public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "#M4360662491g";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createUser(String name, String username, String password) throws SQLException {
        String sql = "INSERT INTO UserInfo (name, username, password) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }

    public boolean loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM UserInfo WHERE username = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public void saveGame(String username, String word, int wrongGuesses, int time, boolean win) throws SQLException {
        String sql = "INSERT INTO GameInfo (Username, Word, WrongGuesses, Time, Win) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, word);
            pstmt.setInt(3, wrongGuesses);
            pstmt.setInt(4, time);
            pstmt.setBoolean(5, win);
            pstmt.executeUpdate();
        }
    }

    public List<PlayerRecord> getLeaderboard() throws SQLException {
        List<PlayerRecord> leaderboard = new ArrayList<>();
        String sql = "SELECT UserInfo.name, COUNT(GameInfo.Win) AS wins " +
                "FROM GameInfo INNER JOIN UserInfo ON GameInfo.Username = UserInfo.username " +
                "WHERE GameInfo.Win = TRUE " +
                "GROUP BY UserInfo.name " +
                "ORDER BY wins DESC";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                int wins = rs.getInt("wins");
                leaderboard.add(new PlayerRecord(name, wins));
            }
        }
        return leaderboard;
    }

    public List<GameRecord> getUserGameDetails(String username) throws SQLException {
        List<GameRecord> gameDetails = new ArrayList<>();
        String sql = "SELECT * FROM GameInfo WHERE Username = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GameRecord record = new GameRecord(
                        rs.getInt("GameID"),
                        rs.getString("Username"),
                        rs.getString("Word"),
                        rs.getInt("WrongGuesses"),
                        rs.getInt("Time"),
                        rs.getBoolean("Win")
                );
                gameDetails.add(record);
            }
        }
        return gameDetails;
    }
}

