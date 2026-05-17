package com.example.ticketsupp.view;

import com.example.ticketsupp.model.Ticket;
import com.example.ticketsupp.model.TicketPriority;
import com.example.ticketsupp.model.TicketStatus;
import com.example.ticketsupp.model.TicketStore;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;

/**
 * View for displaying and editing ticket details.
 */
public class TicketDetailView {
    private VBox root;
    private Ticket currentTicket;
    private Label codeLabel;
    private TextField titleField;
    private TextArea descriptionArea;
    private TextField requesterNameField;
    private TextField requesterEmailField;
    private ComboBox<String> assigneeCombo;
    private ComboBox<TicketPriority> priorityCombo;
    private ComboBox<TicketStatus> statusCombo;
    private Label createdAtLabel;
    private Label updatedAtLabel;
    private Label closedAtLabel;
    private Button saveBtn;
    private Button closeBtn;
    private Button reopenBtn;
    private Button deleteBtn;
    private java.util.function.Consumer<Ticket> onTicketUpdated;
    private Runnable onTicketDeleted;

    public TicketDetailView() {
        this.root = new VBox(10);
        this.root.setPadding(new Insets(15));
        this.root.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #f5f5f5;");
        buildUI();
    }

    private void buildUI() {
        // Title
        Label titleLabel = new Label("Ticket Details");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // ScrollPane for content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Code (read-only)
        HBox codeBox = createLabeledField("Ticket Code:");
        codeLabel = new Label("Not selected");
        codeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        codeBox.getChildren().add(codeLabel);
        content.getChildren().add(codeBox);

        // Title
        HBox titleBox = createLabeledField("Title:");
        titleField = new TextField();
        titleField.setStyle("-fx-font-size: 11px;");
        titleBox.getChildren().add(titleField);
        HBox.setHgrow(titleField, Priority.ALWAYS);
        content.getChildren().add(titleBox);

        // Description
        content.getChildren().add(new Label("Description:"));
        descriptionArea = new TextArea();
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setStyle("-fx-font-size: 11px;");
        content.getChildren().add(descriptionArea);

        // Requester Name
        HBox nameBox = createLabeledField("Requester Name:");
        requesterNameField = new TextField();
        requesterNameField.setStyle("-fx-font-size: 11px;");
        nameBox.getChildren().add(requesterNameField);
        HBox.setHgrow(requesterNameField, Priority.ALWAYS);
        content.getChildren().add(nameBox);

        // Requester Email
        HBox emailBox = createLabeledField("Requester Email:");
        requesterEmailField = new TextField();
        requesterEmailField.setStyle("-fx-font-size: 11px;");
        emailBox.getChildren().add(requesterEmailField);
        HBox.setHgrow(requesterEmailField, Priority.ALWAYS);
        content.getChildren().add(emailBox);

        // Assignee
        HBox assigneeBox = createLabeledField("Assignee:");
        assigneeCombo = new ComboBox<>();
        assigneeCombo.getItems().addAll("Unassigned", "Alice Johnson", "Bob Smith", "Carol White", "David Lee");
        assigneeCombo.setStyle("-fx-font-size: 11px;");
        assigneeBox.getChildren().add(assigneeCombo);
        HBox.setHgrow(assigneeCombo, Priority.ALWAYS);
        content.getChildren().add(assigneeBox);

        // Priority
        HBox priorityBox = createLabeledField("Priority:");
        priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll(TicketPriority.values());
        priorityCombo.setStyle("-fx-font-size: 11px;");
        priorityBox.getChildren().add(priorityCombo);
        HBox.setHgrow(priorityCombo, Priority.ALWAYS);
        content.getChildren().add(priorityBox);

        // Status
        HBox statusBox = createLabeledField("Status:");
        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(TicketStatus.values());
        statusCombo.setStyle("-fx-font-size: 11px;");
        statusBox.getChildren().add(statusCombo);
        HBox.setHgrow(statusCombo, Priority.ALWAYS);
        content.getChildren().add(statusBox);

        // Timestamps
        HBox createdBox = createLabeledField("Created:");
        createdAtLabel = new Label("N/A");
        createdBox.getChildren().add(createdAtLabel);
        content.getChildren().add(createdBox);

        HBox updatedBox = createLabeledField("Updated:");
        updatedAtLabel = new Label("N/A");
        updatedBox.getChildren().add(updatedAtLabel);
        content.getChildren().add(updatedBox);

        HBox closedBox = createLabeledField("Closed:");
        closedAtLabel = new Label("N/A");
        closedBox.getChildren().add(closedAtLabel);
        content.getChildren().add(closedBox);

        scrollPane.setContent(content);
        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-padding: 8px 15px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> saveTicket());

        closeBtn = new Button("🔒 Close Ticket");
        closeBtn.setStyle("-fx-padding: 8px 15px; -fx-background-color: #ff9800; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> closeTicket());

        reopenBtn = new Button("🔓 Reopen Ticket");
        reopenBtn.setStyle("-fx-padding: 8px 15px; -fx-background-color: #2196f3; -fx-text-fill: white;");
        reopenBtn.setOnAction(e -> reopenTicket());

        deleteBtn = new Button("🗑️ Delete Ticket");
        deleteBtn.setStyle("-fx-padding: 8px 15px; -fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteTicket());

        buttonBox.getChildren().addAll(saveBtn, closeBtn, reopenBtn, deleteBtn);
        root.getChildren().add(buttonBox);
    }

    private HBox createLabeledField(String labelText) {
        HBox box = new HBox(10);
        Label label = new Label(labelText);
        label.setPrefWidth(120);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        box.getChildren().add(label);
        return box;
    }

    public void showTicket(Ticket ticket) {
        this.currentTicket = ticket;
        codeLabel.setText(ticket.getTicketCode());
        titleField.setText(ticket.getTitle());
        descriptionArea.setText(ticket.getDescription());
        requesterNameField.setText(ticket.getRequesterName());
        requesterEmailField.setText(ticket.getRequesterEmail());
        assigneeCombo.setValue(ticket.getAssignee());
        priorityCombo.setValue(ticket.getPriority());
        statusCombo.setValue(ticket.getStatus());
        createdAtLabel.setText(ticket.getFormattedCreatedAt());
        updatedAtLabel.setText(ticket.getFormattedUpdatedAt());
        closedAtLabel.setText(ticket.getFormattedClosedAt());

        // Update button visibility
        closeBtn.setVisible(ticket.getStatus() != TicketStatus.CLOSED);
        closeBtn.setManaged(ticket.getStatus() != TicketStatus.CLOSED);
        reopenBtn.setVisible(ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED);
        reopenBtn.setManaged(ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED);
    }

    public void clearView() {
        currentTicket = null;
        codeLabel.setText("Not selected");
        titleField.clear();
        descriptionArea.clear();
        requesterNameField.clear();
        requesterEmailField.clear();
        assigneeCombo.setValue(null);
        priorityCombo.setValue(null);
        statusCombo.setValue(null);
        createdAtLabel.setText("N/A");
        updatedAtLabel.setText("N/A");
        closedAtLabel.setText("N/A");
    }

    private void saveTicket() {
        if (currentTicket == null) return;

        // Validation
        if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty() ||
                requesterNameField.getText().isEmpty() || requesterEmailField.getText().isEmpty()) {
            showAlert("Validation Error", "All fields are required.");
            return;
        }

        // Business rule: Cannot close/resolve without assignee
        TicketStatus newStatus = statusCombo.getValue();
        if ((newStatus == TicketStatus.CLOSED || newStatus == TicketStatus.RESOLVED) &&
                assigneeCombo.getValue().equals("Unassigned")) {
            showAlert("Business Rule Violation", "Please assign this ticket before closing or resolving it.");
            return;
        }

        // Update ticket
        currentTicket.setTitle(titleField.getText());
        currentTicket.setDescription(descriptionArea.getText());
        currentTicket.setRequesterName(requesterNameField.getText());
        currentTicket.setRequesterEmail(requesterEmailField.getText());
        currentTicket.setAssignee(assigneeCombo.getValue());
        currentTicket.setPriority(priorityCombo.getValue());
        currentTicket.setStatus(newStatus);
        currentTicket.setUpdatedAt(LocalDateTime.now());

        // Handle closed_at timestamp
        if (newStatus == TicketStatus.CLOSED || newStatus == TicketStatus.RESOLVED) {
            if (currentTicket.getClosedAt() == null) {
                currentTicket.setClosedAt(LocalDateTime.now());
            }
        } else {
            currentTicket.setClosedAt(null);
        }

        TicketStore.getInstance().updateTicket(currentTicket);
        showTicket(currentTicket);
        if (onTicketUpdated != null) onTicketUpdated.accept(currentTicket);
        showAlert("Success", "Ticket updated successfully.");
    }

    private void closeTicket() {
        if (currentTicket == null) return;

        if (currentTicket.getAssignee().equals("Unassigned")) {
            showAlert("Business Rule Violation", "Please assign this ticket before closing it.");
            return;
        }

        currentTicket.setStatus(TicketStatus.CLOSED);
        currentTicket.setClosedAt(LocalDateTime.now());
        currentTicket.setUpdatedAt(LocalDateTime.now());
        TicketStore.getInstance().updateTicket(currentTicket);
        showTicket(currentTicket);
        if (onTicketUpdated != null) onTicketUpdated.accept(currentTicket);
        showAlert("Success", "Ticket closed successfully.");
    }

    private void reopenTicket() {
        if (currentTicket == null) return;
        currentTicket.setStatus(TicketStatus.OPEN);
        currentTicket.setClosedAt(null);
        currentTicket.setUpdatedAt(LocalDateTime.now());
        TicketStore.getInstance().updateTicket(currentTicket);
        showTicket(currentTicket);
        if (onTicketUpdated != null) onTicketUpdated.accept(currentTicket);
        showAlert("Success", "Ticket reopened successfully.");
    }

    private void deleteTicket() {
        if (currentTicket == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Ticket");
        alert.setContentText("Are you sure you want to delete this ticket?");
        if (alert.showAndWait().orElse(Alert.ButtonType.CANCEL) != Alert.ButtonType.OK) {
            return;
        }

        int ticketId = currentTicket.getId();
        TicketStore.getInstance().deleteTicket(ticketId);
        clearView();
        if (onTicketDeleted != null) onTicketDeleted.run();
        showAlert("Success", "Ticket deleted successfully.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setOnTicketUpdated(java.util.function.Consumer<Ticket> handler) {
        this.onTicketUpdated = handler;
    }

    public void setOnTicketDeleted(Runnable handler) {
        this.onTicketDeleted = handler;
    }

    public VBox getRoot() {
        return root;
    }
}