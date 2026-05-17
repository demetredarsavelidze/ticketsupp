package com.example.ticketsupp.db;

import com.example.ticketsupp.model.Ticket;
import com.example.ticketsupp.model.TicketPriority;
import com.example.ticketsupp.model.TicketStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Ticket CRUD operations.
 * Handles all SQL queries for the tickets table.
 */
public class TicketDAO {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Create the tickets table if it doesn't exist.
     */
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ticket_code TEXT NOT NULL UNIQUE,"
                + "title TEXT NOT NULL,"
                + "description TEXT NOT NULL,"
                + "requester_name TEXT NOT NULL,"
                + "requester_email TEXT NOT NULL,"
                + "assignee TEXT NOT NULL,"
                + "priority TEXT NOT NULL,"
                + "status TEXT NOT NULL,"
                + "created_at TEXT NOT NULL,"
                + "updated_at TEXT NOT NULL,"
                + "closed_at TEXT"
                + ")";

        try (Statement stmt = Database.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Tickets table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Insert a new ticket into the database.
     * @param ticket The ticket object to insert
     * @return true if successful, false otherwise
     */
    public static boolean insertTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (ticket_code, title, description, requester_name, requester_email, "
                + "assignee, priority, status, created_at, updated_at, closed_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTicketCode());
            pstmt.setString(2, ticket.getTitle());
            pstmt.setString(3, ticket.getDescription());
            pstmt.setString(4, ticket.getRequesterName());
            pstmt.setString(5, ticket.getRequesterEmail());
            pstmt.setString(6, ticket.getAssignee());
            pstmt.setString(7, ticket.getPriority().toString());
            pstmt.setString(8, ticket.getStatus().toString());
            pstmt.setString(9, ticket.getCreatedAt().format(DATE_FORMAT));
            pstmt.setString(10, ticket.getUpdatedAt().format(DATE_FORMAT));
            pstmt.setString(11, ticket.getClosedAt() != null ? ticket.getClosedAt().format(DATE_FORMAT) : null);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing ticket in the database.
     * @param ticket The ticket object with updated values
     * @return true if successful, false otherwise
     */
    public static boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET title=?, description=?, requester_name=?, requester_email=?, "
                + "assignee=?, priority=?, status=?, updated_at=?, closed_at=? WHERE id=?";

        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTitle());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setString(3, ticket.getRequesterName());
            pstmt.setString(4, ticket.getRequesterEmail());
            pstmt.setString(5, ticket.getAssignee());
            pstmt.setString(6, ticket.getPriority().toString());
            pstmt.setString(7, ticket.getStatus().toString());
            pstmt.setString(8, ticket.getUpdatedAt().format(DATE_FORMAT));
            pstmt.setString(9, ticket.getClosedAt() != null ? ticket.getClosedAt().format(DATE_FORMAT) : null);
            pstmt.setInt(10, ticket.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a ticket from the database.
     * @param id The ticket ID to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteTicket(int id) {
        String sql = "DELETE FROM tickets WHERE id=?";

        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve all tickets from the database.
     * @return List of all tickets
     */
    public static List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY id DESC";

        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setTicketCode(rs.getString("ticket_code"));
                ticket.setTitle(rs.getString("title"));
                ticket.setDescription(rs.getString("description"));
                ticket.setRequesterName(rs.getString("requester_name"));
                ticket.setRequesterEmail(rs.getString("requester_email"));
                ticket.setAssignee(rs.getString("assignee"));
                ticket.setPriority(TicketPriority.valueOf(rs.getString("priority")));
                ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
                ticket.setCreatedAt(LocalDateTime.parse(rs.getString("created_at"), DATE_FORMAT));
                ticket.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at"), DATE_FORMAT));
                String closedAt = rs.getString("closed_at");
                if (closedAt != null && !closedAt.isEmpty()) {
                    ticket.setClosedAt(LocalDateTime.parse(closedAt, DATE_FORMAT));
                }
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tickets: " + e.getMessage());
        }
        return tickets;
    }

    /**
     * Count total tickets in the database.
     * @return Number of tickets
     */
    public static int countTickets() {
        String sql = "SELECT COUNT(*) FROM tickets";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting tickets: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Check if the tickets table is empty.
     * @return true if no tickets exist, false otherwise
     */
    public static boolean isEmpty() {
        return countTickets() == 0;
    }

    /**
     * Insert demo data into the tickets table if it's empty.
     */
    public static void insertDemoDataIfEmpty() {
        if (!isEmpty()) {
            System.out.println("Demo data already exists.");
            return;
        }

        Ticket[] demoTickets = new Ticket[] {
            createTicket("TKT-A3F9B2", "Cannot login", "Customer cannot access account after password reset.",
                    "Ana Maisuradze", "ana@example.com", "Alice Johnson", TicketPriority.HIGH, TicketStatus.OPEN),
            createTicket("TKT-B7K2M5", "Payment failed", "Payment was charged but order was not completed.",
                    "Giorgi Beridze", "giorgi@example.com", "Bob Smith", TicketPriority.CRITICAL, TicketStatus.IN_PROGRESS),
            createTicket("TKT-C1N8P3", "Password reset email not arriving", "Customer did not receive password reset email.",
                    "Mariam Kapanadze", "mariam@example.com", "Carol White", TicketPriority.MEDIUM, TicketStatus.RESOLVED),
            createTicket("TKT-D5Q4R6", "Dashboard charts not loading", "User reports that dashboard statistics are not visible.",
                    "Nika Gelashvili", "nika@example.com", "David Lee", TicketPriority.HIGH, TicketStatus.OPEN),
            createTicket("TKT-E9T7U2", "Request dark mode support", "Customer requested a dark mode option.",
                    "Lika Tsereteli", "lika@example.com", "Alice Johnson", TicketPriority.LOW, TicketStatus.CLOSED),
            createTicket("TKT-F3V1W8", "CSV export produces corrupted file", "Exported CSV file does not open correctly.",
                    "Saba K.", "saba@example.com", "Bob Smith", TicketPriority.HIGH, TicketStatus.RESOLVED),
            createTicket("TKT-G6X9Y4", "Account locked after failed attempts", "Customer account was locked after multiple login attempts.",
                    "Tekla M.", "tekla@example.com", "Carol White", TicketPriority.CRITICAL, TicketStatus.IN_PROGRESS),
            createTicket("TKT-H2Z5A7", "Notification settings not saving", "Notification preferences reset after closing the app.",
                    "Luka D.", "luka@example.com", "David Lee", TicketPriority.MEDIUM, TicketStatus.OPEN),
            createTicket("TKT-I8B3C1", "Wrong name displayed on profile", "Customer profile shows incorrect display name.",
                    "Salome B.", "salome@example.com", "Alice Johnson", TicketPriority.LOW, TicketStatus.RESOLVED),
            createTicket("TKT-J4D6E9", "App freezes when opening reports", "Application freezes when the reports page is opened.",
                    "Irakli T.", "irakli@example.com", "Bob Smith", TicketPriority.HIGH, TicketStatus.CLOSED)
        };

        for (Ticket ticket : demoTickets) {
            insertTicket(ticket);
        }
        System.out.println("Demo data inserted successfully.");
    }

    /**
     * Helper method to create a demo ticket.
     */
    private static Ticket createTicket(String code, String title, String description,
                                       String requesterName, String requesterEmail,
                                       String assignee, TicketPriority priority, TicketStatus status) {
        Ticket ticket = new Ticket();
        ticket.setTicketCode(code);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setRequesterName(requesterName);
        ticket.setRequesterEmail(requesterEmail);
        ticket.setAssignee(assignee);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        LocalDateTime now = LocalDateTime.now();
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        if (status == TicketStatus.CLOSED || status == TicketStatus.RESOLVED) {
            ticket.setClosedAt(now);
        }
        return ticket;
    }
}