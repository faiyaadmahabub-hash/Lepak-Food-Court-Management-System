package model;

public class Order {

    private String orderID;
    private String vendorID;
    private String customerID;
    private String assignedDR;
    private int quantity;
    private String status;
    private MenuItem menuItem;
    private String deliveryType = "DINE_IN"; // Default to dine-in. NEW CLASS
    private double deliveryFee = 0.0;

    // ✅ Constructor: Ensure all fields are correctly assigned
    public Order(String orderID, String vendorID, String customerID, String assignedDR, MenuItem menuItem, int quantity, String status) {
        this.orderID = orderID;
        this.vendorID = vendorID;
        this.customerID = customerID;
        this.assignedDR = assignedDR;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.status = status;
        this.deliveryType = "DINE_IN";
        this.deliveryFee = 0.0;
    }

    // ✅ Getters
    public String getOrderID() {
        return orderID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAssignedDR() {
        return assignedDR;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
    
    public String getDeliveryType() {
    return deliveryType;
}
    
    public double getDeliveryFee() {
    return deliveryFee;
}
    
    public boolean isDineIn(){
        return "DINE_IN".equals(deliveryType);
    }


    // ✅ Check if Order is Assigned to a Delivery Runner
    public boolean isAssigned() {
        return (assignedDR != null && !assignedDR.isEmpty() && !assignedDR.equals("N/A"));
    }

    // ✅ Prevent NullPointerException if MenuItem is missing
    public String getMenuItemID() {
        return (menuItem != null) ? menuItem.getItemId() : "N/A";
    }

    // ✅ Getters for MenuItem
    public MenuItem getMenuItem() {
        return menuItem;
    }

    // ✅ Setters (Ensure Sync with FileHandler)
    public void setAssignedDR(String assignedDR) {
        this.assignedDR = assignedDR;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
}
    
    public void setDeliveryFee(double deliveryFee) {
    this.deliveryFee = deliveryFee;
}


    // ✅ Validate Status Before Setting
    public void setStatus(String status) {
        if (status.equals("PENDING") || status.equals("PREPARING") || status.equals("DINE IN")||status.equals("IN TRANSIT") || status.equals("DELIVERED") || status.equals("FAILED")) {
            this.status = status;
        } else {
            System.out.println("❌ Invalid status: " + status);
        }
    }
    
    

    // ✅ Remove Assigned Delivery Runner
    public void removeAssignedDR() {
        this.assignedDR = "N/A";
    }

    // ✅ Calculate total amount correctly
    public double getTotalAmount() {
        if (menuItem != null) {
            return (this.quantity * menuItem.getPrice()) + deliveryFee;
        } else {
            System.out.println("⚠ Warning: Menu item not found for order " + orderID);
            return 0;
        }
    }
}
