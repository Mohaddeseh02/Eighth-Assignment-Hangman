package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SignupPageController {

    @FXML
    private TextField emailField;

    @FXML
    private Text messageText;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private DatabaseManager dbManager;

    public SignupPageController() {
        dbManager = new DatabaseManager();
    }

    @FXML
    void handleLogin(MouseEvent event) {
        loadPage("LoginPage.fxml", null);
    }

    @FXML
    void handleSignup(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = emailField.getText();

        try {
            dbManager.createUser(name, username, password);
            messageText.setText("Signup successful! Redirecting to main menu...");

            // Create a new GameRecord instance
            GameRecord gameRecord = new GameRecord(0, username, "", 0, 0, false);

            loadPage("MainMenuPage.fxml", gameRecord);
        } catch (Exception e) {
            messageText.setText("An error occurred. Please try again.");
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlFile, GameRecord gameRecord) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            if (gameRecord != null) {
                MainMenuPageController controller = loader.getController();
                controller.setGameRecord(gameRecord);
            }
            Stage stage = (Stage) messageText.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            messageText.setText("Failed to load the page.");
            e.printStackTrace();
        }
    }
}
