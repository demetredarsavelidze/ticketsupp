package com.example.ticketsupp.view;

import com.example.ticketsupp.model.Ticket;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Custom ListCell for rendering ticket items.
 */
public class TicketCell extends ListCell<Ticket> {
    @Override
    protected void updateItem(Ticket ticket, boolean empty) {
        super.updateItem(ticket, empty);

        if (empty || ticket == null) {
            setGraphic(null);
            return;
        }

        VBox cellContent = new VBox(5);
        cellContent.setPadding(new Insets(10));
        cellContent.setStyle("-fx-border-color: #eee; -fx-border-radius: 5; -fx-background-color: #fff;");

        // Header: Code and Priority
        HBox header = new HBox(10);
        Label codeLabel = new Label(ticket.getTicketCode());
        codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        Label priorityBadge = new Label(ticket.getPriority().getDisplayName());
        priorityBadge.setPadding(new Insets(3, 8, 3, 8));
        String priorityColor = getPriorityColor(ticket.getPriority());
        priorityBadge.setStyle("-fx-text-fill: white; -fx-background-color: " + priorityColor + "; -fx-border-radius: 3;");

        Label statusBadge = new Label(ticket.getStatus().getDisplayName());
        statusBadge.setPadding(new Insets(3, 8, 3, 8));
        String statusColor = getStatusColor(ticket.getStatus());
        statusBadge.setStyle("-fx-text-fill: white; -fx-background-color: " + statusColor + "; -fx-border-radius: 3;");

        header.getChildren().addAll(codeLabel, priorityBadge, statusBadge);

        // Title
        Label titleLabel = new Label(ticket.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        titleLabel.setWrapText(true);

        // Details: Name and Email
        HBox details = new HBox(10);
        Label nameLabel = new Label("👤 " + ticket.getRequesterName());
        nameLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        Label emailLabel = new Label("📧 " + ticket.getRequesterEmail());
        emailLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        details.getChildren().addAll(nameLabel, emailLabel);

        // Footer: Assignee and Date
        HBox footer = new HBox(10);
        Label assigneeLabel = new Label("👥 " + ticket.getAssignee());
        assigneeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        Label dateLabel = new Label(ticket.getFormattedCreatedAt());
        dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        footer.getChildren().addAll(assigneeLabel, dateLabel);

        cellContent.getChildren().addAll(header, titleLabel, details, footer);
        setGraphic(cellContent);
    }

    private String getPriorityColor(com.example.ticketsupp.model.TicketPriority priority) {
        return switch (priority) {
            case CRITICAL -> "#d32f2f";
            case HIGH -> "#f57c00";
            case MEDIUM -> "#fbc02d";
            case LOW -> "#388e3c";
        };
    }

    private String getStatusColor(com.example.ticketsupp.model.TicketStatus status) {
        return switch (status) {
            case OPEN -> "#1976d2";
            case IN_PROGRESS -> "#7b1fa2";
            case RESOLVED -> "#388e3c";
            case CLOSED -> "#616161";
        };
    }
}