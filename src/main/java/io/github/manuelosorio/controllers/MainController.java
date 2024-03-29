package io.github.manuelosorio.controllers;

import io.github.manuelosorio.TextAnalyzerCore;
import io.github.manuelosorio.logger.LoggerAbstraction;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This is the controller for the MainView.fxml file.
 * It will handle the button action and load the table.
 * @see TextAnalyzerCore
 * @see DataModel
 */

public class MainController implements Initializable {

    private final LoggerAbstraction logger;

    @FXML
    public TextField amountField;
    @FXML
    public TextField urlField;
    @FXML
    public Label statusLabel;
    @FXML
    public Button button;
    @FXML
    public TextField amountField;
    @FXML
    public TextField urlField;
    @FXML
    public Label statusLabel;
    @FXML
    public Button button;
    @FXML
    private TableView<DataModel> tableView;
    @FXML
    private TableColumn<DataModel, String> wordColumn;
    @FXML
    private TableColumn<DataModel, Integer> frequencyColumn;
    private ObservableList<DataModel> data;

    public MainController() {
        this.logger = new LoggerAbstraction(MainController.class.getName());
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete`
     * @see URL
     */
    public void handleButtonAction() {
        try {
            if (urlField.getText().isEmpty()) {
                this.statusLabel.setText("URL field is empty.");
                this.statusLabel.setStyle("-fx-text-fill: red");
                return;
            }
            if (!urlField.getText().startsWith("http://") && !urlField.getText().startsWith("https://")) {
                this.statusLabel.setText("URL is not valid.");
                this.statusLabel.setStyle("-fx-text-fill: red");
                return;
            }
            if (amountField.getText().isEmpty()) {
                this.statusLabel.setText("Amount field is empty.");
                this.statusLabel.setStyle("-fx-text-fill: red");
                return;
            }
            if (!amountField.getText().matches("[0-9]+")) {
                this.statusLabel.setText("Amount is not valid.");
                this.statusLabel.setStyle("-fx-text-fill: red");
                return;
            }
            TextAnalyzerCore textAnalyzerCore = new TextAnalyzerCore(urlField.getText());
            List<Map.Entry<String, Integer>> sortedList = textAnalyzerCore.getSortedList();
            this.loadTable(sortedList, Integer.parseInt(amountField.getText()));
            String url = urlField.getText().length() > 40
                    ? urlField.getText().substring(0, 40) + "..." : urlField.getText();
            this.statusLabel.setText("Retrieved data from: " + url );
            this.statusLabel.setStyle("-fx-text-fill: green");

        } catch (Exception e) {
            this.logger.severe(e.getMessage());
        }
    }

    /**
     * This method updates the button text when the amount field is changed.
     * It will also check if the amount field is empty or not a number.
     * If the amount field is empty or not a number, it will not update the button text.
     * If the amount field is less than 1, it will set the amount field to 1.
     *
     * @see TextField
     * @see Button
     */
    public void onAmountChange() {
        if (this.amountField.getText().isEmpty()) {
            return;
        }
        if (!this.amountField.getText().matches("[0-9]+")) {
            return;
        }
        int amount = Integer.parseInt(this.amountField.getText());
        if ( amount < 1) {
            this.amountField.setText("1");
        }
        this.button.setText("Get Top " + this.amountField.getText() +
                (amount > 1 ? " Words" : " Word"));


    }

    /**
     * This method will load the table with the data.
     * It will clear the table and load the data into the table.
     * It will also check if the limit is greater than the data size.
     * If the limit is greater than the data size, it will only load the data size.
     *
     * @param data The data to load into the table.
     * @param limit The number of rows to load.
     * @see DataModel
     */
    private void loadTable(List<Map.Entry<String, Integer>> data, int limit) {
           int index = 0;
           this.tableView.getItems().clear();
           for (Map.Entry<String, Integer> entry : data) {
               if (index >= limit) {
                   break;
               }
               this.tableView.getItems().add(new DataModel(entry.getKey(),
                       entry.getValue()));
               index++;
           }


    }
    /**
     * This method is called by the FXMLLoader when initialization is complete
     * It will initialize the table and set the button text.
     * It will also add a listener to the amount field.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     * @see URL
     * @see ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.data = FXCollections.observableArrayList();
        this.tableView.setItems(data);
        this.wordColumn.setCellValueFactory(new PropertyValueFactory<>("Word"));
        this.frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("Frequency"));
        this.button.setText("Get Top 20 Words");
        this.amountField.textProperty().addListener((observable, oldValue, newValue) -> onAmountChange());
    }

    /**
     * This class is used to create the data model for the table.
     * It will hold the word and the frequency.
     * It will also have getters for the word and frequency.
     *
     * @see MainController
     */
    public static class DataModel {
        private final SimpleStringProperty word;
        private final SimpleIntegerProperty frequency;
        public DataModel(String word, int frequency) {
            this.word = new SimpleStringProperty(word);
            this.frequency = new SimpleIntegerProperty(frequency);
        }
        public String getWord() {
            return word.get();
        }
        public Integer getFrequency() {
            return frequency.get();
        }
    }
}