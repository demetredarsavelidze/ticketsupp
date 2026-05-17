package com.example.ticketsupp.view;

import com.example.ticketsupp.db.TicketDAO;
import com.example.ticketsupp.model.Ticket;
import com.example.ticketsupp.model.TicketPriority;
import com.example.ticketsupp.model.TicketStatus;
import com.example.ticketsupp.model.TicketStore;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Dashboard view showing statistics and charts.
 */
public class DashboardView {
    private VBox root;
    private Label totalLabel;
    private Label openLabel;
    private Label inProgressLabel;
    private Label resolvedLabel;
    private Label closedLabel;
    private Label criticalLabel;
    private BarChart<String, Number> statusChart;
    private BarChart<String, Number> priorityChart;
    private Label recentLabel;

    public DashboardView() {
        this.root = new VBox(15);
        this.root.setPadding(new Insets(20));
        this.root.setStyle("-fx-background-color: #f5f5f5;");
        buildUI();
    }

    private void buildUI() {
        // Title
        Label titleLabel = new Label("📊 Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        root.getChildren().add(titleLabel);

        // Stats cards
        HBox statsBox = new HBox(15);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #fff;");

        totalLabel = createStatCard("Total Tickets", "0", "#2196f3");
        openLabel = createStatCard("Open", "0", "#1976d2");
        inProgressLabel = createStatCard("In Progress", "0", "#7b1fa2");
        resolvedLabel = createStatCard("Resolved", "0", "#388e3c");
        closedLabel = createStatCard("Closed", "0", "#616161");
        criticalLabel = createStatCard("Critical", "0", "#d32f2f");

        statsBox.getChildren().addAll(totalLabel.getParent(), openLabel.getParent(), inProgressLabel.getParent(),
                resolvedLabel.getParent(), closedLabel.getParent(), criticalLabel.getParent());
        root.getChildren().add(statsBox);

        // Load demo data button
        Button loadDemoBtn = new Button("📥 Load Demo Data");
        loadDemoBtn.setStyle("-fx-padding: 10px 20px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px;");
        loadDemoBtn.setOnAction(e -> loadDemoData());
        root.getChildren().add(loadDemoBtn);

        // Charts
        HBox chartsBox = new HBox(15);
        chartsBox.setPadding(new Insets(10));

        statusChart = createStatusChart();
        priorityChart = createPriorityChart();

        chartsBox.getChildren().addAll(statusChart, priorityChart);
        HBox.setHgrow(statusChart, Priority.ALWAYS);
        HBox.setHgrow(priorityChart, Priority.ALWAYS);
        root.getChildren().add(chartsBox);

        // Recent tickets
        VBox recentBox = new VBox(10);
        recentBox.setPadding(new Insets(15));
        recentBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #fff;");

        Label recentTitleLabel = new Label("📝 Recent Tickets (Latest 5)");
        recentTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        recentBox.getChildren().add(recentTitleLabel);

        recentLabel = new Label("No recent tickets");
        recentLabel.setWrapText(true);
        recentBox.getChildren().add(recentLabel);

        root.getChildren().add(recentBox);
        VBox.setVgrow(recentBox, Priority.ALWAYS);
    }

    private Label createStatCard(String title, String value, String color) {
        VBox cardBox = new VBox(5);
        cardBox.setPadding(new Insets(15));
        cardBox.setStyle("-fx-border-color: " + color + "; -fx-border-radius: 5; -fx-background-color: #f9f9f9; -fx-border-width: 2;");
        cardBox.setPrefWidth(150);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        cardBox.getChildren().addAll(titleLabel, valueLabel);
        return valueLabel;
    }

    private BarChart<String, Number> createStatusChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Status");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Tickets by Status");
        chart.setPrefHeight(300);
        return chart;
    }

    private BarChart<String, Number> createPriorityChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Priority");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Tickets by Priority");
        chart.setPrefHeight(300);
        return chart;
    }

    public void refresh() {
        TicketStore store = TicketStore.getInstance();
        int total = store.getTotalCount();
        int open = (int) store.getCountByStatus(TicketStatus.OPEN);
        int inProgress = (int) store.getCountByStatus(TicketStatus.IN_PROGRESS);
        int resolved = (int) store.getCountByStatus(TicketStatus.RESOLVED);
        int closed = (int) store.getCountByStatus(TicketStatus.CLOSED);
        int critical = (int) store.getCountByPriority(TicketPriority.CRITICAL);

        totalLabel.setText(String.valueOf(total));
        openLabel.setText(String.valueOf(open));
        inProgressLabel.setText(String.valueOf(inProgress));
        resolvedLabel.setText(String.valueOf(resolved));
        closedLabel.setText(String.valueOf(closed));
        criticalLabel.setText(String.valueOf(critical));

        updateCharts(store);
        updateRecentTickets(store);
    }

    private void updateCharts(TicketStore store) {
        statusChart.getData().clear();
        priorityChart.getData().clear();

        XYChart.Series<String, Number> statusSeries = new XYChart.Series<>();
        statusSeries.setName("Status");
        statusSeries.getData().add(new XYChart.Data<>("Open", store.getCountByStatus(TicketStatus.OPEN)));
        statusSeries.getData().add(new XYChart.Data<>("In Progress", store.getCountByStatus(TicketStatus.IN_PROGRESS)));
        statusSeries.getData().add(new XYChart.Data<>("Resolved", store.getCountByStatus(TicketStatus.RESOLVED)));
        statusSeries.getData().add(new XYChart.Data<>("Closed", store.getCountByStatus(TicketStatus.CLOSED)));
        statusChart.getData().add(statusSeries);

        XYChart.Series<String, Number> prioritySeries = new XYChart.Series<>();
        prioritySeries.setName("Priority");
        prioritySeries.getData().add(new XYChart.Data<>("Critical", store.getCountByPriority(TicketPriority.CRITICAL)));
        prioritySeries.getData().add(new XYChart.Data<>("High", store.getCountByPriority(TicketPriority.HIGH)));
        prioritySeries.getData().add(new XYChart.Data<>("Medium", store.getCountByPriority(TicketPriority.MEDIUM)));
        prioritySeries.getData().add(new XYChart.Data<>("Low", store.getCountByPriority(TicketPriority.LOW)));
        priorityChart.getData().add(prioritySeries);
    }

    private void updateRecentTickets(TicketStore store) {
        var recentTickets = store.getTickets().stream()
                .limit(5)
                .toList();
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : recentTickets) {
            sb.append("• ").append(ticket.getTicketCode()).append(" - ").append(ticket.getTitle())
                    .append(" (").append(ticket.getStatus().getDisplayName()).append(")\n");
        }
        recentLabel.setText(sb.toString().isEmpty() ? "No recent tickets" : sb.toString());
    }

    private void loadDemoData() {
        if (TicketDAO.isEmpty()) {
            TicketDAO.insertDemoDataIfEmpty();
            TicketStore.getInstance().refresh();
            refresh();
        } else {
            showInfo("Info", "Demo data already exists.");
        }
    }

    private void showInfo(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}