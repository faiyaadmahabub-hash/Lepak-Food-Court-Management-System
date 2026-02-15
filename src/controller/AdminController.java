/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Admin;
import model.User;
import model.Customer;
import model.Vendor;
import model.Order;
import model.Transaction;
import util.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class AdminController {
    private Admin admin;
    
    public AdminController(Admin admin) {
        this.admin = admin;
    }
    
    // View all users
    public List<User> viewAllUsers() {
        return FileHandler.getAllUsers();
    }
    
    // Delete a user
    public boolean deleteUser(String userID) {
        return UserController.removeUser(userID);
    }
    
    // View all orders
    public List<Order> viewAllOrders() {
        return FileHandler.getAllOrders();
    }
    
    // Approve vendor registration
    public boolean approveVendor(Vendor vendor) {
        vendor.setApproved(true);
        return FileHandler.saveUsers();
    }
    
    // Remove a vendor
    public boolean removeVendor(String vendorUserID) {
        return UserController.removeUser(vendorUserID);
    }
    
    // Top-up customer balance
    public boolean topUpCustomerBalance(Customer customer, double amount) {
        if (amount > 0) {
            customer.addBalance(amount);
            return FileHandler.saveUsers();
        }
        return false;
    }
    
    // View transactions
    public List<Transaction> viewTransactions() {
        return FileHandler.getAllTransactions();
    }
}
