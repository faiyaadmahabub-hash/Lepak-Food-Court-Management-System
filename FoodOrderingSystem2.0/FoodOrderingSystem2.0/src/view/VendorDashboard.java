
package view;

import controller.VendorController;
import model.Order;
import model.MenuItem;
import model.Vendor;
import model.DR;
import util.FileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class VendorDashboard extends JFrame {

    private Vendor currentVendor;
    private VendorController vendorController;
    private JTable ordersTable, menuTable;
    private JButton acceptOrderBtn, rejectOrderBtn, updateStatusBtn, addMenuItemBtn, removeMenuItemBtn, generateReportBtn, viewReviewsBtn, logoutBtn;
    private JComboBox<String> assignDRDropdown;

    // Constructor
    public VendorDashboard(Vendor vendor, VendorController vendorController) {
        this.currentVendor = vendor;
        this.vendorController = vendorController;
        System.out.println("VendorDashboard initialized for vendor: " + vendor.getUserID());
        
        initComponents();
        setTitle("Vendor Dashboard");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Order Table
        ordersTable = new JTable(new DefaultTableModel(new Object[]{"Order ID", "Customer ID", "Quantity", "Status"}, 0));
        add(new JScrollPane(ordersTable), BorderLayout.NORTH);

        // Menu Table
        menuTable = new JTable(new DefaultTableModel(new Object[]{"Item ID", "Name", "Price", "Category"}, 0));
        add(new JScrollPane(menuTable), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        acceptOrderBtn = new JButton("Accept Order");
        rejectOrderBtn = new JButton("Reject Order");
        updateStatusBtn = new JButton("Assign Runner");
        addMenuItemBtn = new JButton("Add Menu Item");
        removeMenuItemBtn = new JButton("Remove Menu Item");
        generateReportBtn = new JButton("Generate Sales Report");
        viewReviewsBtn = new JButton("View Reviews"); // NEW: View Reviews Button
        logoutBtn = new JButton("Log Out");

        
        assignDRDropdown = new JComboBox<>();

        refreshDRDropdown();


        buttonPanel.add(acceptOrderBtn);
        buttonPanel.add(rejectOrderBtn);
        buttonPanel.add(assignDRDropdown);
        buttonPanel.add(updateStatusBtn);
        buttonPanel.add(addMenuItemBtn);
        buttonPanel.add(removeMenuItemBtn);
        buttonPanel.add(generateReportBtn);
        buttonPanel.add(viewReviewsBtn); // Added View Reviews Button
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load Data
        loadVendorOrders();
        loadVendorMenu();

        // Button Event Listeners
        acceptOrderBtn.addActionListener(e -> acceptOrder());
        rejectOrderBtn.addActionListener(e -> rejectOrder());
        updateStatusBtn.addActionListener(e -> assignDeliveryRunner());
        addMenuItemBtn.addActionListener(e -> addMenuItem());
        removeMenuItemBtn.addActionListener(e -> removeMenuItem());
        generateReportBtn.addActionListener(e -> generateSalesReport());
        viewReviewsBtn.addActionListener(e -> viewCustomerReviews()); // ✅ NEW: View Reviews Feature
   
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
        
    
    }   
        
    // Load Vendor Orders
    private void loadVendorOrders() {
        List<Order> orders = vendorController.getVendorOrders();
        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);

        for (Order order : orders) {
            System.out.println("Loading order: " + order.getOrderID() + ", Status: " + order.getStatus());
            model.addRow(new Object[]{order.getOrderID(), order.getCustomerID(), order.getQuantity(), order.getStatus()});
        }
        System.out.println("Vendor orders loaded.");
    }

    
   public void refreshDRDropdown() {
    // Clear existing items
    assignDRDropdown.removeAllItems();
    
    // Add default selection
    assignDRDropdown.addItem("Select Runner");
    
    // Add all available delivery runners
    List<DR> availableRunners = FileHandler.loadDeliveryRunners();
    for (DR runner : availableRunners) {
        assignDRDropdown.addItem(runner.getUserID());
    }
}

    // Accept Order
    private void acceptOrder() {
        
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = ordersTable.getValueAt(selectedRow, 0).toString();
            vendorController.acceptOrder(orderId);
            ordersTable.setValueAt("PREPARING", selectedRow, 3); 
            JOptionPane.showMessageDialog(this, "Order Accepted");
            
            
        } else{
            JOptionPane.showMessageDialog(this, "Please select an order to accept", 
                                      "No Order Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Reject Order
    private void rejectOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = ordersTable.getValueAt(selectedRow, 0).toString();
            vendorController.rejectOrder(orderId);
            JOptionPane.showMessageDialog(this, "Order Rejected");
            loadVendorOrders();
        } else {
JOptionPane.showMessageDialog(this, "Please select an order to reject", 
                                     "No Order Selected", JOptionPane.WARNING_MESSAGE);
}
    }

    // Assign Delivery Runner
    private void assignDeliveryRunner() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = ordersTable.getValueAt(selectedRow, 0).toString();
            String selectedDR = (String) assignDRDropdown.getSelectedItem();
            
            if (selectedDR.equals("Select Runner")) {
            JOptionPane.showMessageDialog(this, "Please select a delivery runner");
            return;
        }
        
        // Assign the selected DR to this order
        boolean success = vendorController.assignDeliveryRunner(orderId, selectedDR);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Delivery Runner assigned successfully!");
            ordersTable.setValueAt("PREPARING", selectedRow, 3);
            loadVendorOrders(); // Refresh the orders table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign Delivery Runner", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select an order", 
                                     "No Order Selected", JOptionPane.WARNING_MESSAGE);
    }
}
        

    // Load Vendor Menu
    private void loadVendorMenu() {
        List<MenuItem> menuItems = vendorController.getMenuItems(currentVendor);
        DefaultTableModel model = (DefaultTableModel) menuTable.getModel();
        model.setRowCount(0); // Clear previous data before loading

        for (MenuItem item : menuItems) {
            model.addRow(new Object[]{item.getItemId(), item.getName(), item.getPrice(), item.getCategory()});
        }
    }

    // Add Menu Item
    private void addMenuItem() {
        String itemId = JOptionPane.showInputDialog("Enter Item ID:");
        String name = JOptionPane.showInputDialog("Enter Item Name:");
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Item Price:"));
        String category = JOptionPane.showInputDialog("Enter Category:");

        MenuItem newItem = new MenuItem(itemId, name, price, category, true, currentVendor);

        vendorController.addMenuItem(newItem); // Fixed: Removed `currentVendor`

        JOptionPane.showMessageDialog(this, "Menu Item Added");
        loadVendorMenu();
    }

    // Remove Menu Item
    private void removeMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            String itemId = menuTable.getValueAt(selectedRow, 0).toString();
            
                try{
                    vendorController.removeMenuItem(itemId);
                    // Show success message
                    JOptionPane.showMessageDialog(this, "Menu Item Removed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Reload menu to reflect changes
                    loadVendorMenu();
                }catch(Exception e) {
                    System.err.println("Error removing menu item: " + e.getMessage());
            e.printStackTrace();
            
            // Show error to user
            JOptionPane.showMessageDialog(this, "Error removing item: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
                } 
   
                } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", 
                                     "Warning", JOptionPane.WARNING_MESSAGE);
        }
                }
    

    // Generate Sales Report (Fixed)
    private void generateSalesReport() {
        String report = vendorController.generateSalesReport();
        JOptionPane.showMessageDialog(this, report, "Sales Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // View Customer Reviews
    private void viewCustomerReviews() {
        List<String> reviews = vendorController.getCustomerReviews();
        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customer reviews available.", "Customer Reviews", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder reviewText = new StringBuilder();
            for (String review : reviews) {
                reviewText.append(review).append("\n");
            }
            JOptionPane.showMessageDialog(this, reviewText.toString(), "Customer Reviews", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(VendorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(VendorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(VendorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(VendorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    SwingUtilities.invokeLater(() -> {
        Vendor loggedInVendor = new Vendor("V001", "password123", "Vendor Name", "0123456789", "Business Address", "Default Business");
        new VendorDashboard(loggedInVendor, new VendorController(loggedInVendor)).setVisible(true);
    });

}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
