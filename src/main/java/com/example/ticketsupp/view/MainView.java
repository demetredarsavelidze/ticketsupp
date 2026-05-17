package com.example.ticketsupp.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Main view layout combining sidebar, ticket list, and detail panel.
 */
public class MainView {
    private BorderPane root;
    private SidebarView sidebarView;
    private TicketListView ticketListView;
    private TicketDetailView detailView;
    private DashboardView dashboardView;
    private VBox centerContent;

    public MainView() {
        this.root = new BorderPane();
        this.sidebarView = new SidebarView();
        this.ticketListView = new TicketListView();
        this.detailView = new TicketDetailView();
        this.dashboardView = new DashboardView();

        setupLayout();
        setupNavigation();
    }

    private void setupLayout() {
        // Left sidebar
        root.setLeft(sidebarView.getRoot());

        // Center and right content
        HBox centerPanel = new HBox(10);
        centerPanel.setPadding(new Insets(10));
        centerPanel.setStyle("-fx-fill-height: true;");

        // Ticket list in center
        VBox ticketListContainer = new VBox(10);
        ticketListContainer.setPrefWidth(600);
        ticketListContainer.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");
        ticketListContainer.getChildren().add(ticketListView.getRoot());
        HBox.setHgrow(ticketListContainer, Priority.ALWAYS);

        // Detail panel on right
        VBox detailContainer = new VBox(10);
        detailContainer.setPrefWidth(400);
        detailContainer.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");
        detailContainer.getChildren().add(detailView.getRoot());

        centerPanel.getChildren().addAll(ticketListContainer, detailContainer);
        this.centerContent = new VBox();
        this.centerContent.getChildren().addAll(centerPanel);
        VBox.setVgrow(centerPanel, Priority.ALWAYS);

        root.setCenter(centerContent);
    }

    private void setupNavigation() {
        sidebarView.setOnDashboardClick(() -> showDashboard());
        sidebarView.setOnAllTicketsClick(() -> showTicketList());
        sidebarView.setOnStatusFilterClick(status -> {
            showTicketList();
            ticketListView.filterByStatus(status);
        });
        sidebarView.setOnPriorityFilterClick(priority -> {
            showTicketList();
            ticketListView.filterByPriority(priority);
        });
        sidebarView.setOnAssigneeFilterClick(assignee -> {
            showTicketList();
            ticketListView.filterByAssignee(assignee);
        });
        sidebarView.setOnClearFiltersClick(() -> {
            showTicketList();
            ticketListView.clearFilters();
        });

        ticketListView.setOnTicketSelected(ticket -> detailView.showTicket(ticket));
        detailView.setOnTicketUpdated(ticket -> ticketListView.refresh());
        detailView.setOnTicketDeleted(() -> {
            ticketListView.refresh();
            detailView.clearView();
        });
    }

    private void showDashboard() {
        centerContent.getChildren().clear();
        dashboardView.refresh();
        VBox dashboardContainer = new VBox(10);
        dashboardContainer.setPadding(new Insets(10));
        dashboardContainer.getChildren().add(dashboardView.getRoot());
        VBox.setVgrow(dashboardView.getRoot(), Priority.ALWAYS);
        centerContent.getChildren().add(dashboardContainer);
    }

    private void showTicketList() {
        centerContent.getChildren().clear();
        HBox centerPanel = new HBox(10);
        centerPanel.setPadding(new Insets(10));
        centerPanel.setStyle("-fx-fill-height: true;");

        VBox ticketListContainer = new VBox(10);
        ticketListContainer.setPrefWidth(600);
        ticketListContainer.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");
        ticketListContainer.getChildren().add(ticketListView.getRoot());
        HBox.setHgrow(ticketListContainer, Priority.ALWAYS);

        VBox detailContainer = new VBox(10);
        detailContainer.setPrefWidth(400);
        detailContainer.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");
        detailContainer.getChildren().add(detailView.getRoot());

        centerPanel.getChildren().addAll(ticketListContainer, detailContainer);
        centerContent.getChildren().add(centerPanel);
    }

    public BorderPane getRoot() {
        return root;
    }
}