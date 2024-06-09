package hangman;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HangmanController {
    @FXML
    private Label hangmanText;

    @FXML
    private TextField guessField;

    @FXML
    private Label wordLabel;

    @FXML
    private Label endOfGameLabel;
    @FXML
    private Label timeLabel;

    private static String word;
    private StringBuilder secretWord;

    private int livesPos = 0;
    private AnimationTimer timer;
    private long startTime;
    private DatabaseManager dbManager = new DatabaseManager();
    private GameRecord currentGameRecord;
    private Map<String, Button> letterButtons = new HashMap<>();
    private static final String API_KEY = "lx3V7SSX0UGbFy8DibPSfQ==fbNjG3un4zdTPqV3";

    private final ArrayList<String> hangManLives = new ArrayList<>(Arrays.asList(
            """
            +---+
            |   |
                |
                |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
                |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
            |   |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|   |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
                |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           /    |
                |
          =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           / \\  |
                |
          ========="""
    ));

//    @FXML
//    void initialize() {
//
//    }

    public void setGameRecord(GameRecord gameRecord) {
        this.currentGameRecord = gameRecord;
        resetGame();
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                updateTimerDisplay(elapsedMillis);
            }
        };
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateTimerDisplay(long elapsedMillis) {
        long seconds = elapsedMillis / 1000;
        timeLabel.setText("Time: " + seconds + "s");
    }

    public static String getRandomAnimalName() {
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/animals");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Api-Key", API_KEY);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return parseAnimalName(response.toString());
            } else {
                System.out.println("Error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseAnimalName(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"name\":\"") + 8;
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        if (startIndex > 7 && endIndex > startIndex) {
            String animalName = jsonResponse.substring(startIndex, endIndex);
            return animalName.replaceAll("\\s+", "").toUpperCase();
        } else {
            return null;
        }
    }

    private void resetGame() {
        stopTimer();
        String animalName = getRandomAnimalName();
        if (animalName != null) {
            word = animalName.toUpperCase();
        } else {
            word = "HORSE";
        }
        secretWord = new StringBuilder("-".repeat(word.length()));
        livesPos = 0;
        updateUI();
        guessField.setDisable(false);
        endOfGameLabel.setText("");
        startTimer();
    }

    @FXML
    protected void handleGuess(ActionEvent event) {
        String guess = guessField.getText().toUpperCase();
        guessField.clear();

        if (guess.length() == 1) {
            playTurn(guess.charAt(0));
        } else if (guess.equals(word)) {
            endOfGameLabel.setText("You won!!");
            guessField.setDisable(true);
            stopTimer();
            updateTimerDisplay(System.currentTimeMillis() - startTime);
            saveGame(true); // Save game as won
        } else {
            livesPos++;
            if (livesPos < hangManLives.size()) {
                hangmanText.setText(hangManLives.get(livesPos));
            }
            if (livesPos == hangManLives.size() - 1) {
                endOfGameLabel.setText("You LOST!!");
                guessField.setDisable(true);
                stopTimer();
                updateTimerDisplay(System.currentTimeMillis() - startTime);
                saveGame(false);
            }
        }
        updateUI();
    }

    private void playTurn(char guess) {
        boolean correctGuess = false;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                secretWord.setCharAt(i, guess);
                correctGuess = true;
            }
        }

        if (!correctGuess) {
            livesPos++;
            if (livesPos < hangManLives.size()) {
                hangmanText.setText(hangManLives.get(livesPos));
            }
            if (livesPos == hangManLives.size() - 1) {
                endOfGameLabel.setText("You LOST!!");
                guessField.setDisable(true);
                stopTimer();
                updateTimerDisplay(System.currentTimeMillis() - startTime);
                saveGame(false); // Save game as lost
            }
        } else if (secretWord.toString().equals(word)) {
            endOfGameLabel.setText("You won!!");
            guessField.setDisable(true);
            stopTimer();
            updateTimerDisplay(System.currentTimeMillis() - startTime);
            saveGame(true); // Save game as won
        }

        wordLabel.setText(secretWord.toString());
    }

    private void saveGame(boolean win) {
        int wrongGuesses = livesPos;
        int time = (int) ((System.currentTimeMillis() - startTime) / 1000);
        try {
            dbManager.saveGame(currentGameRecord.getUsername(), word, wrongGuesses, time, win);

            // Update GameRecord
            currentGameRecord.setWord(word);
            currentGameRecord.setWrongGuesses(wrongGuesses);
            currentGameRecord.setTime(time);
            currentGameRecord.setWin(win);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        hangmanText.setText(hangManLives.get(livesPos));
        wordLabel.setText(secretWord.toString());
    }

    @FXML
    protected void handleReset(ActionEvent event) {
        resetGame();
    }

    public void handleKeyPress(ActionEvent event) {
        String letter = ((Button) event.getSource()).getText();
        guessField.setText(letter);
        handleGuess(event);
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #001f3f");
        button.setTextFill(Color.WHITE);
    }
    private void disableLetterButton(String letter) {
        Button button = letterButtons.get(letter);
        if (button != null) {
            button.setDisable(true);
        }
    }
    private void initializeLetterButtons() {
        for (char c = 'A'; c <= 'Z'; c++) {
            String letter = String.valueOf(c);
            Button button = (Button) wordLabel.getScene().lookup("#" + letter);
            letterButtons.put(letter, button);
        }
    }
    private void resetLetterButtons() {
        for (Button button : letterButtons.values()) {
            button.setDisable(false);
            button.setStyle("-fx-background-color: #7FDBFF");
            button.setTextFill(Color.BLACK);
        }
    }

    @FXML
    protected void handleBackToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) guessField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
