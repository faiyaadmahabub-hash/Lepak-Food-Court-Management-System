/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public abstract class User {
   // Protected fields - accessible by child classes 
    private String userID;
    private String password;
    private String role;
    private String name;
    private String mobileNumber;
    private String address;
    
 // Constructor
    public User(String userID, String password, String role, String name, String mobileNumber, String address) {
        this.userID = userID;
        this.password = password;
        this.role = role;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }
        
     // Getters
    public String getUserID() { return userID; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getmobileNumber() { return mobileNumber; }
    public String getAddress() { return address; }
    
    // Setters
    public void setPassword(String password) { this.password = password;}
    
       // Abstract methods (Polymorphism)
    public abstract String getUserType();
}
    
    
 

    

   