package controller;

import model.Customer;
import model.Order;
import model.MenuItem;
import model.Review;
import util.FileHandler;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JOptionPane;
import model.DR;
import model.User;

public class CustomerController {

    private Customer customer;

    // Constructor
    public CustomerController(Customer customer) {
        this.customer = customer;
    }

    // Get Wallet Balance
    public double getWalletBalance() {
        return customer.getWalletBalance();
    }

    // Add Balance to Wallet
    public void addBalance(double amount) {
        if (amount > 0) {
            customer.addBalance(amount);
            FileHandler.updateUser(customer); // Save updated balance
        }
    }

    // Deduct Balance (For Orders)
    public boolean makePayment(double amount) {
        if (customer.deductBalance(amount)) {
            FileHandler.updateUser(customer); // Save updated balance
            return true;
        }
        return false;
    }

    // View Available Menu Items
    public List<MenuItem> viewMenu() {
        return FileHandler.loadMenuItems();
    }

    // Place an Order (Checks Wallet Balance First)
    public boolean placeOrder(String itemId, int quantity) {
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid quantity! Must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<MenuItem> menuItems = FileHandler.loadMenuItems();
        MenuItem selectedItem = menuItems.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "Invalid menu item ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (selectedItem.getVendor() == null) { // Ensure Vendor Exists
            JOptionPane.showMessageDialog(null, "The selected item has no assigned vendor!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double totalPrice = selectedItem.getPrice() * quantity;
        if (customer.getWalletBalance() < totalPrice) {
            JOptionPane.showMessageDialog(null, "Insufficient wallet balance!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Deduct balance & Create Order
        customer.deductBalance(totalPrice);
        String orderId = "ORD" + (FileHandler.loadOrders().size() + 1);
        Order newOrder = new Order(orderId, selectedItem.getVendor().getUserID(), customer.getUserID(), "", selectedItem, quantity, "PENDING");

        // Save Order & Update Wallet
        List<Order> orders = FileHandler.loadOrders();
        orders.add(newOrder);
        FileHandler.saveOrders(orders);
        FileHandler.updateUser(customer);
        return true;
    }

    // Check if a delivery runner exists
    public boolean checkDeliveryRunnerExists(String drId) {
        List<User> users = FileHandler.loadUsers(); // Load all users
        return users.stream()
                .anyMatch(user -> user instanceof DR && user.getUserID().equals(drId)); // Check if DR ID exists
    }

    // Check if an order exists for the customer
    public boolean checkOrderExists(String orderId) {
        List<Order> orders = FileHandler.loadOrders();
        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getCustomerID().equals(customer.getUserID())) {
                return true;
            }
        }
        return false;
    }

    // Get Customer's Order History
    public List<Order> getOrderHistory() {
        return FileHandler.loadOrders().stream()
                .filter(order -> order.getCustomerID().equals(customer.getUserID()))
                .collect(Collectors.toList());
    }

    // Track Order Status
    public String trackOrderStatus(String orderId) {
        return FileHandler.loadOrders().stream()
                .filter(order -> order.getOrderID().equals(orderId) && order.getCustomerID().equals(customer.getUserID()))
                .map(Order::getStatus)
                .findFirst()
                .orElse("Order not found.");
    }

    // Cancel an Order (Refunds Balance if Not Delivered)
    public boolean cancelOrder(String orderId) {
        List<Order> orders = FileHandler.loadOrders();
        Order orderToCancel = null;

        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getCustomerID().equals(customer.getUserID())) {
                orderToCancel = order;
                break;
            }
        }

        if (orderToCancel == null) {
            JOptionPane.showMessageDialog(null, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Refund balance if order is still PENDING
        if (orderToCancel.getStatus().equals("PENDING")) {
            customer.addBalance(orderToCancel.getMenuItem().getPrice() * orderToCancel.getQuantity());
            FileHandler.updateUser(customer);
        } else {
            JOptionPane.showMessageDialog(null, "Order cannot be canceled as it is already processed.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        orders.remove(orderToCancel);
        FileHandler.saveOrders(orders);
        JOptionPane.showMessageDialog(null, "Order canceled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public void submitReview(String orderId, String reviewText, int rating) {
        if (!checkOrderExists(orderId)) {
            JOptionPane.showMessageDialog(null, "Order ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Review review = new Review(orderId, customer.getUserID(), reviewText, rating);
        FileHandler.saveReview(review);
        JOptionPane.showMessageDialog(null, "Review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void submitRunnerReview(String drId, String reviewText, int rating) {
        boolean runnerExists = FileHandler.loadUsers().stream()
                .anyMatch(user -> user instanceof DR && user.getUserID().equals(drId));

        if (!runnerExists) {
            JOptionPane.showMessageDialog(null, "Delivery Runner ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Review review = new Review(drId, customer.getUserID(), reviewText, rating);
        FileHandler.saveReview(review);
        JOptionPane.showMessageDialog(null, "Delivery Runner review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // New Method to get Customer Review
    public List<Review> getCustomerReviews() {
        List<Review> customerReviews = new ArrayList<>();

        try {
            // Load order reviews
            File file = new File("data/reviews.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        String reviewId = parts[0];  // This could be order ID or DR ID
                        String customerId = parts[1];
                        String reviewText = parts[2];
                        int rating = Integer.parseInt(parts[3]);

                        // Only include reviews by this customer
                        if (customerId.equals(customer.getUserID())) {
                            Review review = new Review(reviewId, customerId, reviewText, rating);
                            customerReviews.add(review);
                        }
                    }
                }
                reader.close();
            }

            // Also check for runner reviews if stored separately
            File runnerReviewFile = new File("data/runner_reviews.txt");
            if (runnerReviewFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(runnerReviewFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        String runnerId = parts[0];
                        String customerId = parts[1];
                        String reviewText = parts[2];
                        int rating = Integer.parseInt(parts[3]);

                        if (customerId.equals(customer.getUserID())) {
                            Review review = new Review(runnerId, customerId, reviewText, rating);
                            customerReviews.add(review);
                        }
                    }
                }
                reader.close();
            }

        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
            e.printStackTrace();
        }

        return customerReviews;
    }

    // New Method for get Review Target Info, to look up additional information
    public Map<String, String> getReviewTargetInfo() {
        Map<String, String> targetInfo = new HashMap<>();

        // Load menu items to get vendor info
        List<MenuItem> menuItems = FileHandler.loadMenuItems();

        // Load orders to identify what was ordered
        List<Order> orders = FileHandler.loadOrders();
        for (Order order : orders) {
            if (order.getCustomerID().equals(customer.getUserID())) {
                String orderItem = "";
                if (order.getMenuItem() != null) {
                    orderItem = order.getMenuItem().getName();
                }
                targetInfo.put(order.getOrderID(), "Order: " + orderItem + " from " + order.getVendorID());
            }
        }

        // Load delivery runners for runner reviews
        List<DR> runners = FileHandler.loadDeliveryRunners();
        for (DR runner : runners) {
            targetInfo.put(runner.getUserID(), "Delivery Runner: " + runner.getName());
        }

        return targetInfo;
    }

    // Add this method to CustomerController.java
    public boolean placeOrderWithDelivery(String itemId, int quantity, String deliveryType, double deliveryFee) {
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid quantity! Must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        List<MenuItem> menuItems = FileHandler.loadMenuItems();
        MenuItem selectedItem = menuItems.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "Invalid menu item ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (selectedItem.getVendor() == null) {
            JOptionPane.showMessageDialog(null, "The selected item has no assigned vendor!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Calculate total price including delivery fee
        double itemTotal = selectedItem.getPrice() * quantity;
        double totalPrice = itemTotal + deliveryFee;

        if (customer.getWalletBalance() < totalPrice) {
            JOptionPane.showMessageDialog(null, "Insufficient wallet balance! Total: RM "
                    + String.format("%.2f", totalPrice)
                    + " (Item: RM " + String.format("%.2f", itemTotal)
                    + " + Delivery: RM " + String.format("%.2f", deliveryFee) + ")",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Deduct balance & Create Order
        customer.deductBalance(totalPrice);
        String orderId = "ORD" + (FileHandler.loadOrders().size() + 1);

        // Set initial status based on delivery type
        String initialStatus;
        if (deliveryType.equals("DINE_IN")) {
            initialStatus = "DINE IN"; // Set to "DINE IN" for dine-in orders
        } else {
            initialStatus = "PENDING"; // Use "PENDING" for delivery orders
        }

        // Create a new Order with the appropriate initial status
        Order newOrder = new Order(orderId, selectedItem.getVendor().getUserID(),
                customer.getUserID(), "", selectedItem, quantity, initialStatus);

        // Add delivery information to order
        newOrder.setDeliveryType(deliveryType);
        newOrder.setDeliveryFee(deliveryFee);

        // For DINE_IN orders, set assignedDR to "DINE_IN" right away
        if (deliveryType.equals("DINE_IN")) {
            newOrder.setAssignedDR("DINE_IN");
        }

        // Save Order & Update Wallet
        List<Order> orders = FileHandler.loadOrders();
        orders.add(newOrder);
        FileHandler.saveOrders(orders);
        FileHandler.updateUser(customer);

        return true;
    }

    //method to get transaction history
    public List<String[]> getTransactionHistory() {
        List<String[]> transactions = new ArrayList<>();

        try {
            File file = new File("data/transactions.txt");
            if (!file.exists()) {
                System.out.println("Transaction file not found");
                return transactions;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    // Only add transactions for this customer
                    if (parts[1].equals(customer.getUserID())) {
                        transactions.add(parts);
                    }
                }
            }
            reader.close();

            // Sort transactions by date (newest first)
            Collections.sort(transactions, (a, b) -> b[4].compareTo(a[4]));

        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }

}
