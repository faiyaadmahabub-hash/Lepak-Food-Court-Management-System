/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private Customer customer;
    private Vendor vendor;
    private List<OrderItem> items;
    private String status;
    private String orderType;
    private LocalDateTime orderTime;
    private double totalAmount;
    private DeliveryInfo deliveryInfo;

    public Order(String orderId, Customer customer, Vendor vendor, String orderType) {
        this.orderId = orderId;
        this.customer = customer;
        this.vendor = vendor;
        this.items = new ArrayList<>();
        this.status = "PENDING";
        this.orderType = orderType.toUpperCase();
        this.orderTime = LocalDateTime.now();
        this.totalAmount = 0.0;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType.trim().toUpperCase(); // ✅ Store as String
    }

   
    public void setStatus(String status) {
        this.status = status.toUpperCase(); // ✅ Store as String
    }

    // Add item to order
    public void addItem(MenuItem menuItem, int quantity) {
        OrderItem orderItem = new OrderItem(menuItem, quantity);
        items.add(orderItem);
        calculateTotal();
    }

    // Calculate total amount
    private void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        // Add delivery charge if applicable
       if (orderType.equals("DELIVERY") && deliveryInfo != null) {
            this.totalAmount += deliveryInfo.getDeliveryCharge();
        }
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getStatus() {
        return status;
    }

    public String getOrderType() {
        return orderType;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
        calculateTotal();
    }
}
