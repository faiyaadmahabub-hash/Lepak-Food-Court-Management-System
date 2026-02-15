/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class User {
    private String userID;
    private String password;
    private String name;
    private String mobileNumber;
    private String role;
    private String address;

    public User(String userID, String password, String role, String name, String mobileNumber, String address) {
        this.userID = userID;
        this.password = password;
        this.role = role;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmobileNumber() {
        return mobileNumber;
    }

    public void setmobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() { 
        return address;
    }

    public void setAddress(String address) { 
        this.address = address;
    }
    
  
    
    
}


    
    
 

    

   