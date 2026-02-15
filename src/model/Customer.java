/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;


public class Customer extends User {

    private double walletBalance;
    private List<Order> orderHistory;

    public Customer(String userID, String password, String name, String mobileNumber, String address, double walletBalance) {
        super(userID, password, "Customer", name, mobileNumber, address);
        this.walletBalance = walletBalance;
        this.orderHistory = new ArrayList<>();
    }

    // Getter method (Encapsulation)
    public double getWalletBalance() {
        return walletBalance;
    }

    // Method to add balance
    public void addBalance(double amount) {
        if (amount > 0) {
            walletBalance += amount;
            System.out.println("Successfully added $" + amount + " to wallet.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    // Method to deduct balance (used for order payment)
    public boolean deductBalance(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            System.out.println("Payment of $" + amount + " successful.");
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    // Overriding abstract method from User
    @Override
    public String getUserType() {
        return "Customer";
    }

}
    