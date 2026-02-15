package util;

import model.*;
import java.io.*;
import java.util.*;

public class FileHandler {

    private static final String USER_FILE = "data/users.txt";
    private static final String MENU_FILE = "data/menu_items.txt";
    private static final String ORDER_FILE = "data/orders.txt";
    private static final String REVIEW_FILE = "data/reviews.txt";

    // Load all users (fixed to correctly load vendors)
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length >= 6) { // Ensure there are enough data fields
                    String userID = data[0];
                    String password = data[1];
                    String name = data[2];
                    String mobileNumber = data[3];
                    String role = data[4];
                    String address = data[5];

                    if (role.equals("CUSTOMER") && data.length == 7) {
                        String walletBalance = data[6];
                        users.add(new Customer(userID, password, name, mobileNumber, address, walletBalance));
                    } else if (role.equals("VENDOR")) {
                        users.add(new Vendor(userID, password, name, mobileNumber, address, "Default Business"));
                    } else if (role.equals("DELIVERY RUNNER")) {
                        users.add(new DR(userID, password, name, mobileNumber, address));
                    } else if (role.equals("ADMIN")) {
                        users.add(new Admin(userID, password, name, mobileNumber, address));
                    } else if (role.equals("MANAGER")){
                        users.add(new Manager(userID, password, name, mobileNumber, address));
                    }
                    
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // Load All Vendors
    public static List<Vendor> loadVendors() {
        List<Vendor> vendors = new ArrayList<>();
        for (User user : loadUsers()) {
            if (user.getRole().equals("VENDOR")) {
                vendors.add((Vendor) user);
            }
        }
        return vendors;
    }

    // Save vendors
    public static void saveVendors(List<Vendor> vendors) {
        List<User> users = loadUsers();
        users.removeIf(user -> user.getRole().equals("VENDOR"));
        users.addAll(vendors);
        saveUsers(users);
    }

    //  Load Delivery Runners
    public static List<DR> loadDeliveryRunners() {
        List<DR> runners = new ArrayList<>();
        List<User> users = loadUsers(); // ✅ Load Users Once

        for (User user : users) {
            if (user.getRole().equals("DELIVERY RUNNER")) {
                runners.add((DR) user);
            }
        }
        return runners;
    }

    // Save users with vendor support
    public static boolean saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                writer.write(user.getUserID() + "|" + user.getPassword() + "|" + user.getName() + "|"
                        + user.getmobileNumber() + "|" + user.getRole() + "|" + user.getAddress());

                if (user instanceof Customer) {
                    writer.write("|" + ((Customer) user).getWalletBalance());
                }
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
            return false;
        }
    }

    public static void updateUser(User updatedUser) {
        List<User> users = loadUsers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                if (user.getUserID().equals(updatedUser.getUserID())) {
                    writer.write(updatedUser.getUserID() + "|" + updatedUser.getPassword() + "|"
                            + updatedUser.getName() + "|" + updatedUser.getmobileNumber() + "|"
                            + updatedUser.getRole() + "|" + updatedUser.getAddress());

                    if (updatedUser instanceof Customer) {
                        writer.write("|" + ((Customer) updatedUser).getWalletBalance());
                    }
                } else {
                    writer.write(user.getUserID() + "|" + user.getPassword() + "|"
                            + user.getName() + "|" + user.getmobileNumber() + "|"
                            + user.getRole() + "|" + user.getAddress());

                    if (user instanceof Customer) {
                        writer.write("|" + ((Customer) user).getWalletBalance());
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public static void deleteUser(String userID) {
        List<User> users = loadUsers();
        users.removeIf(user -> user.getUserID().equals(userID));
        saveUsers(users);
    }

    public static boolean saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) { // Append mode
            writer.write(user.getUserID() + "|" + user.getPassword() + "|" + user.getName() + "|"
                    + user.getmobileNumber() + "|" + user.getRole() + "|" + user.getAddress());

            if (user instanceof Customer) {
                writer.write("|" + ((Customer) user).getWalletBalance());
            }
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    public static boolean usernameExists(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUserID().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Load all vendors
    // Load menu items (with vendor assignment)
    public static List<MenuItem> loadMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        List<Vendor> vendors = loadVendors(); // ✅ Load Vendors Once

        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String itemId = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String category = parts[3];
                    boolean available = Boolean.parseBoolean(parts[4]);
                    String vendorId = parts[5];

                    Vendor vendor = null;
                    for (Vendor v : vendors) {
                        if (v.getUserID().equals(vendorId)) {
                            vendor = v;
                            break;
                        }
                    }

                    menuItems.add(new MenuItem(itemId, name, price, category, available, vendor));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menu items: " + e.getMessage());
        }
        return menuItems;
    }

    // Load Menu Items by Vendor
    public static List<MenuItem> loadMenuItemsByVendor(String vendorID) {
        List<MenuItem> vendorMenu = new ArrayList<>();
        for (MenuItem item : loadMenuItems()) {
            if (item.getVendor() != null && item.getVendor().getUserID().equals(vendorID)) {
                vendorMenu.add(item);
            }
        }
        return vendorMenu;
    }

    // Save menu items
    public static void saveMenuItems(List<MenuItem> menuItems) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (MenuItem item : menuItems) {
                String vendorId = (item.getVendor() != null) ? item.getVendor().getUserID() : "UNKNOWN"; // ✅ Fix null vendors
                writer.write(item.getItemId() + "|" + item.getName() + "|" + item.getPrice() + "|"
                        + item.getCategory() + "|" + item.isAvailable() + "|" + vendorId);
                writer.newLine();
            }
            System.out.println("✅ Menu items saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving menu items: " + e.getMessage());
        }
    }

    // Load orders 
    // Update the loadOrders method to handle the additional delivery fields
    public static List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();
        List<MenuItem> menuItems = loadMenuItems(); // Load Once

        File file = new File(ORDER_FILE);
        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                // Basic order data requires at least 7 fields, with optional delivery data
                if (data.length >= 7) {
                    String orderId = data[0];
                    String vendorId = data[1];
                    String customerId = data[2];
                    String menuItemId = data[3];
                    int quantity = Integer.parseInt(data[4]);
                    String status = data[5];
                    String assignedDR = data[6];

                    MenuItem selectedItem = null;
                    for (MenuItem item : menuItems) {
                        if (item.getItemId().equals(menuItemId)) {
                            selectedItem = item;
                            break;
                        }
                    }

                    // If menu item is null, use a fallback to prevent errors
                    if (selectedItem == null) {
                        System.out.println("Warning: Menu item " + menuItemId + " not found for order " + orderId);
                        // Try to find any menu item as a fallback
                        if (!menuItems.isEmpty()) {
                            selectedItem = menuItems.get(0);
                        } else {
                            // Skip this order if no menu items exist at all
                            System.out.println("Error: No menu items available, skipping order " + orderId);
                            continue;
                        }
                    }

                    Order order = new Order(orderId, vendorId, customerId, assignedDR, selectedItem, quantity, status);

                    // Add delivery information if available (fields 8 and 9)
                    if (data.length >= 9) {
                        order.setDeliveryType(data[7]);
                        order.setDeliveryFee(Double.parseDouble(data[8]));
                    } else {
                        // Default values if not specified
                        order.setDeliveryType("DINE_IN");
                        order.setDeliveryFee(0.0);
                    }

                    orders.add(order);
                    System.out.println("Loaded order: " + orderId + ", status: " + status + ", assignedDR: " + assignedDR);
                } else {
                    System.out.println("Warning: Invalid order data: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
        }

        return orders;
    }

    // ✅ Load Orders by Vendor
    public static List<Order> loadOrdersByVendor(String vendorID) {
        List<Order> vendorOrders = new ArrayList<>();
        for (Order order : loadOrders()) {
            if (order.getVendorID().equals(vendorID)) {
                vendorOrders.add(order);
            }
        }
        return vendorOrders;
    }

    // Save orders
    // Fix for the saveOrders method in FileHandler.java
    public static void saveOrders(List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orders) {
                // Get the current status
                String status = order.getStatus();

                // Get assignedDR and make sure it's preserved exactly as is for DINE_IN
                String assignedDR = order.getAssignedDR();

                // Add debug output to help troubleshoot
                System.out.println("Saving order: " + order.getOrderID()
                        + " | Status: " + status
                        + " | AssignedDR: " + assignedDR
                        + " | DeliveryType: " + order.getDeliveryType());

                // Write the order data
                // Make sure we use the assignedDR variable, not order.getAssignedDR() again
                writer.write(order.getOrderID() + "|"
                        + order.getVendorID() + "|"
                        + order.getCustomerID() + "|"
                        + order.getMenuItem().getItemId() + "|"
                        + order.getQuantity() + "|"
                        + status + "|"
                        + // Only use N/A if assignedDR is actually empty or null
                        (assignedDR == null || assignedDR.isEmpty() ? "N/A" : assignedDR) + "|"
                        + order.getDeliveryType() + "|"
                        + order.getDeliveryFee());

                writer.newLine();
            }
            System.out.println("All orders saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    public static List<Order> getOrdersForDeliveryRunner(String drId) {
        List<Order> assignedOrders = new ArrayList<>();
        for (Order order : loadOrders()) {
            if (order.getAssignedDR().equals(drId)) {
                assignedOrders.add(order);
            }
        }
        return assignedOrders;
    }

    public static List<Order> loadOrdersForCustomer(String customerId) {
        List<Order> allOrders = loadOrders(); // ✅ Ensure this is only called once
        List<Order> customerOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomerID().equals(customerId)) {
                customerOrders.add(order);
            }
        }

        return customerOrders;
    }

    // In FileHandler.java, modify the saveReview method to overwrite existing delivery runner reviews
    public static void saveReview(Review newReview) {
        // First, check if this is a delivery runner review (not an order review)
        boolean isDeliveryRunnerReview = false;

        // Try to find if the review ID matches a delivery runner ID rather than an order ID
        for (User user : loadUsers()) {
            if (user instanceof DR && user.getUserID().equals(newReview.getOrderId())) {
                isDeliveryRunnerReview = true;
                break;
            }
        }

        if (isDeliveryRunnerReview) {
            // For delivery runner reviews, we need to check for existing reviews from this customer
            List<Review> existingReviews = loadReviews();
            List<Review> updatedReviews = new ArrayList<>();
            boolean reviewUpdated = false;

            // Filter out any existing reviews from this customer for this delivery runner
            for (Review review : existingReviews) {
                // Skip reviews that match both the delivery runner ID and customer ID
                if (review.getOrderId().equals(newReview.getOrderId())
                        && review.getCustomerId().equals(newReview.getCustomerId())) {
                    reviewUpdated = true;
                    // We'll add the new review later
                } else {
                    updatedReviews.add(review);
                }
            }

            // Add the new review
            updatedReviews.add(newReview);

            // Save all reviews back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE))) {
                for (Review review : updatedReviews) {
                    writer.write(review.getOrderId() + "|" + review.getCustomerId() + "|"
                            + review.getReviewText() + "|" + review.getRating());
                    writer.newLine();
                }
                System.out.println(reviewUpdated
                        ? "Delivery runner review updated successfully."
                        : "New delivery runner review added.");
            } catch (IOException e) {
                System.out.println("Error saving reviews: " + e.getMessage());
            }
        } else {
            // For regular order reviews, just append as before
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE, true))) {
                writer.write(newReview.getOrderId() + "|" + newReview.getCustomerId() + "|"
                        + newReview.getReviewText() + "|" + newReview.getRating());
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error saving review: " + e.getMessage());
            }
        }
    }

    public static List<Review> loadReviews() {
        List<Review> reviews = new ArrayList<>();
        File file = new File("data/reviews.txt");

        if (!file.exists()) {
            return reviews;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 4) {
                    reviews.add(new Review(data[0], data[1], data[2], Integer.parseInt(data[3])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
        }
        return reviews;
    }

    //  Load Reviews by Vendor
    // load Reviews for a Specific Vendor
    public static List<String> loadReviewsByVendor(String vendorID) {
        List<String> reviews = new ArrayList<>();
        File file = new File("data/reviews.txt"); // 

        if (!file.exists()) {
            return reviews; // 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 4) {
                    String orderId = data[0];
                    String customerId = data[1];
                    String reviewText = data[2];
                    int rating = Integer.parseInt(data[3]);

                    // ✅ Find the order to check if it belongs to the vendor
                    for (Order order : loadOrders()) {
                        if (order.getOrderID().equals(orderId) && order.getVendorID().equals(vendorID)) {
                            reviews.add("Order: " + orderId + " | Customer: " + customerId + " | Rating: " + rating + "/5 | Review: " + reviewText);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading vendor reviews: " + e.getMessage());
        }

        return reviews;
    }

    // In FileHandler.java, modify the saveRunnerReview method to call saveReview
    public static void saveRunnerReview(Review review) {
        // Simply call saveReview to maintain consistent behavior
        saveReview(review);

        // Add a log message to help with debugging
        System.out.println("Runner review saved via saveRunnerReview, redirecting to unified saveReview method.");
    }

    // ✅ Load complaints from complaints.txt
    public static List<String> loadComplaints() {
        List<String> complaints = new ArrayList<>();
        File file = new File("data/complaints.txt");
        if (!file.exists()) {
            return complaints;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                complaints.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading complaints: " + e.getMessage());
        }
        return complaints;
    }

// ✅ Save complaints to file
    public static void saveComplaints(List<String> complaints) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/complaints.txt"))) {
            for (String complaint : complaints) {
                writer.write(complaint);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving complaints: " + e.getMessage());
        }
    }
}
