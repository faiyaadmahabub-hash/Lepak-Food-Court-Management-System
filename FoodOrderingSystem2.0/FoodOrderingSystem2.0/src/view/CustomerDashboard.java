/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.CustomerController;
import model.Customer;
import model.Order;
import model.MenuItem;
import model.Review;
import util.FileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class CustomerDashboard extends JFrame {

    private Customer customer;
    private CustomerController customerController;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton viewMenuBtn, placeOrderBtn, orderStatusBtn, orderHistoryBtn, transactionHistoryBtn, cancelOrderBtn, reviewOrderBtn, reviewRunnerBtn, viewReviewBtn, logoutBtn;
    private JLabel walletLabel;

    // ✅ Constructor
    public CustomerDashboard(Customer customer, CustomerController customerController) {
        this.customer = customer;
        this.customerController = customerController;

        initComponents();
        refreshWalletDisplay();

        setTitle("Customer Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ✅ Wallet Balance Display
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(34, 153, 84)); // Green background

        walletLabel = new JLabel("Wallet Balance: RM " + customerController.getWalletBalance(), SwingConstants.CENTER);
        walletLabel.setFont(new Font("Arial", Font.BOLD, 20));
        walletLabel.setForeground(Color.WHITE);
        walletLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(walletLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ✅ Table for displaying orders
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Item", "Quantity", "Status"}, 0);
        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ordersTable.setRowHeight(30);
        ordersTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection color
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // ✅ Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4, 10, 10)); // 2 rows, 4 columns with spacing

        viewMenuBtn = new JButton("View Menu");
        placeOrderBtn = new JButton("Place Order");
        orderStatusBtn = new JButton("Check Order Status");
        orderHistoryBtn = new JButton("Order History");
        transactionHistoryBtn = new JButton("Transaction History");
        cancelOrderBtn = new JButton("Cancel Order");
        reviewOrderBtn = new JButton("Review Order");
        reviewRunnerBtn = new JButton("Review Delivery Runner");
        viewReviewBtn = new JButton("Customer Reviews");
        logoutBtn = new JButton("Logout");

        // ✅ Style buttons
        JButton[] buttons = {viewMenuBtn, placeOrderBtn, orderStatusBtn, orderHistoryBtn, transactionHistoryBtn,
            cancelOrderBtn, reviewOrderBtn, reviewRunnerBtn, viewReviewBtn, logoutBtn};

        for (JButton btn : buttons) {
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(new Color(52, 152, 219)); // Blue buttons
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.SOUTH);

        // ✅ Load orders from file
        loadOrders();

        // ✅ Button Actions
        viewMenuBtn.addActionListener(e -> viewMenu());
        placeOrderBtn.addActionListener(e -> placeOrder());
        orderStatusBtn.addActionListener(e -> checkOrderStatus());
        orderHistoryBtn.addActionListener(e -> checkOrderHistory());
        transactionHistoryBtn.addActionListener(e -> viewTransactionHistory());
        cancelOrderBtn.addActionListener(e -> cancelOrder());
        reviewOrderBtn.addActionListener(e -> submitReview());
        reviewRunnerBtn.addActionListener(e -> reviewDeliveryRunner());
        viewReviewBtn.addActionListener(e -> viewCustomerReviews());
        logoutBtn.addActionListener(e -> logout());

    }

    // ✅ Load Orders
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = customerController.getOrderHistory();
        for (Order order : orders) {
            String itemName = (order.getMenuItem() != null) ? order.getMenuItem().getName() : "Unknown Item"; // ✅ Prevents crash
            tableModel.addRow(new Object[]{order.getOrderID(), itemName, order.getQuantity(), order.getStatus()});
        }
    }

    // ✅ View Menu Items
    private void viewMenu() {
        List<MenuItem> menuItems = customerController.viewMenu();
        StringBuilder menuList = new StringBuilder("Menu:\n");
        for (MenuItem item : menuItems) {
            menuList.append(item.getItemId()).append(" - ").append(item.getName()).append(" - RM ").append(item.getPrice()).append("\n");
        }
        JOptionPane.showMessageDialog(this, menuList.toString(), "Menu", JOptionPane.INFORMATION_MESSAGE);
    }

    // Place an Order
    // Fix for the placeOrder method in CustomerDashboard.java
    private void placeOrder() {
        String itemId = JOptionPane.showInputDialog(this, "Enter Menu Item ID:");
        if (itemId == null || itemId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Menu Item ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String quantityInput = JOptionPane.showInputDialog(this, "Enter Quantity:");
        int quantity;

        try {
            quantity = Integer.parseInt(quantityInput);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid quantity! Must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] deliveryOptions = {"Dine In (No Fee)", "Regular Delivery (RM 2.00)", "Express Delivery (RM 5.00)"};

        // Show delivery option dialog
        JComboBox<String> deliveryComboBox = new JComboBox<>(deliveryOptions);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Delivery Service:"));
        panel.add(deliveryComboBox);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Delivery Options",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Process the result
        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = deliveryComboBox.getSelectedIndex();
            double deliveryFee = 0.0;
            String deliveryType = "DINE_IN";

            switch (selectedIndex) {
                case 0: // Dine In
                    deliveryFee = 0.0;
                    deliveryType = "DINE_IN";
                    break;
                case 1: // Regular Delivery
                    deliveryFee = 2.0;
                    deliveryType = "REGULAR";
                    break;
                case 2: // Express Delivery
                    deliveryFee = 5.0;
                    deliveryType = "EXPRESS";
                    break;
            }

            // KEY FIX: Call the placeOrderWithDelivery method instead of placeOrder
            boolean success = customerController.placeOrderWithDelivery(itemId, quantity, deliveryType, deliveryFee);

            if (success) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshWalletDisplay(); // Refresh wallet balance
                loadOrders(); // Refresh order list
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place order! Insufficient balance or invalid item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Check Order Status

    private void checkOrderStatus() {
        String orderId = JOptionPane.showInputDialog(this, "Enter Order ID:");
        String status = customerController.trackOrderStatus(orderId);
        JOptionPane.showMessageDialog(this, "Order Status: " + status, "Order Status", JOptionPane.INFORMATION_MESSAGE);
    }

    // Check Order History
    private void checkOrderHistory() {
        StringBuilder history = new StringBuilder("Order History:\n");
        List<Order> orders = customerController.getOrderHistory();
        for (Order order : orders) {
            history.append("Order ID: ").append(order.getOrderID()).append(", Item: ").append(order.getMenuItem().getName())
                    .append(", Quantity: ").append(order.getQuantity()).append(", Status: ").append(order.getStatus()).append("\n");
        }
        JOptionPane.showMessageDialog(this, history.toString(), "Order History", JOptionPane.INFORMATION_MESSAGE);
    }

    // View Transaction History
    private void viewTransactionHistory() {
        List<String[]> transactions = customerController.getTransactionHistory();

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transaction history found.",
                    "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a formatted display of transactions
        StringBuilder history = new StringBuilder();
        history.append("Transaction History for ").append(customer.getName()).append("\n\n");

        for (String[] transaction : transactions) {
            String transactionId = transaction[0];
            String type = transaction[2];
            double amount = Double.parseDouble(transaction[3]);
            String date = transaction[4];

            history.append("Date: ").append(date).append("\n");
            history.append("Transaction ID: ").append(transactionId).append("\n");
            history.append("Type: ").append(type).append("\n");
            history.append("Amount: RM ").append(String.format("%.2f", amount)).append("\n");
            history.append("-----------------------------------\n");
        }

        // Display in a scrollable text area
        JTextArea textArea = new JTextArea(history.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Transaction History",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Cancel Order
    private void cancelOrder() {
        String orderId = JOptionPane.showInputDialog(this, "Enter Order ID to Cancel:");
        boolean success = customerController.cancelOrder(orderId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Order canceled successfully!");
            loadOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to cancel order.");
        }
    }

    private void refreshWalletDisplay() {
        if (customer == null) {
            System.out.println("Error: currentCustomer is NULL!"); // ✅ Debugging
            return; // ✅ Prevent crash
        }

        customer.refreshWalletBalance(); // ✅ Fetch latest balance

        if (walletLabel != null) {
            walletLabel.setText("Wallet Balance: RM " + customer.getWalletBalance());
        } else {
            System.out.println("Error: walletLabel is NULL!"); // ✅ Debugging
        }
    }

    private void submitReview() {
        String orderId = JOptionPane.showInputDialog(this, "Enter Order ID:");
        if (orderId == null || orderId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Order ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reviewText = JOptionPane.showInputDialog(this, "Enter Your Review:");
        if (reviewText == null || reviewText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Review cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ratingInput = JOptionPane.showInputDialog(this, "Enter Rating (1-5):");
        int rating;

        try {
            rating = Integer.parseInt(ratingInput);
            if (rating < 1 || rating > 5) {
                JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rating! Please enter a number between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // **Ensure Order Exists**
        boolean orderExists = customerController.checkOrderExists(orderId);
        if (!orderExists) {
            JOptionPane.showMessageDialog(this, "Order ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        customerController.submitReview(orderId, reviewText, rating);
    }

    private void reviewDeliveryRunner() {
        String drId = JOptionPane.showInputDialog(this, "Enter Delivery Runner ID:");
        if (drId == null || drId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Delivery Runner ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Check if DR exists before allowing review
        if (!customerController.checkDeliveryRunnerExists(drId)) {
            JOptionPane.showMessageDialog(this, "Delivery Runner ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reviewText = JOptionPane.showInputDialog(this, "Enter Your Review:");
        if (reviewText == null || reviewText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Review cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ratingInput = JOptionPane.showInputDialog(this, "Enter Rating (1-5):");
        int rating;

        try {
            rating = Integer.parseInt(ratingInput);
            if (rating < 1 || rating > 5) { // ✅ Ensure rating is between 1-5
                JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rating! Please enter a number between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        customerController.submitRunnerReview(drId, reviewText, rating);
    }

    // check all customer reviews
    private void viewCustomerReviews() {
        List<Review> reviews = customerController.getCustomerReviews();

        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You haven't submitted any reviews yet.",
                    "Review History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Get additional info about what was reviewed
        Map<String, String> targetInfo = customerController.getReviewTargetInfo();

        // Create table model with column headers
        String[] columnNames = {"Target", "Rating", "Review Comment"};
        Object[][] data = new Object[reviews.size()][columnNames.length];

        // Populate data
        for (int i = 0; i < reviews.size(); i++) {
            Review review = reviews.get(i);
            String target = targetInfo.getOrDefault(review.getOrderId(), review.getOrderId());

            data[i][0] = target;
            data[i][1] = review.getRating() + "/5";
            data[i][2] = review.getReviewText();
        }

        // Create and configure the table
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        // Make table columns not resizable by user
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Create panel with title
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Review History", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Show dialog
        JOptionPane.showMessageDialog(this, panel, "Review History",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ✅ Logout
    private void logout() {
        dispose();
        new LoginPage().setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1370, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> {
            Customer currentCustomer = new Customer("CUST001", "password123", "John Doe", "0123456789", "123 Main Street", "0.0");
            new CustomerDashboard(currentCustomer, new CustomerController(currentCustomer)).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
