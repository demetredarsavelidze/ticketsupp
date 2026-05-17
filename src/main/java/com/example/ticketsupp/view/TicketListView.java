package com.example.ticketsupp.view;

import com.example.ticketsupp.model.Priority;
import com.example.ticketsupp.model.Ticket;
import com.example.ticketsupp.model.TicketStatus;
import com.example.ticketsupp.model.TicketStore;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * View for displaying and searching/filtering the ticket list.
 */
public class TicketListView {
    private VBox root;
    private ListView<Ticket> ticketList;
    private TextField searchField;
    private FilteredList<Ticket> filteredList;
    private java.util.function.Consumer<Ticket> onTicketSelected;
    private TicketStatus statusFilter;
    private Priority priorityFilter;
    private String assigneeFilter;

    public TicketListView() {
        this.root = new VBox(10);
        this.root.setPadding(new Insets(10));
        buildUI();
        setupFiltering();
    }

    private void buildUI() {
        // Search bar
        HBox searchBox = new HBox(5);
        searchBox.setPadding(new Insets(5));
        searchBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #fff;");

        Label searchLabel = new Label("🔍 Search:");
        searchField = new TextField();
        searchField.setPromptText("Search by title, code, name, or email...");
        searchField.setStyle("-fx-font-size: 11px;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchBox.getChildren().addAll(searchLabel, searchField);
        root.getChildren().add(searchBox);

        // New Ticket button
        Button newTicketBtn = new Button("➕ New Ticket");
        newTicketBtn.setStyle("-fx-padding: 8px 15px; -fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #4CAF50;");
        newTicketBtn.setOnAction(e -> showNewTicketDialog());
        root.getChildren().add(newTicketBtn);

        // Ticket list
        ticketList = new ListView<>();
        ticketList.setCellFactory(param -> new TicketCell());
        ticketList.setOnMouseClicked(e -> {
            Ticket selected = ticketList.getSelectionModel().getSelectedItem();
            if (selected != null && onTicketSelected != null) {
                onTicketSelected.accept(selected);
            }
        });

        root.getChildren().add(ticketList);
        VBox.setVgrow(ticketList, Priority.ALWAYS);
    }

    private void setupFiltering() {
        filteredList = new FilteredList<>(TicketStore.getInstance().getTickets(), p -> true);
        ticketList.setItems(filteredList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilters());
    }

    private void updateFilters() {
        filteredList.setPredicate(ticket -> {
            // Search filter
            String searchText = searchField.getText().toLowerCase();
            boolean matchesSearch = ticket.getTitle().toLowerCase().contains(searchText) ||
                    ticket.getTicketCode().toLowerCase().contains(searchText) ||
                    ticket.getRequesterName().toLowerCase().contains(searchText) ||
                    ticket.getRequesterEmail().toLowerCase().contains(searchText);

            if (!matchesSearch) return false;

            // Status filter
            if (statusFilter != null && ticket.getStatus() != statusFilter) {
                return false;
            }

            // Priority filter
            if (priorityFilter != null && ticket.getPriority() != priorityFilter) {
                return false;
            }

            // Assignee filter
            if (assigneeFilter != null && !ticket.getAssignee().equals(assigneeFilter)) {
                return false;
            }

            return true;
        });
    }

    public void filterByStatus(TicketStatus status) {
        this.statusFilter = status;
        updateFilters();
    }

    public void filterByPriority(Priority priority) {
        this.priorityFilter = priority;
        updateFilters();
    }

    public void filterByAssignee(String assignee) {
        this.assigneeFilter = assignee;
        updateFilters();
    }

    public void clearFilters() {
        this.statusFilter = null;
        this.priorityFilter = null;
        this.assigneeFilter = null;
        this.searchField.clear();
        updateFilters();
    }

    public void refresh() {
        TicketStore.getInstance().refresh();
        updateFilters();
    }

    public void setOnTicketSelected(java.util.function.Consumer<Ticket> handler) {
        this.onTicketSelected = handler;
    }

    private void showNewTicketDialog() {
        NewTicketDialog dialog = new NewTicketDialog();
        dialog.showAndWait();
        refresh();
    }

    public VBox getRoot() {
        return root;
    }
}