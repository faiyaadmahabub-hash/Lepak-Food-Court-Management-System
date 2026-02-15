/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nurul
 */
public class OrderType {
    public static final String PICKUP = "PICKUP";
    public static final String DELIVERY = "DELIVERY";

    public static String valueOf(String toUpperCase) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String type;

    public OrderType(String type) {
        if (isValidType(type)) {
            this.type = type;
        } else {
            this.type = PICKUP; // Default to PICKUP if invalid
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (isValidType(type)) {
            this.type = type;
        }
    }

    private boolean isValidType(String type) {
        return type.equals(PICKUP) || type.equals(DELIVERY);
    }

    @Override
    public String toString() {
        return type;
    }
    
}
