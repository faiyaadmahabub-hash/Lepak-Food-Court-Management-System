package model;

import java.util.ArrayList;
import java.util.List;
import util.FileHandler;

public class Vendor extends User {

    private String businessName;
    private List<MenuItem> menuItems;
    private List<Order> orderList;

    // ✅ Constructor (Fixes Business Name & Proper Initialization)
    public Vendor(String userID, String password, String name, String mobileNumber, String address, String businessName) {
        super(userID, password, "VENDOR", name, mobileNumber, address);
        this.businessName = (businessName != null && !businessName.isEmpty()) ? businessName : "Default Business";
        this.menuItems = new ArrayList<>();
        this.orderList = new ArrayList<>();
    }

    // ✅ Default Constructor (For cases where Vendor is initialized without data)
    public Vendor() {
        super("", "", "VENDOR", "", "", "");
        this.businessName = "Default Business";
        this.menuItems = new ArrayList<>();
        this.orderList = new ArrayList<>();
    }

    // ✅ Getter for Business Name
    public String getBusinessName() {
        return businessName;
    }

    // ✅ Getter for Menu Items
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    // ✅ Getter for Orders
    public List<Order> getOrderList() {
        return orderList;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    // ✅ Add a Menu Item (Ensure it is saved)
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void addMenuItem(MenuItem item) {
        item.setVendor(this);
        menuItems.add(item);
        FileHandler.saveMenuItems(menuItems); // ✅ Save updated menu items
    }

    // ✅ Remove a Menu Item (Ensure it is saved)
    public void removeMenuItem(String itemId) {
        menuItems.removeIf(item -> item.getItemId().equals(itemId));
        FileHandler.saveMenuItems(menuItems); // ✅ Save updated menu list
    }

    // ✅ Add Order to Vendor's List
    public void addOrder(Order order) {
        orderList.add(order);
        FileHandler.saveOrders(orderList); // ✅ Save updated orders
    }

    // ✅ Read Customer Reviews 
    public List<String> getCustomerReviews() {
        return new ArrayList<>(); // ✅ Implementation will be handled in `VendorController`
    }

    // ✅ Get User Type
    public String getUserType() {
        return "VENDOR";
    }
}
