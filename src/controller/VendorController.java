/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import util.FileHandler;
import java.util.List;


public class VendorController {
    
    // Load Vendors from file
    public static void loadVendors() {
        FileHandler.loadUsers();
    }
    
       // Save Vendors to file
    public static void saveVendors() {
        FileHandler.saveUsers();
    }
    
     // Vendor Logins
    public static Vendor loginVendor(String username, String password) {
        for (Vendor vendor : FileHandler.getVendors()) {
            if (vendor.getUsername().equals(username) && vendor.getPassword().equals(password)) {
                return vendor;
            }
        }
        return null;
    }
    
     // View Incoming Orders
    public static List<Order> getVendorOrders(Vendor vendor) {
        return vendor.getOrderList();
    }

    // Accept Order
    public static void acceptOrder(Vendor vendor, String orderId) {
        vendor.acceptOrder(orderId);
        saveVendors();
        FileHandler.saveOrders();
    }

    // Reject Order
    public static void rejectOrder(Vendor vendor, String orderId) {
        vendor.rejectOrder(orderId);
        saveVendors();
        FileHandler.saveOrders();
    }

    // Update Order Status
    public static void updateOrderStatus(Vendor vendor, String orderId, String newStatus) {
        vendor.updateOrderStatus(orderId, newStatus);
        saveVendors();
        FileHandler.saveOrders();
    }

    // Manage Menu Items (Add)
    public static void addMenuItem(Vendor vendor, MenuItem item) {
        vendor.addMenuItem(item);
        saveVendors();
        FileHandler.saveMenuItems();
    }

    // Manage Menu Items (Remove)
    public static void removeMenuItem(Vendor vendor, String itemId) {
        vendor.removeMenuItem(itemId);
        saveVendors();
        FileHandler.saveMenuItems();
    }

    // Generate Sales Report
    public static void generateSalesReport(Vendor vendor) {
        double totalSales = 0;
        System.out.println("Sales Report for " + vendor.getBusinessName());
        for (Order order : vendor.getOrderList()) {
            System.out.println("Order ID: " + order.getOrderId() + " - Total: RM " + order.getTotalAmount());
            totalSales += order.getTotalAmount();
        }
        System.out.println("Total Sales: RM " + totalSales);
    }
    
    
}
