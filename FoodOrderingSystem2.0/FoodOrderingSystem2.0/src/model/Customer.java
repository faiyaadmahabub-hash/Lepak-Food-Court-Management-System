package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import util.FileHandler;

public class Customer extends User {

    private double walletBalance;
    private List<Order> orderHistory;

    public Customer(String userID, String password, String name, String mobileNumber, String address, String walletBalance) {
        super(userID, password, "CUSTOMER", name, mobileNumber, address);
        this.walletBalance = Double.parseDouble(walletBalance); // Default balance
        this.orderHistory = new ArrayList<>(); // ✅ Load persisted orders
    }

    // ✅ Getter and Setter for Wallet Balance
    public double getWalletBalance() {
        return this.walletBalance;
    }

    public void setWalletBalance(double balance) {
        if (balance >= 0) {
            this.walletBalance = balance;
            FileHandler.updateUser(this);
        } else {
            System.out.println("Error: Wallet balance cannot be negative.");
        }
    }

    // ✅ Method to add balance
    public void addBalance(double amount) {
        if (amount > 0) {
            walletBalance += amount;
            FileHandler.updateUser(this);
        }
    }

    // ✅ Method to deduct balance (used for order payment)
    public boolean deductBalance(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            FileHandler.updateUser(this);
            return true;
        }
        return false;
    }

    // ✅ Order History Handling
    public void loadOrderHistory() {
        this.orderHistory = FileHandler.loadOrdersForCustomer(this.getUserID());
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
        FileHandler.saveOrders(orderHistory); // ✅ Store orders in file
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    // ✅ Submit Review with Rating Validation
    public void submitReview(String orderId, String reviewText, int rating) {
        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(null, "Invalid rating! Please enter a value between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (orderId == null || orderId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Order ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Review cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Store the review
        Review review = new Review(orderId, this.getUserID(), reviewText, rating);
        FileHandler.saveReview(review);

        JOptionPane.showMessageDialog(null, "Review submitted successfully!");
    }

    public void submitRunnerReview(String drId, String reviewText, int rating) {
        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(null, "Invalid rating! Please enter a value between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            
        }

        if (drId == null || drId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Delivery Runner ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Review cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Store the review
        Review review = new Review(drId, this.getUserID(), reviewText, rating);
        FileHandler.saveReview(review);

        JOptionPane.showMessageDialog(null, "Delivery Runner review submitted successfully!");
    }

    public void refreshWalletBalance() {
        List<User> users = FileHandler.loadUsers();
        for (User user : users) {
            if (user instanceof Customer && user.getUserID().equals(this.getUserID())) {
                this.setWalletBalance(((Customer) user).getWalletBalance()); // ✅ Update balance
                return;
            }
        }
    }
}
