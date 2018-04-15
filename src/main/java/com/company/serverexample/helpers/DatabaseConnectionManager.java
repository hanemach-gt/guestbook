package com.company.serverexample.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private int connectionCounter = 1;
    private String dbPath;
    private Connection connection;

    public DatabaseConnectionManager(String dbPath) {

        this.dbPath = dbPath;
    }

    public Connection getConnection() {

        if (connectionCounter > 1) {

            throw new UnsupportedOperationException("from db connection manager: only one connection per manager is handleable");
        }
        connectionCounter++;

        try {

            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        String JDBC = "jdbc:sqlite:" + dbPath;

        try {

            connection = DriverManager.getConnection(JDBC);
        } catch (SQLException e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return connection;
    }

    public void closeConnection() {

        try {

            connection.close();
        } catch (SQLException e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
