package com.example.ticketsupp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.ticketsupp.db.Database;
import com.example.ticketsupp.model.TicketStore;
import com.example.ticketsupp.view.MainView;

/**
 * Main JavaFX Application entry point.
 * Initializes the database and starts the Customer Support Ticket Management System.
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database and create tables if needed
            Database.init();
            
            // Initialize TicketStore to load tickets from database
            TicketStore.getInstance().refresh();
            
            // Create main view
            MainView mainView = new MainView();
            Scene scene = new Scene(mainView.getRoot(), 1400, 800);
            
            primaryStage.setTitle("SupportDesk - Customer Support Ticket Management");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                // Close database connection
                Database.close();
            });
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}