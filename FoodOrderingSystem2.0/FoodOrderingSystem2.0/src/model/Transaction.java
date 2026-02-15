/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

public class Transaction {
     private String transactionId;
    private String username;
    private double amount;
    private String transactionType; // "Top-up" or "Order Payment"
    private LocalDateTime transactionDate;
    
    // Constructor
    public Transaction(String transactionId, String username, double amount, String transactionType) {
        this.transactionId = transactionId;
        this.username = username;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = LocalDateTime.now();
    }
    
      // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    // Method to display transaction details
    public void displayTransaction() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("User: " + username);
        System.out.println("Amount: $" + amount);
        System.out.println("Type: " + transactionType);
        System.out.println("Date: " + transactionDate);
    }
    
}
