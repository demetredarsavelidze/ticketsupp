package com.example.ticketsupp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database initialization and connection management.
 * Uses SQLite for persistent storage.
 */
public class Database {
    private static final String DB_URL = "jdbc:sqlite:tickets.db";
    private static Connection connection;

    /**
     * Initialize database connection and create tables if they don't exist.
     */
    public static void init() {
        try {
            // Ensure SQLite driver is loaded
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected successfully.");
            
            // Create tables if they don't exist
            TicketDAO.createTable();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the database connection.
     * @return Connection object for database operations
     */
    public static Connection getConnection() {
        if (connection == null) {
            init();
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}