package controller;

import model.User;
import model.Customer;
import model.Vendor;
import model.DR;
import model.Manager;
import util.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class UserController {

    // Retrieve all users from FileHandler
    public static List<User> getAllUsers() {
        return FileHandler.getAllUsers();
    }

    // Authenticate user login
    public static User authenticate(String userID, String password) {
        for (User user : getAllUsers()) {
            if (user.getUserID().equalsIgnoreCase(userID) && user.getPassword().equals(password)) {
                return user; // ✅ Correct user found
            }
        }
        return null; // ❌ Invalid login
    }

    // Add a new user (Ensures unique username)
    public static boolean addUser(User user) {
        List<User> users = getAllUsers();

        // Check if username already exists
        for (User u : users) {
            if (u.getUserID().equalsIgnoreCase(user.getUserID())) {
                return false; // ❌ Username already exists
            }
        }

        users.add(user);
        return FileHandler.saveUsers(); // ✅ Save updated list
    }

    // Remove a user by username
    public static boolean removeUser(String username) {
        List<User> users = getAllUsers();
        User userToRemove = null;

        for (User user : users) {
            if (user.getUserID().equalsIgnoreCase(userID)) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            users.remove(userToRemove);
            return FileHandler.saveUsers(); // ✅ FIXED: Call method without arguments
        }
        return false; // ❌ User not found
    }

    // Find user by username
    public static User findUserByUsername(String userID) {
        for (User user : getAllUsers()) {
            if (user.getUserID().equalsIgnoreCase(userID)) {
                return user;
            }
        }
        return null; // ❌ User not found
    }

    // Retrieve all customers
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    public static User validateLogin(String username, String password) {
        FileHandler.loadUsers(); // ✅ Ensure latest users are loaded

        System.out.println("Debug: Checking login for " + userID); // Debug message

        for (User user : FileHandler.getAllUsers()) {
            System.out.println("Debug: Checking " + user.getUserID()); // Debug message
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                return user; // ✅ User found
            }
        }
        return null; // ❌ Invalid credentials
    }

    // Retrieve all vendors
    public static List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (user instanceof Vendor) {
                vendors.add((Vendor) user);
            }
        }
        return vendors;
    }

    // Register a new user based on role
   public static boolean registerUser(String userID, String password, String role, 
                                   String name, String mobileNumber, String address, 
                                   String securityQuestion, String securityAnswer, String extraField) {
    List<User> users = FileHandler.getAllUsers();

        // ✅ Fix role checking with correct names
         User newUser = null;
    switch (role.toLowerCase()) {
        case "customer":
            newUser = new Customer(userID, password, name, mobileNumber, address, securityQuestion, securityAnswer, 0.0);
            break;
        case "vendor":
            newUser = new Vendor(userID);
            break;
        case "delivery runner":
            newUser = new DR(userID, password, name, mobileNumber, address, securityQuestion, securityAnswer, extraField); // Vehicle type
            break;
        case "manager":
            newUser = new Manager(userID, password, name, mobileNumber, address, securityQuestion, securityAnswer, extraField); // Department
            break;
        default:
            System.out.println("Error: Invalid role specified! Role provided: " + role);
            return false;
    }

        users.add(newUser);
        boolean isSaved = FileHandler.saveUsers();

        if (isSaved) {
            System.out.println("Debug: User " + userID + " registered successfully.");
        } else {
            System.out.println("Error: User registration failed.");
        }

        return isSaved;
    }
}
