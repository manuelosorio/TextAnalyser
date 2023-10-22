package io.github.manuelosorio;

import io.github.manuelosorio.logger.LoggerAbstraction;
import javafx.application.Application;

import java.util.logging.Level;

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
        LoggerAbstraction logger = new LoggerAbstraction(Main.class.getName());
        logger.info("Application launched");
        Application.launch(TextAnalyzerGui.class, args);

    }
}