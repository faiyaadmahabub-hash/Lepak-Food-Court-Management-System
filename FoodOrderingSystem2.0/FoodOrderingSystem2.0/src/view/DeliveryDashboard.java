/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import controller.DRController;
import model.DR;
import view.LoginPage;
import javax.swing.Timer;
import java.awt.event.ActionListener;

public class DeliveryDashboard extends JFrame {

    private DRController drController;
    private DR deliveryRunner;
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private Timer refreshTimer;
    private JButton revenueReportBtn;

    public DeliveryDashboard(DR deliveryRunner, DRController drController) {
        this.deliveryRunner = deliveryRunner;
        this.drController = drController;

        setTitle("Delivery Runner Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ✅ Background Color for UI
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ✅ Title Label
        JLabel titleLabel = new JLabel("Delivery Runner Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(192, 192, 192));
        titleLabel.setPreferredSize(new Dimension(900, 50));

        // ✅ Orders Table with Column Resizing
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Vendor", "Customer", "Status"}, 0);
        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(30);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        loadAssignedOrders();

        // ✅ Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton acceptDeliveryBtn = createStyledButton("Accept Delivery", new Color(144, 238, 144));
        JButton declineDeliveryBtn = createStyledButton("Decline Delivery", new Color(255, 165, 0));
        JButton updateStatusBtn = createStyledButton("Update Status", new Color(255, 223, 186));
        JButton taskHistoryBtn = createStyledButton("Task History", new Color(204, 204, 255));
        JButton viewReviewsBtn = createStyledButton("View Reviews", new Color(173, 216, 230)); // ✅ Light Blue
        revenueReportBtn = createStyledButton("Revenue Dashboard", new Color(255, 230, 153));
        JButton logoutBtn = createStyledButton("Logout", new Color(255, 102, 102));

        logoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        logoutBtn.setForeground(Color.WHITE);

        // ✅ Button Actions
        acceptDeliveryBtn.addActionListener(e -> acceptSelectedOrder());
        declineDeliveryBtn.addActionListener(e -> declineSelectedOrder());
        updateStatusBtn.addActionListener(e -> updateOrderStatus());
        taskHistoryBtn.addActionListener(e -> showTaskHistory());
        viewReviewsBtn.addActionListener(e -> showCustomerReviews());
        revenueReportBtn.addActionListener(e -> showRevenueReport());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });

        // ✅ Adding Buttons to Panel
        buttonPanel.add(acceptDeliveryBtn);
        buttonPanel.add(declineDeliveryBtn);
        buttonPanel.add(updateStatusBtn);
        buttonPanel.add(taskHistoryBtn);
        buttonPanel.add(viewReviewsBtn);
        buttonPanel.add(revenueReportBtn);
        buttonPanel.add(logoutBtn);

        // ✅ Adding Components to Frame
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ✅ Auto-Refresh Timer (15 seconds)
        startAutoRefresh();

        setVisible(true);
    }

    // ✅ Button Styling
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        button.setFocusPainted(false);
        return button;
    }

    // ✅ Load Assigned Orders
    private void loadAssignedOrders() {
        tableModel.setRowCount(0);
        List<String[]> orders = drController.getAssignedOrders();
        for (String[] order : orders) {
            tableModel.addRow(order);
        }

    }

    // ✅ Accept Order
    private void acceptSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);
            boolean success = drController.acceptDelivery(orderId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Order accepted successfully!");
                loadAssignedOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Order is no longer available.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to accept.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // DECLINE ORDER (NEW)
    private void declineSelectedOrder(){
        int selectedRow = ordersTable.getSelectedRow();
        if(selectedRow != -1) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);
            boolean success = drController.declineDelivery(orderId);
            if(success){
                JOptionPane.showMessageDialog(this, "Delivery declined successfully!");
                loadAssignedOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to decline delivery. The Order may no longer be available.",
                                               "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(this, "Please select an order to decline.",
                                           "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ✅ Update Order Status
    private void updateOrderStatus() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);
            String[] statuses = {"IN TRANSIT", "DELIVERED", "FAILED"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status",
                    JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

            if (newStatus != null) {
                drController.updateStatus(orderId, newStatus);
                JOptionPane.showMessageDialog(this, "Order status updated successfully!");
                loadAssignedOrders();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ✅ Show Task History (Fixed Incorrect Argument)
    private void showTaskHistory() {
        String history = drController.getTaskHistory();
        JOptionPane.showMessageDialog(this, history, "Task History", JOptionPane.INFORMATION_MESSAGE);
    }

    // ✅ Display Customer Reviews
    private void showCustomerReviews() {
        String reviews = drController.getCustomerReviews();
        JOptionPane.showMessageDialog(this, reviews, "Customer Reviews", JOptionPane.INFORMATION_MESSAGE);
    }

    // ✅ Auto-Refresh Orders Every 15 Seconds (Fixed Interval)
    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAssignedOrders();
            }
        });
        refreshTimer.start();
    }

    // METHOD TO DISPLAY REPORT
    private void showRevenueReport() {
        String report = drController.generateRevenueReport();
        JOptionPane.showMessageDialog(this, report, "Revenue Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        DR deliveryRunner = new DR("DR001", "password", "John Doe", "0123456789", "Address");
        DRController drController = new DRController(deliveryRunner);
        new DeliveryDashboard(deliveryRunner, drController).setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
