/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
import java.util.List;

public class DR extends User {
    private List<Order> assignedOrders;

    public DR(String userID, String password, String name, String mobileNumber, String address,String extraField) {
        super(userID, password, name, mobileNumber, address,"DELIVERY_RUNNER");
        this.assignedOrders = new ArrayList<>();
    }

    @Override
    public String getUserType() {
        return "DELIVERY_RUNNER";
    }
}

