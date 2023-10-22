package io.github.manuelosorio.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerAbstraction {
    private static final Logger logger = Logger.getLogger(LoggerAbstraction.class.getName());
    private final String classSource;
    private String methodCaller;

    static {
        LogFileHandler.setup();
    }
    public LoggerAbstraction(String name) {
        this.classSource = name;
    }
    public void info(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.INFO, this.classSource, this.methodCaller, message);
    }

    public void warning(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.WARNING, this.classSource, this.methodCaller, message);
    }

    public void severe(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.SEVERE, this.classSource, this.methodCaller, message);
    }

    public void fine(String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(Level.FINE, this.classSource, this.methodCaller, message);
    }

    public void log(Level level, String message) {
        this.methodCaller = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.logp(level, this.classSource, this.methodCaller, message);
    }
}
