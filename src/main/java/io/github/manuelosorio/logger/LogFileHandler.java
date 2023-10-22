package io.github.manuelosorio.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogFileHandler {
    public static void setup() {
        Logger logger = Logger.getLogger(LoggerAbstraction.class.getName());

        try {
            FileHandler fileHandler = new FileHandler("application.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }
}
