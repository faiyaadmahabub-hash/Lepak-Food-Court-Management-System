package controller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import model.Admin;
import model.User;
import model.Customer;
import model.DR;
import model.Vendor;
import util.FileHandler;

public class AdminController {

    private Admin admin;
    private List<User> users;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.users = FileHandler.loadUsers();
    }

    // Manage Delivery Runners    (change needed)
    public void manageRunners() {
        List<DR> DeliveryRunners = FileHandler.loadDeliveryRunners();
        if (DeliveryRunners.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Delivery Runner found.");
        } else {
            StringBuilder deliveryList = new StringBuilder("Runners:\n");
            for (DR DR : DeliveryRunners) {
                deliveryList.append(DR.getName()).append(" (").append(DR.getUserID()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, deliveryList.toString(), "Delivery List", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Manage Vendors
    public void manageVendors() {
        List<Vendor> vendors = FileHandler.loadVendors();
        if (vendors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vendors found.");
        } else {
            StringBuilder vendorList = new StringBuilder("Vendors:\n");
            for (Vendor v : vendors) {
                vendorList.append(v.getName()).append(" (").append(v.getUserID()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, vendorList.toString(), "Vendor List", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void registerUser(User newUser) {
        List<User> users = FileHandler.loadUsers(); // Load existing users
        users.add(newUser); // Add the new user
        FileHandler.saveUsers(users); // Save back to file
        System.out.println("User registered successfully: " + newUser.getUserID());
    }

    // ✅ Edit User (Ensures User Exists)
    public void editUser() {
        String userID = JOptionPane.showInputDialog("Enter User ID to edit:");
        User userToEdit = findUserByID(userID);

        if (userToEdit == null) {
            JOptionPane.showMessageDialog(null, "User not found!");
            return;
        }

        // Edit only if input is valid
        String newMobile = JOptionPane.showInputDialog("Enter new mobile number:", userToEdit.getmobileNumber());
        if (newMobile != null && !newMobile.trim().isEmpty()) {
            userToEdit.setmobileNumber(newMobile);
        }

        String newAddress = JOptionPane.showInputDialog("Enter new address:", userToEdit.getAddress());
        if (newAddress != null && !newAddress.trim().isEmpty()) {
            userToEdit.setAddress(newAddress);
        }

        FileHandler.updateUser(userToEdit);
        users = FileHandler.loadUsers();
        JOptionPane.showMessageDialog(null, "User updated successfully!");
    }

    // ✅ Delete User (Confirms Before Deleting)
    public void deleteUser() {
        String userID = JOptionPane.showInputDialog("Enter User ID to delete:");
        User userToDelete = findUserByID(userID);

        if (userToDelete == null) {
            JOptionPane.showMessageDialog(null, "User not found!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            FileHandler.deleteUser(userID);
            JOptionPane.showMessageDialog(null, "User deleted successfully!");
        }
    }

    //  Method to Top-Up Customer Credit
    public void topUpCustomerCredit() {
        String customerId = JOptionPane.showInputDialog("Enter Customer ID:");

        if (customerId == null || customerId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Find customer instance
        User user = findUserByID(customerId);

        if (user == null || !(user instanceof Customer)) {
            JOptionPane.showMessageDialog(null, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = (Customer) user; //Cast to Customer

        try {
            String input = JOptionPane.showInputDialog("Enter Amount to Top-Up:");
            if (input == null || input.trim().isEmpty()) {
                return;
            }

            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double oldBalance = customer.getWalletBalance();
            customer.setWalletBalance(oldBalance + amount); // New technique
            FileHandler.updateUser(customer);

            String transactionId = "TRX" + System.currentTimeMillis();
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            saveTransaction(customer.getUserID(), transactionId, "Top-Up", amount, timestamp);

            JOptionPane.showMessageDialog(null, "Top-Up Successful! New Balance: RM " + customer.getWalletBalance());

            int sendReceipt = JOptionPane.showConfirmDialog(null,
                    "Do you want to generate and send a receipt to the customer?",
                    "Generate Receipt",
                    JOptionPane.YES_NO_OPTION);

            if (sendReceipt == JOptionPane.YES_OPTION) {
                // Generate receipt
                String receipt = generateReceiptForTopUp(customer, transactionId, amount, oldBalance, timestamp);

                // Show preview
                JTextArea textArea = new JTextArea(receipt);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Receipt Preview", JOptionPane.INFORMATION_MESSAGE);

                // Send notification
                sendNotification(customer, receipt);
                JOptionPane.showMessageDialog(null, "Receipt sent to customer successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // GENERATE RECEIPT (NEW)
    public void generateAndSendReceipt() {
        // Get list of recent transactions
        List<String[]> transactions = loadRecentTransactions();

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No recent transactions found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display transaction list for admin to select
        String[] transactionOptions = new String[transactions.size()];
        for (int i = 0; i < transactions.size(); i++) {
            String[] transaction = transactions.get(i);
            transactionOptions[i] = "ID: " + transaction[0] + " | Customer: " + transaction[1]
                    + " | Amount: RM" + transaction[3] + " | Date: " + transaction[4];
        }

        String selectedTransaction = (String) JOptionPane.showInputDialog(
                null,
                "Select transaction to generate receipt for:",
                "Generate Receipt",
                JOptionPane.QUESTION_MESSAGE,
                null,
                transactionOptions,
                transactionOptions[0]);

        if (selectedTransaction == null) {
            return; // User canceled
        }

        // Find the selected transaction
        int selectedIndex = -1;
        for (int i = 0; i < transactionOptions.length; i++) {
            if (transactionOptions[i].equals(selectedTransaction)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex == -1) {
            return; // Something went wrong
        }

        String[] transaction = transactions.get(selectedIndex);
        String transactionId = transaction[0];
        String customerId = transaction[1];
        String transactionType = transaction[2];
        double amount = Double.parseDouble(transaction[3]);
        String date = transaction[4];

        // Find the customer
        User user = findUserByID(customerId);
        if (user == null || !(user instanceof Customer)) {
            JOptionPane.showMessageDialog(null, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer customer = (Customer) user;

        // Generate receipt
        String receipt = generateReceipt(customer, transactionId, transactionType, amount, date);

        // Show preview of receipt
        JTextArea textArea = new JTextArea(receipt);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Receipt Preview", JOptionPane.INFORMATION_MESSAGE);

        // Ask user to confirm sending the receipt
        int confirm = JOptionPane.showConfirmDialog(null,
                "Send this receipt to " + customer.getName() + "?",
                "Confirm Send",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Send notification
            sendNotification(customer, receipt);
            JOptionPane.showMessageDialog(null, "Receipt sent to customer successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Get All Customers
    public List<Customer> getCustomers() {
        List<Customer> customers = new java.util.ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    //Find User by ID (Uses FileHandler to Prevent Stale Data)
    private User findUserByID(String userID) {
        return users.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst()
                .orElse(null);
    }

    private List<String[]> loadRecentTransactions() {
        List<String[]> transactions = new ArrayList<>();

        try {
            File file = new File("data/transactions.txt");
            if (!file.exists()) {
                return transactions;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    transactions.add(parts);
                }
            }
            reader.close();

            // Sort by timestamp (newest first)
            Collections.reverse(transactions);

        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }

        return transactions;
    }

    private String generateReceipt(Customer customer, String transactionId, String transactionType, double amount, String transactionDate) {
        // Build receipt content
        StringBuilder receipt = new StringBuilder();
        receipt.append("===================================\n");
        receipt.append("          TRANSACTION RECEIPT         \n");
        receipt.append("===================================\n\n");
        receipt.append("Transaction ID: ").append(transactionId).append("\n");
        receipt.append("Date: ").append(transactionDate).append("\n\n");
        receipt.append("Customer ID: ").append(customer.getUserID()).append("\n");
        receipt.append("Customer Name: ").append(customer.getName()).append("\n\n");
        receipt.append("Transaction Type: ").append(transactionType).append("\n");
        receipt.append("Amount: RM ").append(String.format("%.2f", amount)).append("\n\n");
        receipt.append("Current Balance: RM ").append(String.format("%.2f", customer.getWalletBalance())).append("\n\n");
        receipt.append("===================================\n");
        receipt.append("Thank you for using our service!\n");
        receipt.append("===================================\n");

        return receipt.toString();
    }

    private void sendNotification(Customer customer, String receipt) {
        try {
            // Create notifications directory if it doesn't exist
            File notifDir = new File("data/notifications");
            if (!notifDir.exists()) {
                notifDir.mkdirs();
            }

            // Create notification file for this customer
            String fileName = "data/notifications/" + customer.getUserID() + "_" + System.currentTimeMillis() + ".txt";

            // Write the notification
            FileWriter fw = new FileWriter(fileName);
            fw.write("NOTIFICATION: Transaction Receipt\n");
            fw.write("TO: " + customer.getName() + "\n");
            fw.write("DATE: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n");
            fw.write("------------------------------------\n\n");
            fw.write(receipt);
            fw.close();

            System.out.println("Notification sent to customer: " + customer.getUserID());

        } catch (IOException e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }

    }

    //NEW METHOD 
    private void saveTransaction(String customerId, String transactionId, String type, double amount, String timestamp) {
        try {
            // Make sure the data directory exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            // Create the transaction record
            String transactionData = transactionId + "|" + customerId + "|" + type + "|"
                    + amount + "|" + timestamp + "\n";

            // Write to transactions.txt (append mode)
            FileWriter fw = new FileWriter("data/transactions.txt", true);
            fw.write(transactionData);
            fw.close();

            System.out.println("Transaction saved: " + transactionData);
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    private String generateReceiptForTopUp(Customer customer, String transactionId, double amount, double oldBalance, String timestamp) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("===================================\n");
        receipt.append("          TRANSACTION RECEIPT         \n");
        receipt.append("===================================\n\n");
        receipt.append("Transaction ID: ").append(transactionId).append("\n");
        receipt.append("Date: ").append(timestamp).append("\n\n");
        receipt.append("Customer ID: ").append(customer.getUserID()).append("\n");
        receipt.append("Customer Name: ").append(customer.getName()).append("\n\n");
        receipt.append("Transaction Type: Top-Up\n");
        receipt.append("Amount: RM ").append(String.format("%.2f", amount)).append("\n\n");
        receipt.append("Previous Balance: RM ").append(String.format("%.2f", oldBalance)).append("\n");
        receipt.append("New Balance: RM ").append(String.format("%.2f", customer.getWalletBalance())).append("\n\n");
        receipt.append("===================================\n");
        receipt.append("Thank you for using our service!\n");
        receipt.append("===================================\n");

        return receipt.toString();
    }
}
