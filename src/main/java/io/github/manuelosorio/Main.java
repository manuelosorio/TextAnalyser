package io.github.manuelosorio;

import javafx.application.Application;

/**
 * The Main class serves as the entry point for the Text Analyzer application.
 * It launches the JavaFX application by calling the launch method on the
 * TextAnalyzerGui class.
 *
 * @see io.github.manuelosorio.TextAnalyzerGui
 * @see javafx.application.Application
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(TextAnalyzerGui.class, args);

    }
}