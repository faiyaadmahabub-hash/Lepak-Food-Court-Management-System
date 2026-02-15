/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nurul
 */
public class Review {
    private String orderId;
    private String customerId;
    private String reviewText;
    private int rating;

    // ✅ Constructor
    public Review(String orderId, String customerId, String reviewText, int rating) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // ✅ Getters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getReviewText() { return reviewText; }
    public int getRating() { return rating; }
}