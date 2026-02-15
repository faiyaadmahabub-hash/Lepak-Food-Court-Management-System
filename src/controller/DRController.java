/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.*;
import util.FileHandler;
import java.util.List;

public class DRController {

    // Load DRs from file
    public static void loadDRs() {
        FileHandler.loadUsers(); // DRs are stored in users.txt
    }

    // Save DRs to file
    public static void saveDRs() {
        FileHandler.saveUsers();
    }

    // DR Login
    public static DR loginDR(String userID, String password) {
        for (DR dr : FileHandler.getDRs) { // 
            if (dr.getUsername().equals(userID) && dr.getPassword().equals(password)) {
                return dr;
            }
        }
        return null;
    }

    // View Assigned Orders
    public static List<Order> getAssignedOrders(DR dr) {
        return dr.getAssignedOrders();
    }

    // Update Order Status (Out for Delivery / Delivered)
    public static void updateOrderStatus(DR dr, String orderId, String newStatus) {
        // Validate status before updating
        if (!newStatus.equals(OrderStatus.OUT_FOR_DELIVERY) && !newStatus.equals(OrderStatus.DELIVERED)) {
            System.out.println("Invalid status update. Allowed statuses: OUT_FOR_DELIVERY, DELIVERED.");
            return;
        }

        // If valid, update the order status
        dr.updateOrderStatus(orderId, newStatus);
        saveDRs();
        FileHandler.saveOrders();
    }

    public static List<Order> getDeliveryHistory(DR dr) {
        List<Order> history = new ArrayList<>();
        for (Order order : dr.getAssignedOrders()) {
            if (order.getStatus().equals("DELIVERED") || order.getStatus().equals("delivered")) {
                history.add(order);
            }
        }
        return history;
    }
}
