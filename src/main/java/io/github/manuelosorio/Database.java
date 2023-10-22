package io.github.manuelosorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import io.github.manuelosorio.logger.LoggerAbstraction;

/**
 * The Database class is responsible for establishing a connection to a MySQL database,
 * initializing the database schema if necessary, and executing SQL queries.
 * It provides methods for executing single queries and a list of queries.
 */
public class Database {
    private Connection connection;
    static String sqlUrl;
    static String databaseName;
    static String user;
    static String password;
    static boolean needsInitialization;

    LoggerAbstraction logger;

    /**
     * Constructs a Database instance and establishes a connection to the MySQL server.
     * Initializes the database schema if the needsInitialization parameter is set to true.
     *
     * @param sqlUrl             the URL of the MySQL server
     * @param databaseName       the name of the database
     * @param user               the username for accessing the MySQL server
     * @param password           the password for accessing the MySQL server
     * @param needsInitialization if true, the database schema will be initialized
     */
    public Database(String sqlUrl, String databaseName, String user, String password, boolean needsInitialization) {
        this.logger = new LoggerAbstraction(Database.class.getName());
        Database.sqlUrl = sqlUrl;
        Database.databaseName = databaseName;
        Database.user = user;
        Database.password = password;
        Database.needsInitialization = needsInitialization;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                logger.severe("Unable to load MySQL JDBC driver" + e.getMessage());
            }
            if (needsInitialization) {
                this.connection = DriverManager.getConnection(sqlUrl, user, password);
                this.setupDatabase();
                this.logger.info("Database initialized");
            } else {
                this.connection = DriverManager.getConnection(sqlUrl + "/" + databaseName, user, password);
                this.logger.info("Database connected");
            }
        } catch (SQLException e) {
            this.logger.severe("Unable to connect to MySQL server" + e.getMessage());
        }
    }

    public boolean isDatabaseCreated() {
        try {
            Connection connection = DriverManager.getConnection(sqlUrl, user, password);
            ResultSet resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(Database.databaseName)) {
                    return true;
                }
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            this.logger.severe("Unable to connect to MySQL server. " + e.getMessage());
        }
        return false;
    }

    /**
     * Sets up the database schema, creating the necessary tables if they don't exist.
     */
    private void setupDatabase() {
        List<String> queries = new ArrayList<>();
        queries.add("CREATE DATABASE IF NOT EXISTS " + databaseName);
        queries.add("USE " + databaseName);
        queries.add("""
                CREATE TABLE IF NOT EXISTS poem(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    url VARCHAR(255) NOT NULL,
                    UNIQUE(url)
                );
                """);
        queries.add("""
                CREATE TABLE IF NOT EXISTS word(
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    word VARCHAR(255) NOT NULL,
                    poem_id INT,
                    frequency INT DEFAULT 0,
                    UNIQUE(word, poem_id),
                    FOREIGN KEY(poem_id) REFERENCES poem(id)
                );
                """);
        try {
            this.executeQuery(queries);
            this.logger.info("Database schema initialized");
        } catch (SQLException e) {
            this.logger.severe("Unable to setup database. " + e.getMessage());
        }
    }
    /**
     * Returns the active database connection.
     *
     * @return the Connection object
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Executes a single SQL query and returns the result if applicable.
     * Handles SQLExceptions that may occur during query execution.
     *
     * @param query the SQL query to be executed
     * @return the result of the query execution, if applicable; otherwise, returns null
     * @throws SQLException if an error occurs during query execution
     */
    public <E> E executeQuery(String query) throws SQLException {
        try {
            Statement statement = this.getStatement();
            boolean hasResultSet = statement.execute(query);
            if (hasResultSet) {
                this.logger.info("Query executed successfully. " + query);
                return (E) statement.getResultSet();
            }
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 1062 -> {
                    this.logger.warning("Duplicate entry. " + e.getMessage());
                    return (E) Integer.valueOf(e.getErrorCode());
                }
                case 1050 -> {
                    this.logger.warning("Table already exists. " + e.getMessage());
                    return (E) Integer.valueOf(e.getErrorCode());
                }
                default -> {
                    this.logger.severe("Unable to execute query. " + e.getMessage());
                }
            }
        }
        this.logger.info("Query executed successfully. " + query);
        return null;
    }

    /**
     * Executes a list of SQL queries.
     * Handles SQLExceptions that may occur during query execution.
     *
     * @param queries a list of SQL queries to be executed
     * @throws SQLException if an error occurs during query execution
     */
    public void executeQuery(List<String> queries) throws SQLException {
        for (String query : queries) {
            try {
                this.executeQuery(query);
            } catch (SQLException e) {
                switch (e.getErrorCode()) {
                    case 1062 -> {
                        this.logger.warning("Duplicate entry. " + e.getMessage());
                    }
                    case 1050 -> {
                        this.logger.warning("Table already exists. " + e.getMessage());
                    }
                    default -> {
                        this.logger.severe("Unable to execute query. " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Creates and returns a new Statement object for executing SQL queries.
     *
     * @return the new Statement object
     * @throws SQLException if a database access error occurs
     */
    public Statement getStatement() throws SQLException {
        this.logger.info("Creating new statement");
        return this.getConnection().createStatement();
    }

}
