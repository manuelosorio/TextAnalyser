package io.github.manuelosorio.controllers;

import io.github.manuelosorio.TextAnalyzerCore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    @FXML
    private TableView<DataModel> tableView;
    @FXML
    private TableColumn<DataModel, String> wordColumn;
    @FXML
    private TableColumn<DataModel, Integer> frequencyColumn;
    private ObservableList<DataModel> data;

    public MainController() {
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete
     * @see URL
     */
    public void handleButtonAction() {
        try {
            TextAnalyzerCore textAnalyzerCore = new TextAnalyzerCore("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
            List<Map.Entry<String, Integer>> sortedList = textAnalyzerCore.getSortedList();
            this.loadTable(sortedList, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method will load the table with the data.
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
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.data = FXCollections.observableArrayList();
        this.tableView.setItems(data);
        this.wordColumn.setCellValueFactory(new PropertyValueFactory<>("Word"));
        this.frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("Frequency"));
    }

    /**
     * This class is used to create the data model for the table.
     * It will hold the word and the frequency.
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