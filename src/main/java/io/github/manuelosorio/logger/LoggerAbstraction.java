package io.github.manuelosorio.logger;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is an abstraction of the java.util.logging.Logger class.
 * It provides a simple interface to log messages at different levels.
 * It also provides a method to log messages at a level that is not one of
 * the predefined levels.
 */
public class LoggerAbstraction {
    private static final Logger logger = Logger.getLogger(LoggerAbstraction.class.getName());
    private final String classSource;
    private String methodCaller;

    static {
        LogFileHandler.setup();
    }

    /**
     * Returns a LoggerAbstraction object associated with the class passed as parameter.
     * @param name The name of the class to be associated with the LoggerAbstraction object.
     */
    public LoggerAbstraction(String name) {
        this.classSource = name;
    }

    /**
     * Info level logging method that logs the message passed as parameter.
     * @param message The message to be logged.
     */
    public void info(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.INFO, this.classSource, this.methodCaller, message);
    }

    /**
     * Warning level logging method that logs the message passed as parameter.
     * @param message The message to be logged.
     */
    public void warning(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.WARNING, this.classSource, this.methodCaller, message);
    }

    /**
     * Severe level logging method that logs the message passed as parameter.
     * @param message The message to be logged.
     */
    public void severe(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.SEVERE, this.classSource, this.methodCaller, message);
    }

    /**
     * Fine level logging method that logs the message passed as parameter.
     * @param message The message to be logged.
     */
    public void fine(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.FINE, this.classSource, this.methodCaller, message);
    }

    /**
     * Logging method that logs the message passed as parameter at the level passed as parameter.
     * To be used when the level is not one of the predefined levels or when the level is not known.
     * @param level The level at which the message will be logged.
     * @param message The message to be logged.
     */
    public void log(Level level, String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(level, this.classSource, this.methodCaller, message);
    }
}
