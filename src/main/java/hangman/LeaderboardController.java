package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class LeaderboardController {

    @FXML
    private TableView<PlayerRecord> table;

    @FXML
    private TableColumn<PlayerRecord, String> usernameColumn;

    @FXML
    private TableColumn<PlayerRecord, Integer> totalWinsColumn;

    private DatabaseManager databaseManager = new DatabaseManager();

    @FXML
    public void initialize() {
        try {
            List<PlayerRecord> leaderboard = databaseManager.getLeaderboard();
            System.out.println(leaderboard);
            table.getItems().addAll(leaderboard);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void moveMenuPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
