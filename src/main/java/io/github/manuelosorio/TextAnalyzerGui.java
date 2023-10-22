package io.github.manuelosorio;

import com.mysql.cj.log.Log;
import io.github.manuelosorio.logger.LoggerAbstraction;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * TextAnalyzerGui is the main GUI class for the Text Analyzer application.
 * This class initializes and launches the JavaFX application. It extends the
 * Application class and overrides its start method to set up the initial view
 * of the application using the ViewManager.
 *
 * @see javafx.application.Application
 * @see io.github.manuelosorio.ViewManager
 */
public class TextAnalyzerGui extends Application {

    private LoggerAbstraction logger = new LoggerAbstraction(TextAnalyzerGui.class.getName());

    /**
     * Opens the initial view of the application using the ViewManager.
     * This method is called by the JavaFX application thread.
     *
     * @param primaryStage the primary stage of the JavaFX application
     * @throws RuntimeException if there is an error initializing the application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ViewManager viewManager = new ViewManager(primaryStage);
            viewManager.setView("SetupView.fxml",
                    "style.css");

        } catch (IOException e) {
            this.logger.severe("Failed to initialize application: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
