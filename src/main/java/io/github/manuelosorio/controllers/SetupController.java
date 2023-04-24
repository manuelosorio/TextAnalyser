package io.github.manuelosorio.controllers;

import io.github.manuelosorio.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class SetupController {
    @FXML
    public TextField urlField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField databaseField;
    public Button connectButton;
    public CheckBox initialiseCheckBox;
    public Label statusLabel;

    public SetupController() {
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

        } else {
            statusLabel.setText("Database creation failed.");
            statusLabel.setStyle("-fx-text-fill: red");
        }



    }
}
