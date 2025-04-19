package Admin;

import connectors.dbconnect;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;

public class ManageCustomers extends javax.swing.JPanel {
    private Connection conn;
    private DefaultTableModel model; // Make model a class variable
    private int adminId;

    public ManageCustomers() {
        initComponents();
        dbconnect dbc = new dbconnect();
        conn = dbc.getConnection();
        loadData();
        setupLiveSearch();
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    private void loadData() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the "Edit" column is editable (last column)
                return column == 7;
            }
        };
        model.addColumn("Member ID");
        model.addColumn("First Name");
        model.addColumn("Middle Name");
        model.addColumn("Last Name");
        model.addColumn("Email");
        model.addColumn("Phone Number");
        model.addColumn("Points");
        model.addColumn("Edit"); // Add Edit column

        try {
            String query = "SELECT membership_id, first_name, middle_name, last_name, email, IFNULL(phone_number, 'N/A') AS phone, IFNULL(points, 0) AS points, password FROM customers";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("membership_id");
                String firstName = rs.getString("first_name");
                String middleName = rs.getString("middle_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int points = rs.getInt("points");
                model.addRow(new Object[]{memberId, firstName, middleName, lastName, email, phone, points, "Edit"});
            }

            rs.close();
            pst.close();

            Customers.setModel(model);

            // Center align all cells except the Edit button
            javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            for (int i = 0; i < Customers.getColumnCount() - 1; i++) {
                Customers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Set button renderer/editor for the Edit column
            Customers.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
            Customers.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(Customers));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupLiveSearch() {
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
    }

    private void filterTable() {
        String text = search.getText().trim().toLowerCase();
        DefaultTableModel filteredModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        // Add columns
        for (int i = 0; i < model.getColumnCount(); i++) {
            filteredModel.addColumn(model.getColumnName(i));
        }
        // Filter rows
        for (int i = 0; i < model.getRowCount(); i++) {
            String memberId = String.valueOf(model.getValueAt(i, 0)).toLowerCase();
            String firstName = String.valueOf(model.getValueAt(i, 1)).toLowerCase();
            String middleName = String.valueOf(model.getValueAt(i, 2)).toLowerCase();
            String lastName = String.valueOf(model.getValueAt(i, 3)).toLowerCase();
            String email = String.valueOf(model.getValueAt(i, 4)).toLowerCase();
            String fullName = (firstName + " " + middleName + " " + lastName).trim();

            if (memberId.contains(text) ||
                firstName.contains(text) ||
                middleName.contains(text) ||
                lastName.contains(text) ||
                fullName.contains(text) ||
                email.contains(text)) {
                Object[] rowData = new Object[model.getColumnCount()];
                for (int j = 0; j < model.getColumnCount(); j++) {
                    rowData[j] = model.getValueAt(i, j);
                }
                filteredModel.addRow(rowData);
            }
        }
        Customers.setModel(filteredModel);

        // Center align all cells except the Edit button
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0; i < Customers.getColumnCount() - 1; i++) {
            Customers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // Set button renderer/editor for the Edit column
        Customers.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        Customers.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(Customers));
    }

    // ButtonRenderer for JTable
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Edit" : value.toString());
            return this;
        }
    }

    // ButtonEditor for JTable
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        private JTable table;

        public ButtonEditor(JTable table) {
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    editRow(row);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "Edit" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    private void editRow(int row) {
        String memberId = (String) Customers.getValueAt(row, 0);
        String firstName = (String) Customers.getValueAt(row, 1);
        String middleName = (String) Customers.getValueAt(row, 2);
        String lastName = (String) Customers.getValueAt(row, 3);
        String email = (String) Customers.getValueAt(row, 4);
        String phone = (String) Customers.getValueAt(row, 5);
        int points = (int) Customers.getValueAt(row, 6);

        JTextField memberIdField = new JTextField(memberId);
        memberIdField.setEditable(false);
        JTextField firstNameField = new JTextField(firstName);
        JTextField middleNameField = new JTextField(middleName);
        JTextField lastNameField = new JTextField(lastName);
        JTextField emailField = new JTextField(email);
        emailField.setEditable(false);
        JTextField phoneField = new JTextField(phone);
        JTextField pointsField = new JTextField(String.valueOf(points));
        pointsField.setEditable(false);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
        panel.add(new JLabel("Member ID:"));
        panel.add(memberIdField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Middle Name:"));
        panel.add(middleNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Points:"));
        panel.add(pointsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newFirstName = firstNameField.getText().trim();
            String newMiddleName = middleNameField.getText().trim();
            String newLastName = lastNameField.getText().trim();
            String newPhone = phoneField.getText().trim();
            int newPoints;
            try {
                newPoints = Integer.parseInt(pointsField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Points must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this customer?", "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String updateQuery = "UPDATE customers SET first_name=?, middle_name=?, last_name=?, phone_number=?, points=? WHERE membership_id=?";
                    PreparedStatement pst = conn.prepareStatement(updateQuery);
                    pst.setString(1, newFirstName);
                    pst.setString(2, newMiddleName.isEmpty() ? null : newMiddleName);
                    pst.setString(3, newLastName.isEmpty() ? null : newLastName);
                    pst.setString(4, newPhone);
                    pst.setInt(5, newPoints);
                    pst.setString(6, memberId);

                    int updated = pst.executeUpdate();
                    pst.close();

                    if (updated > 0) {
                        Customers.setValueAt(newFirstName, row, 1);
                        Customers.setValueAt(newMiddleName, row, 2);
                        Customers.setValueAt(newLastName, row, 3);
                        Customers.setValueAt(newPhone, row, 5);
                        Customers.setValueAt(newPoints, row, 6);

                        // Log the update action
                        logAuditAction(adminId, "UPDATE", "customer", memberId, "Customer updated: " + newFirstName + " " + newLastName);

                        JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Update failed. Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void logAuditAction(int adminId, String actionType, String tableName, String recordId, String description) {
        try {
            String logQuery = "INSERT INTO audit_log (admin_id, action_type, table_name, record_id, action_description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement logPst = conn.prepareStatement(logQuery);
            logPst.setInt(1, adminId);
            logPst.setString(2, actionType);
            logPst.setString(3, tableName);
            logPst.setString(4, recordId);
            logPst.setString(5, description);
            logPst.executeUpdate();
            logPst.close();
        } catch (SQLException logEx) {
            logEx.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Customers = new javax.swing.JTable();
        DeleteCustomer = new javax.swing.JButton();
        alert = new javax.swing.JButton();
        search = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Customers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Email", "Phone Number", "Password", "Points"
            }
        ));
        jScrollPane1.setViewportView(Customers);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 880, 430));

        DeleteCustomer.setText("DELETE");
        DeleteCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteCustomerActionPerformed(evt);
            }
        });
        add(DeleteCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 90, -1));

        alert.setText("ALERT");
        alert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alertActionPerformed(evt);
            }
        });
        add(alert, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 100, -1));
        add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 230, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("SEARCH");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void DeleteCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteCustomerActionPerformed
        int selectedRow = Customers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String memberId = Customers.getValueAt(selectedRow, 0).toString();

        String reason = JOptionPane.showInputDialog(this, "Please enter the reason for deleting this customer:", "Reason Required", JOptionPane.QUESTION_MESSAGE);
        if (reason == null || reason.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Deletion cancelled. Reason is required.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this customer?\nMember ID: " + memberId + "\nReason: " + reason,
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String deleteQuery = "DELETE FROM customers WHERE membership_id = ?";
                PreparedStatement pst = conn.prepareStatement(deleteQuery);
                pst.setString(1, memberId);
                int deleted = pst.executeUpdate();
                pst.close();

                if (deleted > 0) {
                    ((DefaultTableModel) Customers.getModel()).removeRow(selectedRow);
                    // Log the delete action
                    logAuditAction(adminId, "DELETE", "customer", memberId, "Customer deleted. Reason: " + reason);
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully.\nReason: " + reason);
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed. Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_DeleteCustomerActionPerformed

    private void alertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alertActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Customers;
    private javax.swing.JButton DeleteCustomer;
    private javax.swing.JButton alert;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField search;
    // End of variables declaration//GEN-END:variables
}
