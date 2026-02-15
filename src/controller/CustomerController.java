/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.*;
import util.FileHandler;
import java.util.List;

public class CustomerController {
    
// View Menu 
    public static List<MenuItem> ViewMenu(){
        return FileHandler.getMenuItems(); // Returns the list of menu items
    }
    
   // Track Order Status
    public static String trackOrderStatus(String orderId) {
        for (Order order : FileHandler.getAllOrders()) {
            if (order.getOrderId().equals(orderId)) {
                return "Order ID: " + order.getOrderId() + " - Status: " + order.getStatus();
            }
        }
        return "Order not found.";
    }
    
     // View Order History
 public static List<Order> getOrderHistory(Customer customer) {
    List<Order> orderHistory = new ArrayList<>();
    for (Order order : FileHandler.getAllOrders()) {
        if (order.getCustomer().getUserID().equals(customer.getUserID())) {
            orderHistory.add(order);  // Manually adding to the list
        }
    }
    return orderHistory;  // Return the manually filled list
}
    
     

}
