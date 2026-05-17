package com.example.ticketsupp.view;

import com.example.ticketsupp.model.Ticket;
import com.example.ticketsupp.model.TicketPriority;
import com.example.ticketsupp.model.TicketStatus;
import com.example.ticketsupp.model.TicketStore;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDateTime;

/**
 * Dialog for creating a new ticket with validation.
 */
public class NewTicketDialog {
    private Stage stage;
    private TextField requesterNameField;
    private TextField requesterEmailField;
    private TextField titleField;
    private TextArea descriptionArea;
    private ComboBox<TicketPriority> priorityCombo;
    private ComboBox<String> assigneeCombo;
    private boolean saved = false;

    public NewTicketDialog() {
        stage = new Stage();
        stage.setTitle("Create New Ticket");
        stage.setWidth(500);
        stage.setHeight(600);
        stage.initModality(Modality.APPLICATION_MODAL);
        buildUI();
    }

    private void buildUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Requester Name
        Label nameLabel = new Label("Requester Name: *");
        nameLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(nameLabel, 0, 0);
        requesterNameField = new TextField();
        requesterNameField.setPromptText("Customer name");
        gridPane.add(requesterNameField, 1, 0);
        GridPane.setHgrow(requesterNameField, Priority.ALWAYS);

        // Requester Email
        Label emailLabel = new Label("Requester Email: *");
        emailLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(emailLabel, 0, 1);
        requesterEmailField = new TextField();
        requesterEmailField.setPromptText("customer@example.com");
        gridPane.add(requesterEmailField, 1, 1);
        GridPane.setHgrow(requesterEmailField, Priority.ALWAYS);

        // Title
        Label titleLabel = new Label("Title: *");
        titleLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(titleLabel, 0, 2);
        titleField = new TextField();
        titleField.setPromptText("Brief title of the issue");
        gridPane.add(titleField, 1, 2);
        GridPane.setHgrow(titleField, Priority.ALWAYS);

        // Description
        Label descLabel = new Label("Description: *");
        descLabel.setStyle("-fx-font-weight: bold; -fx-alignment: top;");
        gridPane.add(descLabel, 0, 3);
        descriptionArea = new TextArea();
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setPromptText("Detailed description of the issue...");
        gridPane.add(descriptionArea, 1, 3);
        GridPane.setHgrow(descriptionArea, Priority.ALWAYS);

        // Priority
        Label priorityLabel = new Label("Priority: *");
        priorityLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(priorityLabel, 0, 4);
        priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll(TicketPriority.values());
        priorityCombo.setValue(TicketPriority.MEDIUM);
        gridPane.add(priorityCombo, 1, 4);
        GridPane.setHgrow(priorityCombo, Priority.ALWAYS);

        // Assignee
        Label assigneeLabel = new Label("Assignee:");
        assigneeLabel.setStyle("-fx-font-weight: bold;");
        gridPane.add(assigneeLabel, 0, 5);
        assigneeCombo = new ComboBox<>();
        assigneeCombo.getItems().addAll("Unassigned", "Alice Johnson", "Bob Smith", "Carol White", "David Lee");
        assigneeCombo.setValue("Unassigned");
        gridPane.add(assigneeCombo, 1, 5);
        GridPane.setHgrow(assigneeCombo, Priority.ALWAYS);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button createBtn = new Button("✅ Create Ticket");
        createBtn.setStyle("-fx-padding: 8px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        createBtn.setOnAction(e -> createTicket());

        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-padding: 8px 20px; -fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(createBtn, cancelBtn);
        gridPane.add(buttonBox, 1, 6);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
    }

    private void createTicket() {
        // Validation
        if (requesterNameField.getText().isEmpty()) {
            showError("Validation Error", "Requester name is required.");
            return;
        }
        if (requesterEmailField.getText().isEmpty()) {
            showError("Validation Error", "Requester email is required.");
            return;
        }
        if (!requesterEmailField.getText().contains("@")) {
            showError("Validation Error", "Email must contain @.");
            return;
        }
        if (titleField.getText().isEmpty()) {
            showError("Validation Error", "Title is required.");
            return;
        }
        if (descriptionArea.getText().isEmpty()) {
            showError("Validation Error", "Description is required.");
            return;
        }
        if (priorityCombo.getValue() == null) {
            showError("Validation Error", "Priority must be selected.");
            return;
        }

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setRequesterName(requesterNameField.getText());
        ticket.setRequesterEmail(requesterEmailField.getText());
        ticket.setTitle(titleField.getText());
        ticket.setDescription(descriptionArea.getText());
        ticket.setPriority(priorityCombo.getValue());
        ticket.setAssignee(assigneeCombo.getValue());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        // Save to database
        TicketStore.getInstance().addTicket(ticket);
        saved = true;
        showInfo("Success", "Ticket created successfully.");
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public boolean isSaved() {
        return saved;
    }
}