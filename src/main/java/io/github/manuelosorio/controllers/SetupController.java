package io.github.manuelosorio.controllers;

import io.github.manuelosorio.Database;
import io.github.manuelosorio.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * SetupController is the controller class responsible for managing the setup
 * view of the Text Analyzer application. It handles the initialization of the
 * database connection and transitions to the main view of the application once
 * the connection is established.
 *
 * @see io.github.manuelosorio.Database
 * @see io.github.manuelosorio.ViewManager
 */
public class SetupController {

    private final ViewManager viewManager;
    @FXML
    public TextField urlField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField databaseField;
    public Button connectButton;
    public CheckBox initialiseCheckBox;
    public Label statusLabel;
    public SetupController() {
        this.viewManager = ViewManager.getInstance();
    }
    /**
     * This method is called during initialization and sets up the event handler
     * for the connectButton.
     */
    public void initialize() {
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleButtonAction);
    }

    /**
     * This method handles the connectButton action. It creates a new database
     * connection using the input provided by the user. If the connection is
     * successful, it transitions to the main view of the application.
     *
     * @param mouseEvent the MouseEvent triggering the action
     */
    public void handleButtonAction(MouseEvent mouseEvent) {
        Database db = new Database("jdbc:mysql://" + urlField.getText(), databaseField.getText(),
                usernameField.getText(), passwordField.getText(), initialiseCheckBox.isSelected());

        if (db.isDatabaseCreated()) {
            statusLabel.setText("Database created successfully.");
            statusLabel.setStyle("-fx-text-fill: green");
            CompletableFuture.delayedExecutor(2, java.util.concurrent.TimeUnit.SECONDS).execute(() -> {
                Platform.runLater(() -> {
                    try {
                        viewManager.setView("MainView.fxml", "style.css");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        } else {
            statusLabel.setText("Database creation failed.");
            statusLabel.setStyle("-fx-text-fill: red");
        }



    }
}
