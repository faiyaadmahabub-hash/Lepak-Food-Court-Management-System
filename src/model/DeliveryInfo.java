/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


public class DeliveryInfo {
    private String deliveryAddress;
    private String deliveryTime;
    private double deliveryCharge;

    public DeliveryInfo(String deliveryAddress, String deliveryTime, double deliveryCharge) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryTime = deliveryTime;
        this.deliveryCharge = deliveryCharge;
    }

    public String getDeliveryAddress() { return deliveryAddress; }
    public String getDeliveryTime() { return deliveryTime; }
    public double getDeliveryCharge() { return deliveryCharge; }

    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }
    public void setDeliveryCharge(double deliveryCharge) { this.deliveryCharge = deliveryCharge; }
    
}
