package controller;

import model.*;
import util.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class VendorController {

    private List<MenuItem> menuItems;
    private Vendor currentVendor;

    // Constructor to Load Orders and Menu Items from FileHandler
    public VendorController(Vendor vendor) {
        this.currentVendor = vendor;
        this.menuItems = FileHandler.loadMenuItems();

        if (this.menuItems == null) {
            this.menuItems = new ArrayList<>(); // Prevent NullPointerException
        }
    }

    // Vendor Login
    public Vendor loginVendor(String username, String password) {
        List<Vendor> vendors = FileHandler.loadVendors(); // Load vendors on demand
        for (Vendor vendor : vendors) {
            if (vendor.getUserID().equals(username) && vendor.getPassword().equals(password)) {
                return vendor;
            }
        }
        return null; // Return null if login fails
    }

    // Get Vendor's Orders
    public List<Order> getVendorOrders() {
        List<Order> vendorOrders = new ArrayList<>();
        List<Order> allOrders = FileHandler.loadOrders(); // Load orders dynamically

        for (Order order : allOrders) {
            if (order.getVendorID().equals(currentVendor.getUserID())) {
                vendorOrders.add(order);
            }
        }
        return vendorOrders;
    }

    // Accept Order
    public void acceptOrder(String orderId) {
        List<Order> orders = FileHandler.loadOrders();
        List<DR> availableRunners = FileHandler.loadDeliveryRunners();
        boolean orderFound = false;

        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getVendorID().equals(currentVendor.getUserID())) {
                // Check if it's a DINE_IN order (either by delivery type or current status)
                if (order.getDeliveryType().equals("DINE_IN") || order.getStatus().equals("DINE IN")) {
                    // For DINE_IN orders:
                    order.setStatus("PREPARING");
                    order.setAssignedDR("DINE_IN");
                    System.out.println("DINE IN order " + orderId + " accepted. Status: PREPARING, AssignedDR: DINE_IN");
                } else {
                    // For non-DINE_IN orders:
                    order.setStatus("PREPARING");

                    // Auto-assign a delivery runner if available
                    if (!availableRunners.isEmpty()) {
                        // Simple assignment - just pick the first available runner
                        String runnerId = availableRunners.get(0).getUserID();
                        order.setAssignedDR(runnerId);
                        System.out.println("Order " + orderId + " assigned to runner " + runnerId);
                    }
                }

                orderFound = true;
                break;
            }
        }

        if (orderFound) {
            // Save all orders back to file
            FileHandler.saveOrders(orders);
            System.out.println("Order " + orderId + " status updated and saved");
        } else {
            System.out.println("Order " + orderId + " not found or not owned by this vendor");
        }
    }

    // Reject Order
    public void rejectOrder(String orderId) {
        List<Order> orders = FileHandler.loadOrders();
        orders.removeIf(order -> order.getOrderID().equals(orderId) && order.getVendorID().equals(currentVendor.getUserID()));
        FileHandler.saveOrders(orders);
    }

    // Update Order Status
    public void updateOrderStatus(String orderId, String newStatus) {
        List<Order> orders = FileHandler.loadOrders();
        boolean orderFound = false;

        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getVendorID().equals(currentVendor.getUserID())) {
                orderFound = true;

                if (order.getStatus().equals("PREPARING") && !order.getAssignedDR().isEmpty()
                        && !order.getAssignedDR().equals("N/A") && newStatus.equals("PENDING")) {
                    System.out.println("Order " + orderId + " already has delivery runner assigned. Keeping PREPARING status.");
                } else {
                    order.setStatus(newStatus);
                }

                FileHandler.saveOrders(orders);
                return;
            }
        }
        if (!orderFound) {
            System.out.println("Order " + orderId + "not found or doesn't belong to vendor" + currentVendor.getUserID());
        }
    }

    public List<MenuItem> getVendorMenu() {
        List<MenuItem> vendorMenu = new ArrayList<>();
        List<MenuItem> allMenuItems = FileHandler.loadMenuItems();

        for (MenuItem item : allMenuItems) {
            if (item.getVendor() != null && item.getVendor().getUserID().equals(currentVendor.getUserID())) {
                vendorMenu.add(item);
            }
        }
        return vendorMenu;
    }

    // Retrieve Menu Items for a Specific Vendor
    public List<MenuItem> getMenuItems(Vendor vendor) {
        List<MenuItem> vendorMenu = new ArrayList<>();
        List<MenuItem> allMenuItems = FileHandler.loadMenuItems(); // Load all menu items

        for (MenuItem item : allMenuItems) {
            if (item.getVendor() != null && item.getVendor().getUserID().equals(vendor.getUserID())) {
                vendorMenu.add(item);
            }
        }
        return vendorMenu;
    }

    //Add method to choose runner
    public boolean assignDeliveryRunner(String orderId, String drId) {
        List<Order> orders = FileHandler.loadOrders();

        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getVendorID().equals(currentVendor.getUserID())) {
                if (order.getStatus().equals("PENDING")) {
                    order.setStatus("PREPARING");
                }
                order.setAssignedDR(drId);
                FileHandler.saveOrders(orders);
                return true;
            }
        }
        return false;
    }

    // Manage Menu Items (Add)
    public void addMenuItem(MenuItem item) {
        if (menuItems == null) {
            menuItems = new ArrayList<>(); // Ensure list is initialized before adding
        }

        menuItems.add(item);
        FileHandler.saveMenuItems(menuItems); // Save updated menu items
    }

    // Manage Menu Items (Remove)
    // Also check and fix the removeMenuItem method in VendorController.java
    public void removeMenuItem(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            System.out.println("Error: Attempt to remove item with null or empty ID");
            return;
        }

        System.out.println("VendorController: Removing menu item with ID: " + itemId);

        // Load all menu items
        List<MenuItem> menuItems = FileHandler.loadMenuItems();

        // Track if we found the item
        boolean itemFound = false;

        // Create a new list without the item to remove
        List<MenuItem> updatedMenuItems = new ArrayList<>();

        for (MenuItem item : menuItems) {
            if (item.getItemId().equals(itemId)
                    && (item.getVendor() != null && item.getVendor().getUserID().equals(currentVendor.getUserID()))) {
                // Skip this item (removing it from the list)
                itemFound = true;
                System.out.println("Found and removing item: " + item.getName());
            } else {
                // Keep this item
                updatedMenuItems.add(item);
            }
        }

        // Only save if we actually found and removed an item
        if (itemFound) {
            FileHandler.saveMenuItems(updatedMenuItems);
            System.out.println("Item removed and menu saved successfully");

            // Update the class member variable to reflect the change
            this.menuItems = updatedMenuItems;
        } else {
            System.out.println("Warning: Item with ID " + itemId + " not found or doesn't belong to vendor " + currentVendor.getUserID());
        }
    }

    // Generate Sales Report (Now Returns String Instead of Printing)
    public String generateSalesReport() {
        double totalSales = 0;
        StringBuilder report = new StringBuilder();

        report.append("Sales Report for ").append(currentVendor.getBusinessName()).append("\n");
        report.append("---------------------------------------\n");

        List<Order> orders = FileHandler.loadOrders();
        for (Order order : orders) {
            if (order.getVendorID().equals(currentVendor.getUserID())) {
                double orderTotal = order.getQuantity() * order.getMenuItem().getPrice();
                totalSales += orderTotal;

                report.append("Order ID: ").append(order.getOrderID())
                        .append(" | Item: ").append(order.getMenuItem().getName())
                        .append(" | Quantity: ").append(order.getQuantity())
                        .append(" | Total: ").append(String.format("RM %.2f", orderTotal)).append("\n");
            }
        }

        report.append("---------------------------------------\n");
        report.append("Total Sales: ").append(String.format("RM %.2f", totalSales));

        return report.toString();
    }

    // NEW FEATURE: Get Customer Reviews for Vendor
    public List<String> getCustomerReviews() {
        List<String> reviews = FileHandler.loadReviewsByVendor(currentVendor.getUserID());
        return (reviews != null) ? reviews : new ArrayList<>();
    }
}
