/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nurul
 */
public class MenuItem {

    private String itemId;
    private String name;
    private double price;
    private String description;
    private String category;
    private boolean available;
    private Vendor vendor;

    // Full Constructor (Ensuring all fields are set)
    public MenuItem(String itemId, String name, double price, String category, boolean available, Vendor vendor) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
        this.vendor = vendor; // ✅ Ensure vendor is assigned
    }

    // Constructor without Vendor (For cases where vendor is unknown)
    public MenuItem(String itemId, String name, double price, String category, boolean available) {
        this(itemId, name, price, category, available, null); // ✅ Default vendor to null
    }

    // Default Constructor
    public MenuItem() {
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (isValidPrice(price)) {
            this.price = price;
        } else {
            System.out.println("❌ Invalid price: " + price);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    // ✅ Validate price
    private boolean isValidPrice(double price) {
        return price >= 0;
    }

    // ✅ toString() for Debugging
    @Override
    public String toString() {
        return "MenuItem{"
                + "itemId='" + itemId + '\''
                + ", name='" + name + '\''
                + ", price=" + price
                + ", description='" + description + '\''
                + ", category='" + category + '\''
                + ", available=" + available
                + ", vendor=" + (vendor != null ? vendor.getUserID() : "No Vendor Assigned")
                + '}';
    }
}
