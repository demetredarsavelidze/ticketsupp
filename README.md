# Customer Support Ticket Management System

A JavaFX desktop application for managing customer support tickets. Built as a semester final project demonstrating modern Java development practices.

## Features

✅ **Dashboard** - Live statistics and charts showing ticket distribution  
✅ **Ticket Management** - Create, read, update, delete tickets with validation  
✅ **Search & Filter** - Real-time search and multi-criteria filtering  
✅ **Priority & Status Tracking** - Visual badges for quick identification  
✅ **Support Staff Assignment** - Assign tickets to support team members  
✅ **Persistent Storage** - SQLite database (tickets.db)  
✅ **Demo Data** - Pre-loaded sample tickets for testing  
✅ **Business Rules** - Cannot close tickets without assignee  

## Technology Stack

- **Language:** Java 17
- **UI Framework:** JavaFX 21
- **Database:** SQLite
- **Build Tool:** Maven
- **Database Access:** JDBC

## Project Structure

```
com.example.ticketsupp/
├── App.java                    # Application entry point
├── db/
│   ├── Database.java          # Database connection and initialization
│   └── TicketDAO.java         # CRUD operations for tickets
├── model/
│   ├── Ticket.java            # Ticket data model
│   ├── Priority.java          # Priority enumeration
│   ├── TicketStatus.java      # Status enumeration
│   └── TicketStore.java       # ObservableList wrapper and state management
└── view/
    ├── MainView.java          # Main layout controller
    ├── DashboardView.java     # Statistics and charts
    ├── SidebarView.java       # Navigation and filters
    ├── TicketListView.java    # Ticket list with search/filter
    ├── TicketDetailView.java  # Ticket editor
    ├── TicketCell.java        # Custom list cell renderer
    └── NewTicketDialog.java   # New ticket creation dialog
```

## Building & Running

### Prerequisites
- Java 17 or later
- Maven 3.6+

### Build
```bash
mvn clean install
```

### Run
```bash
mvn javafx:run
```

Or:
```bash
java -jar target/ticket-support-1.0.0.jar
```

## Usage

### Creating a Ticket
1. Click "New Ticket" button in the ticket list
2. Fill in all required fields
3. Select priority and optional assignee
4. Click "Create Ticket"

### Managing Tickets
1. Select a ticket from the list
2. View/edit details in the right panel
3. Use buttons to Close, Reopen, or Delete
4. Save changes when done

### Filtering & Searching
- Use sidebar navigation to filter by status, priority, or support staff
- Type in search bar to search by title, code, requester name/email
- Click "Clear Filters" to reset

### Dashboard
- Click "Dashboard" to view statistics and charts
- Load demo data (10 sample tickets) for testing

## Database Schema

```sql
CREATE TABLE tickets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ticket_code TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    requester_name TEXT NOT NULL,
    requester_email TEXT NOT NULL,
    assignee TEXT NOT NULL,
    priority TEXT NOT NULL,
    status TEXT NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    closed_at TEXT
);
```

## Validation Rules

- **Required Fields:** Requester name, email, title, description, priority
- **Email Validation:** Must contain "@" symbol
- **Business Rule:** Cannot close or resolve a ticket without assigning it
- **Timestamps:** Automatically updated on create/modify

## Data Persistence

All tickets are stored in `tickets.db` SQLite database file in the working directory. Data persists between application restarts.

## Test Scenarios

✅ Create ticket → Close app → Reopen → Ticket still exists  
✅ Update ticket → Close app → Reopen → Changes persisted  
✅ Delete ticket → Close app → Reopen → Ticket still deleted  
✅ Load demo data → Close app → Reopen → Demo data persists  
✅ Close ticket → Verify status and closed_at timestamp  
✅ Reopen ticket → Verify status changed and closed_at cleared  

## Semester Requirements Met

- ✅ JavaFX UI
- ✅ Minimum 3 classes (12 total)
- ✅ List/Collection usage (ObservableList)
- ✅ CRUD operations (TicketDAO)
- ✅ JDBC database (SQLite)
- ✅ TableView/ListView with search and filters
- ✅ Chart visualization (BarCharts)
- ✅ Validation and business rules

## Author

Demetred Arsavelidze  
Semester Final Project - 2026
