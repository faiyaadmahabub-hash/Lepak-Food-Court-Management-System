/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


public class OrderStatus {
    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PREPARING = "PREPARING"; 
    public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELED = "CANCELED";
    

    public static OrderStatus valueOf(String toUpperCase) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String status;

    public OrderStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
        } else {
            this.status = PENDING; // Default to PENDING if invalid
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
        }
    }

    private boolean isValidStatus(String status) {
        return status.equals(PENDING) || status.equals(CONFIRMED) || 
               status.equals(PREPARING) || status.equals(OUT_FOR_DELIVERY) || 
               status.equals(DELIVERED) || status.equals(CANCELED);
    }

    @Override
    public String toString() {
        return status;
    }
    
}
