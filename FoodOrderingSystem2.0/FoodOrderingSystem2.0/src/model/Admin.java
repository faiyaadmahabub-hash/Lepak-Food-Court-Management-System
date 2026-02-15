/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Admin extends User {

    // Constructor
    public Admin(String userID, String password, String name, String mobileNumber, String address) {
        super(userID, password, "ADMIN", name, mobileNumber, address);
    }

    // abstract method from User class
    public String getUserType() {
        return "Admin";
    }
}
