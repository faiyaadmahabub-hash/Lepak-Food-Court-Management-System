/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import util.FileHandler;
import controller.UserController;
import controller.OrderController;

public class Admin extends User {

    public Admin(String userID, String password, String name, String mobileNumber, String address) {
        super(userID, password, "ADMIN", name, mobileNumber, address, "", "");
    }

    // View All Users
    public List<User> viewAllUsers() {
        return UserController.getAllUsers();
    }

    // Delete User
    public boolean deleteUser(String userID) {
        User user = UserController.findUserByUsername(userID);
        if (user != null) {
            return UserController.removeUser(userID);
        }
        return false; // User not found
    }

    // View All Orders
    public List<Order> viewAllOrders() {
        return OrderController.getAllOrders();
    }

    // Approve Vendor Registration
    public boolean approveVendor(Vendor vendor) {
        vendor.setApprovalStatus(true);
        FileHandler.saveUsers();
        return true;
    }

    // Remove Vendor
    public boolean removeVendor(String vendorUserID) {
        return UserController.removeUser(vendorUserID);
    }

    // Top-up Customer Wallet
    public boolean topUpCustomerBalance(Customer customer, double amount) {
        if (amount <= 0) {
            return false; // Invalid amount
        }
        customer.addBalance(amount);
        FileHandler.saveUsers();
        return true;
    }

    // View Transactions
    public List<Transaction> viewTransactions() {
        return FileHandler.getAllTransactions();
    }

    @Override
    public String getUserType() {
        return "ADMIN";
    }
}
