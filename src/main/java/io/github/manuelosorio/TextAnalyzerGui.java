package io.github.manuelosorio;

import javafx.application.Application;
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
            ViewManager viewManager = new ViewManager(primaryStage);
            viewManager.setView("SetupView.fxml",
                    "style.css");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
