/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.JOptionPane;
import model.Customer;

public class CustomerDashboard extends javax.swing.JFrame {

    /**
     * Creates new form CustomerDashboard
     */
    public CustomerDashboard() {
        initComponents();
    }

    CustomerDashboard(Customer currentCustomer) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogout = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        btnReview = new javax.swing.JButton();
        btnOrderStatus = new javax.swing.JButton();
        btnCP = new javax.swing.JButton();
        btnCOH = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogout.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 32, 168, 36));

        btnMenu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/new product.png"))); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });
        getContentPane().add(btnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(589, 32, 193, 36));

        btnReview.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnReview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/View Bills & Order Placed Details.png"))); // NOI18N
        btnReview.setText("Review");
        btnReview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewActionPerformed(evt);
            }
        });
        getContentPane().add(btnReview, new org.netbeans.lib.awtextra.AbsoluteConstraints(1018, 32, 173, 36));

        btnOrderStatus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnOrderStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/place order.png"))); // NOI18N
        btnOrderStatus.setText("Check Order Status");
        btnOrderStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrderStatusActionPerformed(evt);
            }
        });
        getContentPane().add(btnOrderStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 688, -1, -1));

        btnCP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/change Password.png"))); // NOI18N
        btnCP.setText("Change Password");
        btnCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCPActionPerformed(evt);
            }
        });
        getContentPane().add(btnCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(589, 687, 204, 39));

        btnCOH.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCOH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/view edit delete product.png"))); // NOI18N
        btnCOH.setText("Check Order History");
        btnCOH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCOHActionPerformed(evt);
            }
        });
        getContentPane().add(btnCOH, new org.netbeans.lib.awtextra.AbsoluteConstraints(1018, 688, -1, -1));

        jLabel2.setBackground(new java.awt.Color(92, 64, 51));
        jLabel2.setFont(new java.awt.Font("Segoe Script", 3, 70)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(92, 64, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Customer Main Page");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 1180, 110));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Untitled design (2).png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        this.dispose(); //Close login page
        new Login().setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        Menu menuPage = new Menu();  //Create a new instance of Menu Jframe
        menuPage.setVisible(true);  //Show the Menu Jframe
        this.dispose();             //Close the Customer Dashboard Jframe
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnReviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReviewActionPerformed

    private void btnOrderStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrderStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOrderStatusActionPerformed

    private void btnCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCPActionPerformed

    private void btnCOHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCOHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCOHActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCOH;
    private javax.swing.JButton btnCP;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnOrderStatus;
    private javax.swing.JButton btnReview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
