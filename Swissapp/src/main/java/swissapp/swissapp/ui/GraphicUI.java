/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swissapp.swissapp.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import swissapp.swissapp.applogic.AppLogic;
import swissapp.swissapp.filehandling.FileHandler;
/**
 *
 * @author lassisav
 */
public class GraphicUI extends Application{
    int x;
    int y;
    int rating;
    int tourneyID;
    int rounds;
    Pane layout;
    HBox topRow;
    VBox allRows;
    VBox displayedPlayerList;
    Label tourneyNameLabel;
    Label newTourneyNameLabel;
    Label newTourneyRatingLabel;
    Label errorLabel;
    Label roundsLabel;
    TextField textInput;
    TextField playerName;
    TextField playerRating;
    TextField roundsInput;
    Button newTourneyButton;
    Button loadTourneyButton;
    Button addPlayer;
    Button finishAdd;
    String tourneyName;
    String fileNameAbsolute;
    ArrayList<String> newTourneyPlayerList;
    public GraphicUI() {
        x = 1600;
        y = 1000;
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        layout = new Pane();
        allRows = new VBox();
        topRow = new HBox();
        
        tourneyNameLabel = new Label("Type tournament name, letters and numbers only, no spaces: ");
        textInput = new TextField();
        roundsLabel = new Label("Number of rounds");
        roundsInput = new TextField();
        newTourneyButton = new Button("New tournament");
        loadTourneyButton = new Button("Load tournament");
        
        newTourneyButton.setOnAction((ActionEvent e) -> {
            boolean tourneyCreated = false;
            rounds = Integer.parseInt(roundsInput.getText());
            tourneyName = textInput.getText();
            try {
                tourneyCreated = newTournamentCreation();
            } catch (IOException ex) {
                Logger.getLogger(GraphicUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        initialTopRow();
        
        stage.setTitle("Swissapp");
        initialLayout();
        stage.setScene(new Scene(layout, x, y));
        stage.show();
    }
    public void initialLayout() {
        layout.getChildren().clear();
        layout.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().add(allRows);
        allRows.getChildren().add(topRow);
    }
    public void initialTopRow() {
        topRow.getChildren().clear();
        topRow.getChildren().add(tourneyNameLabel);
        topRow.getChildren().add(textInput);
        topRow.getChildren().add(roundsLabel);
        topRow.getChildren().add(roundsInput);
        topRow.getChildren().add(newTourneyButton);
        topRow.getChildren().add(loadTourneyButton);
    }
    public boolean newTournamentCreation() throws IOException {
        tourneyName = tourneyName.replaceAll("\\s", "");
        tourneyID = FileHandler.setupDir(tourneyName);
        if (tourneyID == -1) {
            initialTopRow();
            topRow.getChildren().add(new Label("Tournament name is already in use, please select another name."));
            return false;
        }
        newTourneyPlayerList = new ArrayList<>();
        displayedPlayerList = new VBox();
        allRows.getChildren().add(displayedPlayerList);
        newTourneyNameLabel = new Label("Name: ");
        playerName = new TextField();
        newTourneyRatingLabel = new Label("Rating: ");
        playerRating = new TextField();
        addPlayer = new Button("Add player");
        finishAdd = new Button("All players added");
        errorLabel = new Label("");
        addPlayer.setOnAction((ActionEvent e) -> {
            boolean goodInt = true;
            try {
                rating = Integer.parseInt(playerRating.getText());
            } catch (NumberFormatException nfe) {
                errorLabel = new Label("Please enter rating as an integer.");
                goodInt = false;
            }
            if (goodInt) {
                errorLabel = new Label("");
                String nameAndRating = playerName.getText();
                nameAndRating = nameAndRating.replaceAll("\\s", "");
                nameAndRating = nameAndRating.concat(" " + playerRating.getText());
                newTourneyPlayerList.add(nameAndRating);
                displayedPlayerList.getChildren().add(new Label(nameAndRating));
            }
        });
        finishAdd.setOnAction((ActionEvent e) -> {
            try {
                fileNameAbsolute = AppLogic.makeInitialMatchesFile(tourneyID, tourneyName, newTourneyPlayerList, rounds);
                matchView();
            } catch (IOException ex) {
                Logger.getLogger(GraphicUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        topRow.getChildren().clear();
        topRow.getChildren().add(newTourneyNameLabel);
        topRow.getChildren().add(playerName);
        topRow.getChildren().add(newTourneyRatingLabel);
        topRow.getChildren().add(playerRating);
        topRow.getChildren().add(addPlayer);
        topRow.getChildren().add(finishAdd);
        topRow.getChildren().add(errorLabel);
        return false;
    }
    public void matchView() throws FileNotFoundException {
        topRow.getChildren().clear();
        allRows.getChildren().clear();
        String[][] matchList = AppLogic.getMatches(fileNameAbsolute);
        topRow.getChildren().add(new Label(matchList[0][0] + " Round " + matchList[0][1] + "/" + matchList[0][2]));
        for (int i = 1; i < matchList.length; i++) {
            allRows.getChildren().add(new Label("Match " + matchList[i][0] + ": " + matchList[i][1] + " ("
                    + matchList[i][2] + ") vs. " + matchList[i][3] + " (" + matchList[i][4] + ")"));
        }
    }
}