package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class PreviousGamesController {

    @FXML
    private TableView<GameRecord> table;

    @FXML
    private TableColumn<GameRecord, Integer> gameIdColumn;

    @FXML
    private TableColumn<GameRecord, String> usernameColumn;

    @FXML
    private TableColumn<GameRecord, String> wordColumn;

    @FXML
    private TableColumn<GameRecord, Integer> wrongGuessesColumn;

    @FXML
    private TableColumn<GameRecord, Integer> timeColumn;

    @FXML
    private TableColumn<GameRecord, Boolean> winColumn;

    private DatabaseManager databaseManager = new DatabaseManager();
    private GameRecord currentUser;

    @FXML
    public void initialize() {

        try {
            List<GameRecord> previousGames = databaseManager.getUserGameDetails("Ali");
            table.getItems().addAll(previousGames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
