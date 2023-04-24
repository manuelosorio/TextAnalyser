package io.github.manuelosorio;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {

    //instance
    private static ViewManager instance = null;
    private final Stage stage;


    public ViewManager(Stage primaryStage) {
        instance = this;
        this.stage = primaryStage;
        this.stage.setTitle("Text Analyzer");
    }

    public void setView(String viewFile, String StyleSheet) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewFile));
        Parent parent = fxmlLoader.load();
        parent.getStylesheets().setAll(StyleSheet);
        this.stage.setScene(new Scene(parent));
        this.stage.show();
    }

    public static ViewManager getInstance() {
        return instance;
    }

}
