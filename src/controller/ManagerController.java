/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import util.FileHandler;
import java.util.List;

public class ManagerController {

    // Load Managers from file
    public static void loadManagers() {
        FileHandler.loadUsers(); // Managers are stored in users.txt
    }

    // Save Managers to file
    public static void saveManagers() {
        FileHandler.saveUsers();
    }

    // Manager Login
    public static Manager loginManager(String userID, String password) {
        for (User user : FileHandler.getAllUsers()) {
            if (user.getRole().equals("MANAGER")
                    && user.getUserID.equals(userID)
                    && user.getPassword().equals(password)) {
                return (Manager) user;
            }
        }
        return null;
    }

    // View All Orders
    public static List<Order> getAllOrders() {
        return FileHandler.getAllOrders(); // Load all orders from file
    }

    // Activate/Deactivate a User (Vendor or DR)
    public static void changeUserStatus(String username, boolean isActive) {
        for (User user : FileHandler.getAllUsers()) {
            if (user.getUsername().equals(username)) {
                user.setActive(isActive);
                saveManagers();
                return;
            }
        }
    }

    // Generate Sales Report
    public static void generateSalesReport() {
        double totalSales = 0;
        System.out.println("Business Sales Report:");
        for (Order order : getAllOrders()) {
            System.out.println("Order ID: " + order.getOrderId() + " - Total: RM " + order.getTotalAmount());
            totalSales += order.getTotalAmount();
        }
        System.out.println("Total Sales: RM " + totalSales);
    }
}
