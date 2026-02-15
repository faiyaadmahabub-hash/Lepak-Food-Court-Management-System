package controller;

import model.DR;
import model.Order;
import util.FileHandler;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import model.Review;
import java.util.stream.Collectors;

public class DRController {

    private DR deliveryRunner;

    public DRController(DR deliveryRunner) {
        this.deliveryRunner = deliveryRunner;
    }

    // Get Orders Assigned to the Delivery Runner (excluding DINE_IN orders)
    public List<String[]> getAssignedOrders() {
        List<Order> allOrders = FileHandler.loadOrders(); // Load all orders
        List<String[]> assignedOrders = new ArrayList<>();
        
       
        for (Order order : allOrders) {
            // Only include orders that:
            // 1. Are assigned to this delivery runner
            // 2. Are NOT marked as DINE_IN in the assignedDR field
            if (order.getAssignedDR().equals(deliveryRunner.getUserID())
                    && !order.getAssignedDR().equals("DINE_IN")) 
             {

                String[] orderDetails = {
                    order.getOrderID(),
                    order.getVendorID(),
                    order.getCustomerID(),
                    order.getStatus()
                };

                assignedOrders.add(orderDetails);
                
              
            }
        }

        return assignedOrders;
    }

    public List<String[]> getPreparingOrders() {
        List<Order> preparingOrders = FileHandler.loadOrders().stream()
                .filter(order -> order.getStatus().equals("PREPARING")
                && order.getAssignedDR().equals(deliveryRunner.getUserID()))
                .collect(Collectors.toList());

        List<String[]> orderList = new ArrayList<>();
        for (Order order : preparingOrders) {
            String[] orderDetails = {order.getOrderID(), order.getVendorID(),
                order.getCustomerID(), "PREPARING"};
            orderList.add(orderDetails);
        }

        return orderList;
    }

    // Accept a Delivery Order
    public boolean acceptDelivery(String orderId) {
        List<Order> orders = FileHandler.loadOrders();

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getOrderID().equals(orderId)
                    && (order.getAssignedDR().equals(deliveryRunner.getUserID())
                    || order.getAssignedDR().equals(""))) {

                // Ensure the order is either unassigned or already assigned to this runner
                order.setAssignedDR(deliveryRunner.getUserID());
                order.setStatus("IN TRANSIT");

                FileHandler.saveOrders(orders);
                return true;
            }
        }

        return false;
    }
    
    // DECLINE A DELIVERY ORDER (NEW)
    public boolean declineDelivery(String orderId) {
    List<Order> orders = FileHandler.loadOrders();
    boolean orderFound = false;
    
    for (Order order : orders) {
        // Find the order that matches the ID and is assigned to this delivery runner
        if (order.getOrderID().equals(orderId) && 
            order.getAssignedDR().equals(deliveryRunner.getUserID())) {
            
            // Set status to indicate decline
            order.setStatus("PENDING"); // Reset to PENDING so vendor can reassign
            
            // Remove this delivery runner's assignment
            order.setAssignedDR("N/A");
            
            orderFound = true;
            System.out.println("Order " + orderId + " declined by runner " + deliveryRunner.getUserID());
            break;
        }
    }
    
    if (orderFound) {
        // Save the updated orders
        FileHandler.saveOrders(orders);
        return true;
    } else {
        System.out.println("Order " + orderId + " not found or not assigned to runner " + deliveryRunner.getUserID());
        return false;
    }
}

    // Update the Status of an Order
    public boolean updateStatus(String orderId, String newStatus) {
        if (!newStatus.equals("IN TRANSIT") && !newStatus.equals("DELIVERED") && !newStatus.equals("FAILED")) {
            JOptionPane.showMessageDialog(null, "Invalid status! Allowed: IN TRANSIT, DELIVERED, FAILED", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<Order> orders = FileHandler.loadOrders();

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getOrderID().equals(orderId) && order.getAssignedDR().equals(deliveryRunner.getUserID())) {
                order.setStatus(newStatus);
                FileHandler.saveOrders(orders);
                JOptionPane.showMessageDialog(null, "Order status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }

        JOptionPane.showMessageDialog(null, "Order not found or not assigned to you!", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Get Task History for the Delivery Runner
    public String getTaskHistory() {
        List<Order> completedOrders = deliveryRunner.getCompletedOrders(); // Fetch directly from model
        StringBuilder history = new StringBuilder("Task History:\n");

        if (completedOrders.size() == 0) {
            history.append("No completed tasks yet.");
        } else {
            for (int i = 0; i < completedOrders.size(); i++) {
                Order order = completedOrders.get(i);
                history.append("Order ID: ").append(order.getOrderID())
                        .append(", Vendor: ").append(order.getVendorID())
                        .append(", Status: ").append(order.getStatus()).append("\n");
            }
        }

        return history.toString();
    }

    // Get Customer Reviews for the Delivery Runner
    public String getCustomerReviews() {
        List<Review> allReviews = FileHandler.loadReviews();
        StringBuilder reviews = new StringBuilder("Customer Reviews:\n");

        boolean hasReviews = false;
        for (Review review : allReviews) {
            if (review.getOrderId().equals(deliveryRunner.getUserID())) {
                hasReviews = true;
                reviews.append("DeliveryRunner ID: ").append(deliveryRunner.getUserID())
                        .append("\nCustomer ID: ").append(review.getCustomerId())
                        .append("\nReview: ").append(review.getReviewText())
                        .append("\nRating: ").append(review.getRating()).append("/5\n\n");
            }
        }

        if (!hasReviews) {
            reviews.append("No reviews available.");
        }

        return reviews.toString();
    }

    // Add this method to DRController.java
    public String generateRevenueReport() {
        double totalEarnings = 0;
        StringBuilder report = new StringBuilder();

        report.append("Earnings Report for Delivery Runner: ").append(deliveryRunner.getName()).append("\n");
        report.append("---------------------------------------\n");

        // Load all orders
        List<Order> allOrders = FileHandler.loadOrders();
        List<Order> deliveredOrders = new ArrayList<>();

        // Filter orders assigned to this delivery runner and completed
        for (Order order : allOrders) {
            if (order.getAssignedDR().equals(deliveryRunner.getUserID())
                    && order.getStatus().equals("DELIVERED")) {
                deliveredOrders.add(order);
            }
        }

        // Calculate delivery fee (let's assume 2.00 per delivery)
        double deliveryFee = 2.00;

        // Generate report for each delivered order
        for (Order order : deliveredOrders) {
            totalEarnings += deliveryFee;

            report.append("Order ID: ").append(order.getOrderID())
                    .append(" | Vendor: ").append(order.getVendorID())
                    .append(" | Customer: ").append(order.getCustomerID())
                    .append(" | Earning: ").append(String.format("RM %.2f", deliveryFee)).append("\n");
        }

        report.append("---------------------------------------\n");
        report.append("Total Deliveries: ").append(deliveredOrders.size()).append("\n");
        report.append("Total Earnings: ").append(String.format("RM %.2f", totalEarnings));

        return report.toString();
    }
}
