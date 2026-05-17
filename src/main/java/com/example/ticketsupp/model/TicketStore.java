package com.example.ticketsupp.model;

import com.example.ticketsupp.db.TicketDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Singleton class that manages the ObservableList of tickets.
 * Acts as a bridge between the database and the UI.
 */
public class TicketStore {
    private static TicketStore instance;
    private ObservableList<Ticket> tickets;

    private TicketStore() {
        this.tickets = FXCollections.observableArrayList();
    }

    /**
     * Get the singleton instance of TicketStore.
     */
    public static TicketStore getInstance() {
        if (instance == null) {
            instance = new TicketStore();
        }
        return instance;
    }

    /**
     * Get the ObservableList of tickets.
     */
    public ObservableList<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Refresh tickets from database.
     */
    public void refresh() {
        tickets.setAll(TicketDAO.getAllTickets());
    }

    /**
     * Add a new ticket to the store and database.
     */
    public void addTicket(Ticket ticket) {
        if (TicketDAO.insertTicket(ticket)) {
            refresh();
        }
    }

    /**
     * Update an existing ticket in the store and database.
     */
    public void updateTicket(Ticket ticket) {
        if (TicketDAO.updateTicket(ticket)) {
            refresh();
        }
    }

    /**
     * Delete a ticket from the store and database.
     */
    public void deleteTicket(int ticketId) {
        if (TicketDAO.deleteTicket(ticketId)) {
            refresh();
        }
    }

    /**
     * Get a ticket by ID.
     */
    public Ticket getTicketById(int id) {
        return tickets.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get total ticket count.
     */
    public int getTotalCount() {
        return tickets.size();
    }

    /**
     * Get count of tickets by status.
     */
    public long getCountByStatus(TicketStatus status) {
        return tickets.stream()
                .filter(t -> t.getStatus() == status)
                .count();
    }

    /**
     * Get count of tickets by priority.
     */
    public long getCountByPriority(Priority priority) {
        return tickets.stream()
                .filter(t -> t.getPriority() == priority)
                .count();
    }
}