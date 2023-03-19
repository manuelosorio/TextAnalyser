package io.github.manuelosorio;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TextAnalyzerGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent parent = fxmlLoader.load();
            parent.getStylesheets().setAll("./style.css");
            primaryStage.setTitle("Text Analyzer 1");
            primaryStage.onCloseRequestProperty().set(event -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.setScene(new Scene(parent));
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
