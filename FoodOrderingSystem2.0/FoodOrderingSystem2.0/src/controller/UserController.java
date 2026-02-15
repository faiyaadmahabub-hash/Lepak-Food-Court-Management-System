package controller;

import model.*;
import util.FileHandler;
import java.util.List;

public class UserController {

    // Authenticate User Login
    public static User loginUser(String userID, String password) {
        List<User> users = FileHandler.loadUsers();

        for (User user : users) {
            System.out.println("Checking User: " + user.getUserID() + " | Password: " + user.getPassword()); // Debugging output
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {  // Strict comparison
                return user; // Successfully authenticated
            }
        }
        System.out.println("Login Failed: User not found or incorrect password."); // Debugging output
        return null; // Authentication failed
    }

    // Register User
    public static boolean registerUser(String userID, String password, String name, String mobileNumber, String address, String role) {
        if (FileHandler.usernameExists(userID)) {
            return false; // Check if user already exists
        }
        User newUser;
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                newUser = new Customer(userID, password, name, mobileNumber, address, "0.0" );
                break;
            case "VENDOR":
                newUser = new Vendor(userID, password, name, mobileNumber, address, "Default Business");
                break;
            case "DELIVERY RUNNER":
                newUser = new DR(userID, password, name, mobileNumber, address);
                break;
            case "MANAGER":
                newUser = new Manager(userID, password, name, mobileNumber, address);
                break;
            case "ADMIN":
                newUser = new Admin(userID, password, name, mobileNumber, address);
                break;
            default:
                return false; // Invalid role, return false
        }

        return FileHandler.saveUser(newUser); // Save new user using OOP principle
    }
}
