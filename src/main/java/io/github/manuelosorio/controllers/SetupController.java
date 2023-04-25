package io.github.manuelosorio.controllers;

import io.github.manuelosorio.Database;
import io.github.manuelosorio.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SetupController {
    @FXML
    public TextField urlField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField databaseField;
    public Button connectButton;
    public CheckBox initialiseCheckBox;
    public Label statusLabel;
    private final ViewManager viewManager;
    public SetupController() {
        this.viewManager = ViewManager.getInstance();
    }

    public void initialize() {
        connectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleButtonAction);
    }
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
