/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Order;
import util.FileHandler;
import java.util.List;
import java.util.stream.Collectors;

public class OrderController {
    private List<Order> orders;

    public OrderController() {
        this.orders = FileHandler.loadOrders();
    }

    public boolean placeOrder(Order newOrder) {
        orders.add(newOrder);
        FileHandler.saveOrders(orders);
        return true;
    }

    public boolean cancelOrder(String orderId, String customerId) {
        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && 
                order.getCustomerID().equals(customerId) && 
                order.getStatus().equalsIgnoreCase("PENDING")) {
                
                order.setStatus("CANCELLED");
                FileHandler.saveOrders(orders);
                return true;
            }
        }
        return false;
    }

    public List<Order> getCustomerOrders(String customerId) {
        return orders.stream()
                     .filter(order -> order.getCustomerID().equals(customerId))
                     .collect(Collectors.toList());
    }
}