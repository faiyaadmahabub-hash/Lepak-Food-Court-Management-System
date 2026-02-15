package view;

import controller.AdminController;
import controller.CustomerController;
import controller.DRController;
import controller.ManagerController;
import controller.UserController;
import controller.VendorController;
import model.User;

import javax.swing.*;
import java.awt.*;
import model.Admin;
import model.Customer;
import model.DR;
import model.Manager;
import model.Vendor;
import view.AdminDashboard;
import view.CustomerDashboard;
import view.ManagerDashboard;
import view.SignUpFrame;
import view.VendorDashboard;

public class LoginPage extends javax.swing.JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;

    public LoginPage() {
        setTitle("User Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        // Title Label
        JLabel titleLabel = new JLabel("Welcome! Please Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);

        // Username Field
        JPanel usernamePanel = new JPanel(new FlowLayout());
        usernamePanel.add(new JLabel("UserID:"));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);
        add(usernamePanel);

        // Password Field
        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);
        add(passwordPanel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        add(buttonPanel);

        setLocationRelativeTo(null);
        setVisible(true);

        // Button Actions
        loginButton.addActionListener(e -> authenticateUser());
        signupButton.addActionListener(e -> {
            new SignUpFrame().setVisible(true);
            dispose();
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = UserController.loginUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            redirectToDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redirectToDashboard(User user) {
        dispose();
        switch (user.getRole().toUpperCase()) {
            case "ADMIN":
                new AdminDashboard((Admin) user, new AdminController((Admin) user)).setVisible(true);
                break;
            case "MANAGER":
                new ManagerDashboard((Manager) user, new ManagerController((Manager) user)).setVisible(true);
                break;
            case "CUSTOMER":
                new CustomerDashboard((Customer) user, new CustomerController((Customer) user)).setVisible(true);
                break;
            case "VENDOR":
                new VendorDashboard((Vendor) user, new VendorController((Vendor) user)).setVisible(true);
                break;
            case "DELIVERY RUNNER":
                new DeliveryDashboard((DR) user, new DRController((DR) user)).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown user role!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
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

    
    public static void main(String args[]) {
        /* Create and display the form */
        new LoginPage();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
