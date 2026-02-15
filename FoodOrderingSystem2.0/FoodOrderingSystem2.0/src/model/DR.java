package model;

import util.FileHandler;
import java.util.List;
import java.util.stream.Collectors;

public class DR extends User {

    public DR(String userID, String password, String name, String mobileNumber, String address) {
        super(userID, password, "DELIVERY RUNNER", name, mobileNumber, address);
    }

    // ✅ Get Assigned Orders
    public List<Order> getAssignedOrders() {
        return FileHandler.loadOrders().stream()
                .filter(order -> order.getAssignedDR().equals(this.getUserID()))
                .collect(Collectors.toList());
    }

    // ✅ Assign Order to the DR and Save to File
    public void assignOrder(Order order) {
        if (order.getAssignedDR().isEmpty()) {
            order.setAssignedDR(this.getUserID());
            order.setStatus("IN TRANSIT");

            // ✅ Save changes
            List<Order> orders = FileHandler.loadOrders();
            for (Order o : orders) {
                if (o.getOrderID().equals(order.getOrderID())) {
                    o.setAssignedDR(this.getUserID());
                    o.setStatus("IN TRANSIT");
                    break;
                }
            }
            FileHandler.saveOrders(orders);
        }
    }

    // ✅ Remove Order (Only if Delivered or Failed)
    public void removeOrder(String orderId) {
        List<Order> orders = FileHandler.loadOrders();
        Order orderToRemove = null;

        for (Order order : orders) {
            if (order.getOrderID().equals(orderId) && order.getAssignedDR().equals(this.getUserID())) {
                if (order.getStatus().equals("DELIVERED") || order.getStatus().equals("FAILED")) {
                    orderToRemove = order;
                    break;
                }
            }
        }

        if (orderToRemove != null) {
            orders.remove(orderToRemove);
            FileHandler.saveOrders(orders);
        }
    }

    // ✅ Get Completed Orders (Latest from File)
    public List<Order> getCompletedOrders() {
        return FileHandler.loadOrders().stream()
                .filter(order -> order.getAssignedDR().equals(this.getUserID())
                && (order.getStatus().equals("DELIVERED") || order.getStatus().equals("FAILED")))
                .collect(Collectors.toList());
    }
}
