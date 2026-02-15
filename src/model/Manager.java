/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.ArrayList;

public class Manager extends User {
    private List<Order> allOrders;

    public Manager(String userID, String password, String name, String mobileNumber, String address, String securityQuestion, String securityAnswer, String extraField) {
        super(userID, password, name, mobileNumber, address,"MANAGER");
        this.allOrders = new ArrayList<>();
    }

    @Override
    public String getUserType() {
        return "MANAGER";
    }
}
