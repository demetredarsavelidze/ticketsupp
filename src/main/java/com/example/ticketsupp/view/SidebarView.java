package com.example.ticketsupp.view;

import com.example.ticketsupp.model.Priority;
import com.example.ticketsupp.model.TicketStatus;
import com.example.ticketsupp.model.TicketStore;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Sidebar with navigation and filters.
 */
public class SidebarView {
    private VBox root;
    private Runnable onDashboardClick;
    private Runnable onAllTicketsClick;
    private java.util.function.Consumer<TicketStatus> onStatusFilterClick;
    private java.util.function.Consumer<Priority> onPriorityFilterClick;
    private java.util.function.Consumer<String> onAssigneeFilterClick;
    private Runnable onClearFiltersClick;

    public SidebarView() {
        this.root = new VBox(10);
        this.root.setPrefWidth(220);
        this.root.setPadding(new Insets(15));
        this.root.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        buildUI();
    }

    private void buildUI() {
        // Title
        Label title = new Label("SupportDesk");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        root.getChildren().add(title);

        // Navigation
        Separator sep1 = new Separator();
        root.getChildren().add(sep1);

        Label navLabel = new Label("Navigation");
        navLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        root.getChildren().add(navLabel);

        Button dashboardBtn = new Button("📊 Dashboard");
        dashboardBtn.setPrefWidth(200);
        dashboardBtn.setStyle("-fx-padding: 8px; -fx-font-size: 12px;");
        dashboardBtn.setOnAction(e -> {
            if (onDashboardClick != null) onDashboardClick.run();
        });
        root.getChildren().add(dashboardBtn);

        Button allTicketsBtn = new Button("📋 All Tickets");
        allTicketsBtn.setPrefWidth(200);
        allTicketsBtn.setStyle("-fx-padding: 8px; -fx-font-size: 12px;");
        allTicketsBtn.setOnAction(e -> {
            if (onAllTicketsClick != null) onAllTicketsClick.run();
        });
        root.getChildren().add(allTicketsBtn);

        // Status filters
        Separator sep2 = new Separator();
        root.getChildren().add(sep2);

        Label statusLabel = new Label("Status");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        root.getChildren().add(statusLabel);

        for (TicketStatus status : TicketStatus.values()) {
            Button statusBtn = new Button(status.getDisplayName());
            statusBtn.setPrefWidth(200);
            statusBtn.setStyle("-fx-padding: 6px; -fx-font-size: 11px;");
            statusBtn.setOnAction(e -> {
                if (onStatusFilterClick != null) onStatusFilterClick.accept(status);
            });
            root.getChildren().add(statusBtn);
        }

        // Priority filters
        Separator sep3 = new Separator();
        root.getChildren().add(sep3);

        Label priorityLabel = new Label("Priority");
        priorityLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        root.getChildren().add(priorityLabel);

        for (Priority priority : Priority.values()) {
            Button priorityBtn = new Button(priority.getDisplayName());
            priorityBtn.setPrefWidth(200);
            priorityBtn.setStyle("-fx-padding: 6px; -fx-font-size: 11px;");
            priorityBtn.setOnAction(e -> {
                if (onPriorityFilterClick != null) onPriorityFilterClick.accept(priority);
            });
            root.getChildren().add(priorityBtn);
        }

        // Support Staff filters
        Separator sep4 = new Separator();
        root.getChildren().add(sep4);

        Label staffLabel = new Label("Support Staff");
        staffLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        root.getChildren().add(staffLabel);

        String[] staffMembers = {"Unassigned", "Alice Johnson", "Bob Smith", "Carol White", "David Lee"};
        for (String staff : staffMembers) {
            Button staffBtn = new Button(staff);
            staffBtn.setPrefWidth(200);
            staffBtn.setStyle("-fx-padding: 6px; -fx-font-size: 11px;");
            staffBtn.setOnAction(e -> {
                if (onAssigneeFilterClick != null) onAssigneeFilterClick.accept(staff);
            });
            root.getChildren().add(staffBtn);
        }

        // Clear filters
        Separator sep5 = new Separator();
        root.getChildren().add(sep5);

        Button clearBtn = new Button("🔄 Clear Filters");
        clearBtn.setPrefWidth(200);
        clearBtn.setStyle("-fx-padding: 8px; -fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #ff6b6b;");
        clearBtn.setOnAction(e -> {
            if (onClearFiltersClick != null) onClearFiltersClick.run();
        });
        root.getChildren().add(clearBtn);

        // Bottom stats
        Separator sep6 = new Separator();
        root.getChildren().add(sep6);

        Label statsLabel = new Label("Stats");
        statsLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #666;");
        root.getChildren().add(statsLabel);

        Label totalLabel = new Label("Total: " + TicketStore.getInstance().getTotalCount());
        totalLabel.setStyle("-fx-font-size: 11px;");
        root.getChildren().add(totalLabel);
    }

    public void setOnDashboardClick(Runnable handler) {
        this.onDashboardClick = handler;
    }

    public void setOnAllTicketsClick(Runnable handler) {
        this.onAllTicketsClick = handler;
    }

    public void setOnStatusFilterClick(java.util.function.Consumer<TicketStatus> handler) {
        this.onStatusFilterClick = handler;
    }

    public void setOnPriorityFilterClick(java.util.function.Consumer<Priority> handler) {
        this.onPriorityFilterClick = handler;
    }

    public void setOnAssigneeFilterClick(java.util.function.Consumer<String> handler) {
        this.onAssigneeFilterClick = handler;
    }

    public void setOnClearFiltersClick(Runnable handler) {
        this.onClearFiltersClick = handler;
    }

    public VBox getRoot() {
        return root;
    }
}