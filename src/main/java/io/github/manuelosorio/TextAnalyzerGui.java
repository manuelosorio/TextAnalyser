package io.github.manuelosorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the main class for the Text Analyzer application.
 * It will launch the GUI.
 */
public class TextAnalyzerGui extends Application {

    /**
     * This method is called by the JavaFX runtime when the application is ready to start.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent parent = fxmlLoader.load();
            parent.getStylesheets().setAll("style.css");
            primaryStage.setTitle("Text Analyzer");
            primaryStage.setScene(new Scene(parent));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
