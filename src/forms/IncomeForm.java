package forms;

import components.SimpleForm;
import menu.FormManager;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */
public class IncomeForm extends SimpleForm {
    
    DateFormat dateFormat = new SimpleDateFormat("MMMM/dd/yyyy");
    java.util.Date date = new java.util.Date();
    Calendar cal = Calendar.getInstance();
    DefaultTableModel model;
    private double totalAmount = 0.0;

    public IncomeForm() {
        initComponents();

        model = new DefaultTableModel(); // Initialize the table model
        dateTF.setText(dateFormat.format(date).trim());

        // Initialize balance label
        balance.setText("Balance: $ " + String.format("%.2f", calculateBalance()));

        // Load existing data from the database when the form is initialized
        loadIncomeData();
    }

    // Method to load existing income data from the database
    private void loadIncomeData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            String sql = "SELECT * FROM incomes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Clear existing table data
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            // Populate the table with data from the database
            while (rs.next()) {
                String date = rs.getString("date");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                model.addRow(new Object[]{date, description, amount, category});

                // Update total amount
                totalAmount += amount;
            }

            rs.close();
            pstmt.close();
            conn.close();

            // Update balance label
            balance.setText("Balance: $ " + String.format("%.2f", calculateBalance()));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading income data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to calculate balance (Income - Expense)
    private double calculateBalance() {
        double expenseAmount = 0.0;

        try {
            // Fetch total expense amount from the expenses table
            Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            String expenseSql = "SELECT SUM(amount) FROM expenses";
            PreparedStatement expensePstmt = conn.prepareStatement(expenseSql);
            ResultSet expenseRs = expensePstmt.executeQuery();
            if (expenseRs.next()) {
                expenseAmount = expenseRs.getDouble(1);
            }
            expenseRs.close();
            expensePstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating balance", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return totalAmount - expenseAmount;
    }
    private boolean isValidDateFormat(String date) {
    String[] parts = date.split("/");
    if (parts.length != 3) {
        return false;
    }

    // Validate month
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    boolean isValidMonth = false;
    for (String month : months) {
        if (parts[0].equalsIgnoreCase(month)) {
            isValidMonth = true;
            break;
        }
    }
    if (!isValidMonth) {
        return false;
    }

    // Validate day and year
    try {
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31 || year < 1000 || year > 9999) {
            return false;
        }
    } catch (NumberFormatException e) {
        return false;
    }

    return true;
}
    // Other methods and components of your Income class...

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        desTF = new javax.swing.JTextPane();
        add = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        amnTF = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        cateTF = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        dateTF = new javax.swing.JTextPane();
        delete = new javax.swing.JButton();
        balance = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();

        jButton1.setText("View Dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Date");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Description");

        jScrollPane1.setViewportView(desTF);

        add.setBackground(new java.awt.Color(255, 153, 51));
        add.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        add.setText("ADD");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Amount");

        jScrollPane2.setViewportView(amnTF);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Category");

        jScrollPane4.setViewportView(cateTF);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Description", "Amount", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table);

        jScrollPane5.setViewportView(dateTF);

        delete.setBackground(new java.awt.Color(255, 153, 51));
        delete.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        delete.setText("DELETE");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        balance.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        balance.setText("BALANCE: 0.0");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("INCOME TRACKER");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jScrollPane3)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(518, 518, 518)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(add)
                        .addGap(18, 18, 18)
                        .addComponent(delete)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(394, 394, 394)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(balance)))
                .addContainerGap())
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane4))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(balance)
                        .addGap(0, 90, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    // NAG RERESET NG VALUES SINCE NAKA RANDOM
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FormManager.showForm(new DashboardForm());  //TO CHANGE PA
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        // TODO add your handling code here:                               
    // Get input values from input fields
    String date = dateTF.getText();
    String description = desTF.getText();
    String amountStr = amnTF.getText();
    String category = cateTF.getText();
    double amount;

    // Validate input values
    if (amountStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter the Category", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        amount = Double.parseDouble(amountStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validate the date format
    if (!isValidDateFormat(date)) {
        JOptionPane.showMessageDialog(this, "Invalid Date Format. Please use the format: Month/DD/YYYY", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Extract the month from the date
    String month = extractMonth(date);

    // Add the data to the table model
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.addRow(new Object[]{date, description, amount, category});

    // Update total amount
    totalAmount += amount;
    balance.setText("Balance: $ " + String.format("%.2f", calculateBalance()));

    // Clear input fields after adding the data
    desTF.setText("");
    amnTF.setText("");
    cateTF.setText("");

    // Store data in the database
    try {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
        String sql = "INSERT INTO incomes (date, description, amount, category, month) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, date);
        pstmt.setString(2, description);
        pstmt.setDouble(3, amount);
        pstmt.setString(4, category);
        pstmt.setString(5, month); // Store month value in the database
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error storing data in database", "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }
       // Method to extract the month from the date
        private String extractMonth(String date) {
        // Split the date string using the delimiter "/"
        String[] parts = date.split("/");

        // Extract the month part
        if (parts.length >= 1) {
            return parts[0]; // Assuming the month is the first part
        } else {
            return ""; // Return empty string if date format is invalid
                }

    }//GEN-LAST:event_addActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
         // Get the selected row index
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the values from the selected row
        String date = (String) table.getValueAt(selectedRow, 0);
        String description = (String) table.getValueAt(selectedRow, 1);
        double amount = (double) table.getValueAt(selectedRow, 2);
        String category = (String) table.getValueAt(selectedRow, 3);

        // Get the amount of the income being deleted
        double deletedAmount = (double) table.getValueAt(selectedRow, 2);

        // Remove the row from the table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(selectedRow);

        // Update total amount
        totalAmount -= deletedAmount;
        balance.setText("Balance: $ " + String.format("%.2f", calculateBalance()));

        // Delete the corresponding entry from the database
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            String sql = "DELETE FROM incomes WHERE date = ? AND description = ? AND amount = ? AND category = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, description);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, category);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Select the next row after deletion, if available
        int rowCount = table.getRowCount();
        if (rowCount > 0) {
            int nextRow = Math.min(selectedRow, rowCount - 1);
            table.setRowSelectionInterval(nextRow, nextRow);
        }
    
    }//GEN-LAST:event_deleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JTextPane amnTF;
    private javax.swing.JLabel balance;
    private javax.swing.JTextPane cateTF;
    private javax.swing.JTextPane dateTF;
    private javax.swing.JButton delete;
    private javax.swing.JTextPane desTF;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}




