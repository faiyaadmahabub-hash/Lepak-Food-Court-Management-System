package model;

import java.util.List;
import java.util.ArrayList;
import util.FileHandler;

public class Manager extends User {

    private List<Order> allOrders;
    private List<String> complaints;  // ✅ Added complaints tracking

    public Manager(String userID, String password, String name, String mobileNumber, String address) {
        super(userID, password, "MANAGER", name, mobileNumber, address);
        this.allOrders = new ArrayList<>();
        this.complaints = new ArrayList<>();
    }

    // ✅ Getter for all orders
    public List<Order> getAllOrders() {
        return allOrders;
    }

    // ✅ Add order to the list
    public void addOrder(Order order) {
        allOrders.add(order);
    }

    // ✅ Remove order by order ID
    public void removeOrder(String orderId) {
        allOrders.removeIf(order -> order.getOrderID().equals(orderId));
    }

    // ✅ Load complaints from FileHandler
    public void loadComplaints() {
        this.complaints = FileHandler.loadComplaints();
    }

    // ✅ Get complaints list
    public List<String> getComplaints() {
        return complaints;
    }

    // ✅ Resolve complaint
    public void resolveComplaint(int index) {
        if (index >= 0 && index < complaints.size()) {
            complaints.remove(index);
            FileHandler.saveComplaints(complaints);  // ✅ Save updated complaints
        }
    }

    public String getUserType() {
        return "Manager";
    }
}
