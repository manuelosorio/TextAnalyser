package io.github.manuelosorio;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The ViewManager class is a utility class responsible for managing the JavaFX views
 * in the application. It provides a mechanism to switch between different views and apply
 * a stylesheet to them.
 *
 */
public class ViewManager {

    /**
     * A static ViewManager instance used to implement the singleton pattern.
     * This ensures that only one ViewManager instance exists throughout
     * the application's lifecycle.
     */
    private static ViewManager instance = null;
    private final Stage stage;

    /**
     * Constructs a new ViewManager instance and initializes the primary stage.
     *
     * @param primaryStage the primary stage of the JavaFX application
     */
    public ViewManager(Stage primaryStage) {
        instance = this;
        this.stage = primaryStage;
        this.stage.setTitle("Text Analyzer");
    }

    /**
     * Sets the view for the application and applies the provided stylesheet to it.
     * This method is used to switch between different views in the application.
     *
     * @param viewFile   the FXML file for the view to be set
     * @param StyleSheet the CSS file to be applied to the view
     * @throws IOException if there is an error loading the FXML or CSS files
     */
    public void setView(String viewFile, String StyleSheet) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewFile));
        Parent parent = fxmlLoader.load();
        parent.getStylesheets().setAll(StyleSheet);
        this.stage.setScene(new Scene(parent));
        this.stage.show();
    }

    /**
     * Returns the singleton instance of the ViewManager class.
     *
     * @return the ViewManager instance
     */
    public static ViewManager getInstance() {
        return instance;
    }

}
