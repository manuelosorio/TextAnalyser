package io.github.manuelosorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;
    static String sqlUrl;
    static String databaseName;
    static String user;
    static String password;
    static boolean needsInitialization;

    public Database(String sqlUrl, String databaseName, String user, String password, boolean needsInitialization) {
        Database.sqlUrl = sqlUrl;
        Database.databaseName = databaseName;
        Database.user = user;
        Database.password = password;
        Database.needsInitialization = needsInitialization;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (needsInitialization) {
                this.connection = DriverManager.getConnection(sqlUrl, user, password);
                this.setupDatabase();
            } else {
                this.connection = DriverManager.getConnection(sqlUrl + "/" + databaseName, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public <E> E executeQuery(String query) throws SQLException {
        try {
            Statement statement = this.getStatement();
            boolean hasResultSet = statement.execute(query);
            if (hasResultSet) {
                return (E) statement.getResultSet();
            }
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 1062 -> {
                    System.out.println("Duplicate entry");
                    return (E) Integer.valueOf(e.getErrorCode());
                }
                case 1050 -> {
                    System.out.println("Table already exists");
                }
                default -> {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void executeQuery(List<String> queries) throws SQLException {
        for (String query : queries) {
            try {
                this.executeQuery(query);
            } catch (SQLException e) {
                switch (e.getErrorCode()) {
                    case 1062 -> {
                        System.out.println("Duplicate entry");
                    }
                    case 1050 -> {
                        System.out.println("Table already exists");
                    }
                    default -> {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public Statement getStatement() throws SQLException {
        return this.getConnection().createStatement();
    }

}
