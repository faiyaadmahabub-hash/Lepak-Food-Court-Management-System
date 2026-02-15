/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


 
public class FileHandler {

    private static final String USER_FILE = "data/users.txt";
    private static final String MENU_ITEMS_FILE = "data/menu_items.txt";
    private static final String MENU_FILE = "data/menu.txt";
    private static final String ORDER_FILE = "data/orders.txt";
    private static final String TRANSACTIONS_FILE = "data/transactions.txt";

    private static List<User> users = new ArrayList<>();
    private static List<MenuItem> menuItems = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Transaction> transactions = new ArrayList<>();

    // ✅ Load users from file before searching
public static void loadUsers() {
    users.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split("\\|");

            if (data.length >= 8) {
                String role = data[2].trim();
                switch (role.toLowerCase()) {
                    case "customer":
                        users.add(new Customer(data[0], data[1], data[3], data[4], data[5], data[6], data[7], Double.parseDouble(data[8])));
                        break;
                    case "vendor":
                        users.add(new Vendor(data[0]));
                        break;
                    case "delivery_runner":
                        users.add(new DR(data[0], data[1], data[3], data[4], data[5], data[6], data[7], extraField)); // ✅ Fix DR loading
                        break;
                    case "manager":
                        users.add(new Manager(data[0], data[1], data[3], data[4], data[5], data[6], data[7], extraField)); // ✅ Fix Manager loading
                        break;
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading users: " + e.getMessage());
    }
}  


    // ✅ Ensure users are saved correctly
    public static boolean saveUsers() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) { // ✅ Append mode
        for (User user : users) {
            writer.write(user.getUsername() + "|" + user.getPassword() + "|" + user.getRole() + "|"
                    + user.getName() + "|" + user.getmobileNumber() + "|" + user.getAddress() + "|");

            // ✅ Handle additional fields for DR & Manager
            if (user instanceof DR) {
                writer.write("|" + ((DR) user).getUserType()); // ✅ Append extra field for DR
            } else if (user instanceof Manager) {
                writer.write("|" + ((Manager) user).getUserType()); // ✅ Append extra field for Manager
            }
            writer.write("\n");
        }
        return true;
    } catch (IOException e) {
        System.out.println("Error saving users: " + e.getMessage());
        return false;
    }
}


    // ✅ Register a new user with file check
    public static boolean registerUser(User newUser) {
    loadUsers(); // Ensure latest users are loaded

    // Check if username exists
    for (User user : users) {
        if (user.getUsername().equalsIgnoreCase(newUser.getUsername())) {
            System.out.println("Debug: Username already exists -> " + newUser.);
            return false;
        }
    }

    users.add(newUser);
    return saveUsers(); // ✅ Append new user instead of overwriting
}


    // ✅ Ensure `loadUsers()` is called before retrieving users
    public static List<User> getAllUsers() {
        loadUsers();
        return users;
    }

    // ✅ Fix login validation to ensure users are loaded
    public static User validateLogin(String userID, String password) {
        loadUsers();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(userID) && user.getPassword().equals(password)) {
                return user; // ✅ Correct user found
            }
        }
        return null; // ❌ Invalid login
    }

    // Load menu items from file
    public static void loadMenuItems() {
        menuItems.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_ITEMS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 3) {
                    String itemId = data[0];
                    String itemName = data[1];
                    double price = Double.parseDouble(data[2]);
                    MenuItem item = new MenuItem(itemId, itemName, price);
                    menuItems.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menu items.");
        }
    }

    // Save menu items to file
    public static void saveMenuItems() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_ITEMS_FILE))) {
            for (MenuItem item : menuItems) {
                writer.write(item.getItemId() + "|" + item.getName() + "|" + item.getPrice() + "|" + item.getDescription() + "|" + item.getCategory() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving menu items.");
        }
    }

    public static List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 2) {
                    String name = data[0];
                    double price = Double.parseDouble(data[1]);
                    menuItems.add(new MenuItem(name, price));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menu: " + e.getMessage());
        }
        return menuItems;
    }

    // Load orders from file
    public static void loadOrders() {
        orders.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 6) {
                    String orderId = data[0];
                    String customerUserID = data[1];
                    String vendorUserID = data[2];
                    String orderStatus = data[3].toUpperCase();
                    String orderType = data[4].toUpperCase();

                    Customer customer = Order.getCustomer();
                    Vendor vendor = getVendorByUsername(vendorUserID);

                    if (customer != null && vendor != null) {
                        Order order = new Order(orderId, customer, vendor, orderType);
                        order.setStatus(orderStatus); // ✅ Set String status

                        for (int i = 5; i < data.length; i += 3) {
                            String menuItemId = data[i];
                            String itemName = data[i + 1];
                            double price = Double.parseDouble(data[i + 2]);
                            MenuItem menuItem = new MenuItem(menuItemId, itemName, price);
                            order.addItem(menuItem, 1);
                        }
                        orders.add(order);
                    } else {
                        System.out.println("Warning: Customer or Vendor not found for Order ID: " + orderId);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading orders.");
        }
    }

    // Save orders to file
    public static void saveOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orders) {
                writer.write(order.getOrderId() + "|"
                        + order.getCustomer().getUserID() + "|"
                        + order.getVendor().getUserID() + "|"
                        + order.getStatus() + "|"
                        + order.getOrderType());

                for (OrderItem item : order.getItems()) {
                    writer.write("|" + item.getMenuItem().getItemId() + "|"
                            + item.getMenuItem().getName() + "|"
                            + item.getMenuItem().getPrice());
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving orders.");
        }
    }

    public static List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 4) {
                    Customer customer = getCustomerByUserID(data[1]);
                    Vendor vendor = getVendorByUserID(data[2]);

                    if (customer != null && vendor != null) {
                        String orderType = data[3].trim();
                        Order order = new Order(data[0], customer, vendor, orderType);
                        orderList.add(order);
                    } else {
                        System.out.println("Warning: Customer or Vendor not found for Order ID: " + data[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading orders.");
        }
        return orderList;
    }

    public static Customer getCustomerByUserID(String username) {
        for (User user : users) {
            if (user instanceof Customer && user.getUserID().equals(userID)) {
                return (Customer) user;
            }
        }
        return null;
    }

    public static Vendor getVendorByUserID(String userID) {
        for (User user : users) {
            if (user instanceof Vendor && user.getUserID().equals(userID)) {
                return (Vendor) user;
            }
        }
        return null;
    }

    // Load transactions from file
    public static void loadTransactions() {
        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 4) {
                    transactions.add(new Transaction(data[0], data[1], Double.parseDouble(data[2]), data[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing transactions found.");
        }
    }

    // Save transactions to file
    public static void saveTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.getTransactionId() + "|" + transaction.getUserID() + "|" + transaction.getAmount() + "|" + transaction.getTransactionType() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
        }
    }

}
