package io.github.manuelosorio.controllers;

import io.github.manuelosorio.TextAnalyzerCore;
import io.github.manuelosorio.TextAnalyzerGui;
import io.github.manuelosorio.utils.JavaConnector;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;


public class MainController implements Initializable {
    @FXML
    private TableView<DataModel> tableView;
    @FXML
    private TableColumn<DataModel, String> wordColumn;
    @FXML
    private TableColumn<DataModel, Integer> frequencyColumn;
    private ObservableList<DataModel> data;
    @FXML
    private WebView webView;

    JSObject win;
    List<Map.Entry<String, Integer>> sortedList;

    public MainController() {
    }

    public void handleButtonAction() {
        try {
            TextAnalyzerCore textAnalyzerCore = new TextAnalyzerCore("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
            this.sortedList = textAnalyzerCore.getSortedList();
            this.loadTable(this.sortedList, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTable(List<Map.Entry<String, Integer>> data, int limit) throws InterruptedException {
           int index = 0;
           this.tableView.getItems().clear();
           for (Map.Entry<String, Integer> entry : data) {
               if (index >= limit) {
                   break;
               }
               this.data.add(new DataModel(entry.getKey(), entry.getValue()));
               index++;
           }
           this.win.setMember("getSortedList", this.getSortedList());
           this.win.setMember("myData", this.data);
           this.tableView.setItems(this.data);

    }
    private List<Map.Entry<String, Integer>> getSortedList() {
        return this.sortedList;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.data = FXCollections.observableArrayList();
        this.tableView.setItems(data);
        this.wordColumn.setCellValueFactory(new PropertyValueFactory<>("Word"));
        this.frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("Frequency"));
        WebEngine webEngine = this.webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                this.win = (JSObject) webEngine.executeScript("window");
                this.win.setMember("javaConnector", new JavaConnector());
                this.win.setMember("mainController", this);
                this.win.call("init");
            }
        });
        try {
            webEngine.load(TextAnalyzerGui.class.getResource("html/index.html").toExternalForm());
        } catch (Exception e) {
            webEngine.loadContent("Error loading html");
            e.printStackTrace();
        }
    }
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