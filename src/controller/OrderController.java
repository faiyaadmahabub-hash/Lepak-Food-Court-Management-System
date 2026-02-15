/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import java.util.List;
import java.util.stream.Collectors;
import util.FileHandler;


public class OrderController {

    // Load orders from file
    public static void loadOrders() {
        FileHandler.loadOrders();
    }

    // Save orders to file
    public static void saveOrders() {
        FileHandler.saveOrders();
    }

// Place an order and save it to file
    public static boolean placeOrder(Customer customer, Vendor vendor, MenuItem item, int quantity, String orderType) {
        Order newOrder = new Order("ORD" + (FileHandler.getAllOrders().size() + 1), customer, vendor, OrderType.valueOf(orderType.toUpperCase()));
        newOrder.addItem(item, quantity);
        FileHandler.getAllOrders().add(newOrder);
        saveOrders();
        return true;
    }

    // Update Order Type and Save
    public static boolean updateOrderType(String orderId, String newOrderType) {
        for (Order order : FileHandler.getAllOrders()) {
            if (order.getOrderId().equals(orderId)) {
                order.setOrderType(newOrderType);
                saveOrders(); // 
                return true;
            }
        }
        return false;
    }

    // Cancel Order
    public static boolean cancelOrder(String orderId, Customer customer) {
        for (Order order : FileHandler.getAllOrders()) {
            if (order.getOrderId().equals(orderId) && order.getCustomer().getUserID().equals(customer.getUserID())) {
                if (order.getStatus().equals(OrderStatus.PENDING)) {
                    FileHandler.getAllOrders().remove(order);
                    customer.addBalance(order.getTotalAmount()); // Refund balance
                    FileHandler.saveOrders();
                    FileHandler.saveUsers();
                    return true;
                }
                return false; // Cannot cancel non-pending orders
            }
        }
        return false; // Order not found
    }

    // Retrieve orders for a customer
    public static List<Order> getCustomerOrders(String userID) {
        List<Order> allOrders = FileHandler.getAllOrders();
        return allOrders.stream()
                .filter(order -> order.getCustomer().getUserID().equals(userID))
                .collect(Collectors.toList());
    }

    public static void createOrder(List<OrderItem> cart) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static Order getOrderDetails(String orderId) {
        List<Order> allOrders = FileHandler.getAllOrders();
        return allOrders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
}
