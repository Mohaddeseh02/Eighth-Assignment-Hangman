package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuPageController {
    @FXML
    private Text text;
    private GameRecord currentGameRecord;

    public void setGameRecord(GameRecord gameRecord) {
        this.currentGameRecord = gameRecord;
    }


    @FXML
    void handleLeaderBoard(ActionEvent event) {

        loadPage("Leaderboard.fxml");
    }

    @FXML
    void handlePreviousGames(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("previousgames.fxml"));
        Parent root = loader.load();
        try {
            PreviousGamesController previousGamesController = loader.getController();
            previousGamesController.setCurrentUser(currentGameRecord);
            previousGamesController.initPG();
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = (Stage) text.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void handleStartGame(ActionEvent event) {
//        loadPage("hangman-view.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hangman-view.fxml"));
            Parent root = loader.load();
            HangmanController hangmanController = loader.getController();
            hangmanController.setGameRecord(currentGameRecord);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) text.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            text.setText("Failed to load the page.");
            e.printStackTrace();
        }
    }
}

