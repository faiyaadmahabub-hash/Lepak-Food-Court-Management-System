/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Vendor extends User {

    private List<MenuItem> menuItems;
    private List<Order> orderList;

    // Constructor
    public Vendor(String userID) {
        super(userID, password, name, mobileNumber, address, "VENDOR");
        this.menuItems = new ArrayList<>();
        this.orderList = new ArrayList<>();
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public void removeMenuItem(String itemId) {
        menuItems.removeIf(item -> item.getItemId().equals(itemId));
    }

    public void acceptOrder(String orderId) {
        for (Order order : orderList) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(OrderStatus.CONFIRMED);
            }
        }
    }

    public void rejectOrder(String orderId) {
        orderList.removeIf(order -> order.getOrderId().equals(orderId));
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        for (Order order : orderList) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(newStatus);
            }
        }
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    private boolean approved; // Field to track vendor approval

// Getter for approval status
    public boolean isApproved() {
        return approved;
    }

// Setter for approval status
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String getUserType() {
        return "VENDOR"; //Return the user role
    }
}
