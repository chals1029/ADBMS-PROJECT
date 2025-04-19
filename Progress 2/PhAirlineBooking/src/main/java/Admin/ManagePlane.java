package Admin;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import connectors.dbconnect;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;

public class ManagePlane extends javax.swing.JPanel {
    private Connection conn;
    private DefaultTableModel model;

    public ManagePlane() {
        initComponents();
        dbconnect dbc = new dbconnect();
        conn = dbc.getConnection();
        loadData();
        setupEditButton();
        setupLiveSearch(); // <-- Add this line
    }

    private void loadData() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };
        model.setColumnIdentifiers(new String[]{
            "Plane", "Economy Capacity", "Business Capacity", "Vip Capacity",
            "Remaining Economy", "Remaining Business", "Remaining VIP",
            "Status", "Origin Location", "Destination"
        });

        try {
            String query = "SELECT plane_id, plane_name, economy_capacity, business_capacity, vip_capacity, " +
                    "remaining_economy_capacity, remaining_business_capacity, remaining_vip_capacity, status, " +
                    "origin_location, destination_location FROM planes";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("plane_name"),
                    rs.getInt("economy_capacity"),
                    rs.getInt("business_capacity"),
                    rs.getInt("vip_capacity"),
                    rs.getInt("remaining_economy_capacity"),
                    rs.getInt("remaining_business_capacity"),
                    rs.getInt("remaining_vip_capacity"),
                    rs.getString("status"),
                    rs.getString("origin_location"),
                    rs.getString("destination_location")
                });
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        jTable1.setModel(model);

        // Set status column as combo box
        String[] statusOptions = {"AVAILABLE", "ALREADY FULL", "MAINTENANCE"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        jTable1.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(statusCombo));

    }

   
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

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int editingRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    editRow(editingRow);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Edit" : value.toString();
            button.setText(label);
            isPushed = true;
            editingRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    private void editRow(int row) {
        
        String planeName = (String) jTable1.getValueAt(row, 0);
        int ecoCap = 0, busCap = 0, vipCap = 0, remEco = 0, remBus = 0, remVip = 0;
        String status = "", origin = "", destination = "";
        try {
            String query = "SELECT economy_capacity, business_capacity, vip_capacity, " +
                    "remaining_economy_capacity, remaining_business_capacity, remaining_vip_capacity, status, origin_location, destination_location " +
                    "FROM planes WHERE plane_name = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, planeName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ecoCap = rs.getInt("economy_capacity");
                busCap = rs.getInt("business_capacity");
                vipCap = rs.getInt("vip_capacity");
                remEco = rs.getInt("remaining_economy_capacity");
                remBus = rs.getInt("remaining_business_capacity");
                remVip = rs.getInt("remaining_vip_capacity");
                status = rs.getString("status");
                origin = rs.getString("origin_location");
                destination = rs.getString("destination_location");
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField ecoField = new JTextField(String.valueOf(ecoCap));
        JTextField busField = new JTextField(String.valueOf(busCap));
        JTextField vipField = new JTextField(String.valueOf(vipCap));
        JTextField remEcoField = new JTextField(String.valueOf(remEco));
        JTextField remBusField = new JTextField(String.valueOf(remBus));
        JTextField remVipField = new JTextField(String.valueOf(remVip));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Already Full", "Maintenance", "Available"});
        statusCombo.setSelectedItem(status);

        // Only set remaining capacities to 0 if user selects "Already Full" in the dialog
        statusCombo.addActionListener(e -> {
            if ("Already Full".equals(statusCombo.getSelectedItem())) {
                remEcoField.setText("0");
                remBusField.setText("0");
                remVipField.setText("0");
                remEcoField.setEditable(false);
                remBusField.setEditable(false);
                remVipField.setEditable(false);
            } else {
                remEcoField.setEditable(true);
                remBusField.setEditable(true);
                remVipField.setEditable(true);
            }
        });

        // Set fields editable state based on initial status
        if ("Already Full".equals(statusCombo.getSelectedItem())) {
            remEcoField.setEditable(false);
            remBusField.setEditable(false);
            remVipField.setEditable(false);
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Economy Capacity:"));
        panel.add(ecoField);
        panel.add(new JLabel("Business Capacity:"));
        panel.add(busField);
        panel.add(new JLabel("VIP Capacity:"));
        panel.add(vipField);
        panel.add(new JLabel("Remaining Economy:"));
        panel.add(remEcoField);
        panel.add(new JLabel("Remaining Business:"));
        panel.add(remBusField);
        panel.add(new JLabel("Remaining VIP:"));
        panel.add(remVipField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Plane", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newEco = Integer.parseInt(ecoField.getText().trim());
                int newBus = Integer.parseInt(busField.getText().trim());
                int newVip = Integer.parseInt(vipField.getText().trim());
                int newRemEco = Integer.parseInt(remEcoField.getText().trim());
                int newRemBus = Integer.parseInt(remBusField.getText().trim());
                int newRemVip = Integer.parseInt(remVipField.getText().trim());
                String displayStatus = (String) statusCombo.getSelectedItem();
                String newStatus;
                switch (displayStatus) {
                    case "Available": newStatus = "AVAILABLE"; break;
                    case "Already Full": newStatus = "ALREADY FULL"; break;
                    case "Maintenance": newStatus = "MAINTENANCE"; break;
                    default: newStatus = "AVAILABLE";
                }

                // If status is "Already Full", force remaining capacities to 0
                if ("ALREADY FULL".equals(newStatus)) {
                    newRemEco = 0;
                    newRemBus = 0;
                    newRemVip = 0;
                }

                String updateQuery = "UPDATE planes SET economy_capacity=?, business_capacity=?, vip_capacity=?, " +
                        "remaining_economy_capacity=?, remaining_business_capacity=?, remaining_vip_capacity=?, status=? WHERE plane_name=?";
                PreparedStatement pst = conn.prepareStatement(updateQuery);
                pst.setInt(1, newEco);
                pst.setInt(2, newBus);
                pst.setInt(3, newVip);
                pst.setInt(4, newRemEco);
                pst.setInt(5, newRemBus);
                pst.setInt(6, newRemVip);
                pst.setString(7, newStatus);
                pst.setString(8, planeName);
                int updated = pst.executeUpdate();
                pst.close();

                if (updated > 0) {
                    // Update the table model to reflect changes
                    jTable1.setValueAt(newEco, row, 1);
                    jTable1.setValueAt(newBus, row, 2);
                    jTable1.setValueAt(newVip, row, 3);
                    jTable1.setValueAt(newRemEco, row, 4);
                    jTable1.setValueAt(newRemBus, row, 5);
                    jTable1.setValueAt(newRemVip, row, 6);
                    jTable1.setValueAt(newStatus, row, 7);
                    JOptionPane.showMessageDialog(this, "Plane updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Database error: Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupEditButton() {
        // No-op if already handled in loadData
    }

    private void setupLiveSearch() {
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
    }

    private void filterTable() {
        String text = search.getText().trim().toLowerCase();
        DefaultTableModel filteredModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 1 && column <= 7;
            }
        };
        // Add columns
        for (int i = 0; i < model.getColumnCount(); i++) {
            filteredModel.addColumn(model.getColumnName(i));
        }
        // Filter rows by plane name
        for (int i = 0; i < model.getRowCount(); i++) {
            String planeName = String.valueOf(model.getValueAt(i, 0)).toLowerCase();
            if (planeName.contains(text)) {
                Object[] rowData = new Object[model.getColumnCount()];
                for (int j = 0; j < model.getColumnCount(); j++) {
                    rowData[j] = model.getValueAt(i, j);
                }
                filteredModel.addRow(rowData);
            }
        }
        jTable1.setModel(filteredModel);

        // Set status column as combo box
        String[] statusOptions = {"AVAILABLE", "ALREADY FULL", "MAINTENANCE"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        jTable1.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(statusCombo));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plane = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        search = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        update = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Plane", "Economy Capacity", "Business Capacity", "Vip Capacity", "Remaining Economy", "Remaining Business", "Remaining VIP", "Status", "Origin Location", "Destination"
            }
        ));
        plane.setViewportView(jTable1);

        add(plane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 940, 410));
        add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 60, 180, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Search");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 60, -1, 30));

        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 80, 40));

        update.setText("UPDATE");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        add(update, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 90, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a plane to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        editRow(selectedRow);
    }//GEN-LAST:event_updateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a plane to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String planeName = jTable1.getValueAt(selectedRow, 0).toString();
    
        String reason = JOptionPane.showInputDialog(this, "Please enter the reason for deleting this plane:", "Reason Required", JOptionPane.QUESTION_MESSAGE);
        if (reason == null || reason.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Deletion cancelled. Reason is required.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this plane?\nPlane: " + planeName + "\nReason: " + reason,
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String deleteQuery = "DELETE FROM planes WHERE plane_name = ?";
                PreparedStatement pst = conn.prepareStatement(deleteQuery);
                pst.setString(1, planeName);
                int deleted = pst.executeUpdate();
                pst.close();
    
                if (deleted > 0) {
                    ((DefaultTableModel) jTable1.getModel()).removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Plane deleted successfully.\nReason: " + reason);
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed. Plane not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane plane;
    private javax.swing.JTextField search;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
