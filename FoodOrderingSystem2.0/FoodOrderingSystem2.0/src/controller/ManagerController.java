package controller;

import model.Manager;
import model.Order;
import model.DR;
import util.FileHandler;
import java.util.List;
import model.Review;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;

public class ManagerController {

    private Manager manager;

    // Constructor
    public ManagerController(Manager manager) {
        this.manager = manager;
        loadOrdersAndComplaints(); // ✅ Load data on initialization
    }

    // Load orders and complaints from file
    private void loadOrdersAndComplaints() {
        manager.getAllOrders().clear();
        manager.getAllOrders().addAll(FileHandler.loadOrders());
        manager.loadComplaints();
    }

    // Monitor Performance (Displays Order Statistics)
    public void monitorPerformance() {
        List<Order> orders = manager.getAllOrders();
        int totalOrders = orders.size();
        int deliveredOrders = (int) orders.stream().filter(o -> o.getStatus().equalsIgnoreCase("DELIVERED")).count();
        double deliveryRate = (totalOrders == 0) ? 0 : (deliveredOrders * 100.0) / totalOrders;

        String report = "Total Orders: " + totalOrders + "\n"
                + "Delivered Orders: " + deliveredOrders + "\n"
                + "Delivery Success Rate: " + String.format("%.2f", deliveryRate) + "%";

        JOptionPane.showMessageDialog(null, report, "Performance Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Resolve Complaints
    // Add this to your ManagerController.java to replace the resolveComplaints method

public void showReviewsAndResolveComplaint() {
    // Get all reviews from the system
    List<Review> allReviews = FileHandler.loadReviews();
    
    if (allReviews.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No reviews found in the system.", 
                                     "Resolve Complaints", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    // Create table model with column headers
    String[] columnNames = {"Review ID", "Customer ID", "Rating", "Review Comment"};
    Object[][] data = new Object[allReviews.size()][columnNames.length];
    
    // Populate the data array
    for (int i = 0; i < allReviews.size(); i++) {
        Review review = allReviews.get(i);
        
        // Get target info (whether it's an order or delivery runner)
        String reviewId = review.getOrderId();
        
        // For order reviews - try to get more context from the order list
        String targetInfo = reviewId;
        List<Order> orders = FileHandler.loadOrders();
        for (Order order : orders) {
            if (order.getOrderID().equals(reviewId)) {
                String itemName = "Unknown Item";
                if (order.getMenuItem() != null) {
                    itemName = order.getMenuItem().getName();
                }
                targetInfo = "Order: " + itemName + " from " + order.getVendorID();
                break;
            }
        }
        
        // For delivery runner reviews - try to get runner name
        if (targetInfo.equals(reviewId)) {
            List<DR> runners = FileHandler.loadDeliveryRunners();
            for (DR runner : runners) {
                if (runner.getUserID().equals(reviewId)) {
                    targetInfo = "Delivery Runner: " + runner.getName();
                    break;
                }
            }
        }
        
        data[i][0] = targetInfo;
        data[i][1] = review.getCustomerId();
        data[i][2] = review.getRating() + "/5";
        data[i][3] = review.getReviewText();
    }
    
    // Create and configure the table
    JTable table = new JTable(data, columnNames);
    table.setRowHeight(30);
    
    // Add selection listener
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // Create scroll pane
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(600, 300));
    
    // Create panel with title
    JPanel panel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Select Review to Resolve", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    
    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Show dialog to select a review
    int result = JOptionPane.showConfirmDialog(null, panel, "Review Selection", 
                                             JOptionPane.OK_CANCEL_OPTION, 
                                             JOptionPane.PLAIN_MESSAGE);
    
    // Process the result if OK was clicked
    if (result == JOptionPane.OK_OPTION) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Get the selected review
            Review selectedReview = allReviews.get(selectedRow);
            
            // Ask for resolution text
            String resolution = JOptionPane.showInputDialog(null, 
                                                          "Enter resolution details for this complaint:",
                                                          "Resolve Complaint",
                                                          JOptionPane.PLAIN_MESSAGE);
            
            if (resolution != null && !resolution.trim().isEmpty()) {
                // Save the resolution
                boolean success = resolveReviewComplaint(selectedReview, resolution);
                
                if (success) {
                    JOptionPane.showMessageDialog(null, "Complaint resolved successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to resolve complaint.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Resolution cannot be empty.", 
                                            "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a review to resolve.", 
                                        "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}

    public List<Review> getAllReviews() {
        // Get all reviews from the system
        List<Review> allReviews = FileHandler.loadReviews();
        return allReviews;
    }

    public boolean resolveReviewComplaint(Review review, String resolution) {
        try {
            // Create the complaints directory if it doesn't exist
            File complaintsDir = new File("data");
            if (!complaintsDir.exists()) {
                complaintsDir.mkdir();
            }

            // Create a unique complaint ID
            String complaintId = "COMP" + System.currentTimeMillis();

            // Format: ComplaintID|ReviewID|CustomerID|OriginalReview|Rating|Resolution|Status
            String complaintEntry = complaintId + "|"
                    + review.getOrderId() + "|"
                    + review.getCustomerId() + "|"
                    + review.getReviewText() + "|"
                    + review.getRating() + "|"
                    + resolution + "|"
                    + "RESOLVED" + "\n";

            // Append to complaints.txt
            FileWriter fw = new FileWriter("data/complaints.txt", true);
            fw.write(complaintEntry);
            fw.close();

            System.out.println("Complaint resolution saved: " + complaintId);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving complaint resolution: " + e.getMessage());
            return false;
        }
    }
}
