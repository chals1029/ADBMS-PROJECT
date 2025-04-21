package Front;

import LoginUser.UserLogin;
import connectors.dbconnect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableCellEditor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JPasswordField;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.Dimension;
import java.awt.Desktop;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.event.ChangeListener;
import javax.swing.JProgressBar;

// ButtonRenderer for JTable
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Book Now" : value.toString());
        setBackground(new Color(0, 102, 204));
        setForeground(Color.WHITE);
        return this;
    }
}

// ButtonEditor for JTable
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JTextField checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Book Now" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            // Handle booking logic here
            JOptionPane.showMessageDialog(button, "Booking for flight: " + 
                ((JTable)getComponent().getParent()).getValueAt(((JTable)getComponent().getParent()).getEditingRow(), 0));
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

public class UserFront extends javax.swing.JFrame {

    private UserLogin currentUser;
    private Connection conn;
    private TableRowSorter<DefaultTableModel> sorter;

    private Timer slideshowTimer;
    private Timer animationTimer;
    private int currentImageIndex = 0;
    private final String[] imageNames = {"promotion.jpg", "Pal.png", "palpromotion.jpg"};
    private ImageIcon currentIcon;
    private ImageIcon nextIcon;
    private int animationX = 0;
    private boolean animating = false;
    private final int ANIMATION_SPEED = 10;
    private final int ANIMATION_DELAY = 20; // milliseconds between animation frames

    private final Map<String, String> destinationAirports = new HashMap<>();
    private final Map<String, Double> baseFares = new HashMap<>();

    public UserFront() {
        initComponents();
        currentUser = UserLogin.getCurrentUser();
        dbconnect dbc = new dbconnect();
        conn = dbc.getConnection();
        loadFlights();
        loadDestinationsToComboBox();
        setupLiveSearch();
        fadeIn();
        setupSlideshow();   
        initializeDestinationData();
    }

    private void initializeDestinationData() {
        // Map destinations to their main airports
        destinationAirports.put("Seoul", "Incheon International Airport");
        destinationAirports.put("Tokyo", "Narita International Airport");
        destinationAirports.put("Boracay", "Caticlan Airport");
        destinationAirports.put("Hong Kong", "Hong Kong International Airport");
        
        // Base fares for each destination (in PHP)
        baseFares.put("Seoul", 18500.00);
        baseFares.put("Tokyo", 22000.00);
        baseFares.put("Boracay", 5500.00);
        baseFares.put("Hong Kong", 12500.00);
    }

    private Timer fadeTimer;

    private void fadeIn() {
        setOpacity(0f);

        int fadeDuration = 500;
        fadeTimer = new Timer(50, new ActionListener() {
            private float opacity = 0f;
            private final float increment = 50f / fadeDuration;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += increment;
                if (opacity >= 1f) {
                    opacity = 1f;
                    fadeTimer.stop();
                }
                setOpacity(opacity);
            }
        });
        fadeTimer.start();
    }

    private void loadFlights() {
        try {
            System.out.println("Loading flights...");
            String sql = "SELECT f.flightNumber, " +
                         "p.plane_name, " +
                         "f.origin, f.origin_country, " +
                         "f.destination, f.destination_country, " +
                         "f.departureTime, f.arrivalTime, f.status " +
                         "FROM flights f " +
                         "LEFT JOIN planes p ON f.plane_id = p.plane_id";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            System.out.println("Query executed, checking for data...");
            
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Flight Number", "Plane Name", "Origin", "Origin Country", "Destination", "Destination Country", "Departure Time", "Arrival Time", "Status", "Action"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 9;
                }
            };

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                System.out.println("Found flight: " + rs.getString("flightNumber"));
                model.addRow(new Object[]{
                    rs.getString("flightNumber"),
                    rs.getString("plane_name"),
                    rs.getString("origin"),
                    rs.getString("origin_country"),
                    rs.getString("destination"),
                    rs.getString("destination_country"),
                    rs.getString("departureTime"),
                    rs.getString("arrivalTime"),
                    rs.getString("status"),
                    "Book Now"
                });
            }
            
            System.out.println("Added " + rowCount + " rows to the model");
            flights.setModel(model);
            
            if (flights.getColumnCount() > 9) {
                flights.getColumn("Action").setCellRenderer(new ButtonRenderer());
                flights.getColumn("Action").setCellEditor(new ButtonEditor(new javax.swing.JTextField()));
                System.out.println("Set button renderer and editor");
            } else {
                System.out.println("Table has only " + flights.getColumnCount() + " columns");
            }

            // Update sorter with new model
            sorter = new TableRowSorter<>((DefaultTableModel) flights.getModel());
            flights.setRowSorter(sorter);
            
            // Set up table display properties for centered data and horizontal scrolling
            setupTableDisplay(flights);
            
            // Apply modern table style
            applyModernTableStyle(flights);

        } catch (Exception e) {
            System.out.println("Error loading flights: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableDisplay(JTable table) {
        // Center align all cell content (except the action button column)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            // Skip the action column
            if (i != 9) { // Assuming column 9 is the "Action" column
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Enable horizontal scrolling
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set preferred column widths
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        // Configure the scroll pane
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void loadDestinationsToComboBox() {
        ActionListener[] listeners = SelectCountry.getActionListeners();
        for (ActionListener listener : listeners) {
            SelectCountry.removeActionListener(listener);
        }

        SelectCountry.removeAllItems();
        SelectCountry.addItem("-- Select Destination Country --"); 
        String[] countries = {
            "Brunei", "Cambodia", "China", "Hong Kong", "Indonesia", "Japan", "Laos", "Macau",
            "Malaysia", "Mongolia", "Myanmar", "North Korea", "Philippines", "Singapore",
            "South Korea", "Taiwan", "Thailand", "Timor-Leste", "Vietnam"
        };
        for (String country : countries) {
            SelectCountry.addItem(country);
        }

        for (ActionListener listener : listeners) {
            SelectCountry.addActionListener(listener);
        }
    }

    private void loadFlightsByDestination(String destinationCountry) {
        try {
            String sql = "SELECT f.flightNumber, " +
                         "p.plane_name, " +
                         "f.origin, f.origin_country, " +
                         "f.destination, f.destination_country, " +
                         "f.departureTime, f.arrivalTime, f.status " +
                         "FROM flights f " +
                         "LEFT JOIN planes p ON f.plane_id = p.plane_id " +
                         "WHERE f.destination_country = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, destinationCountry);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Flight Number", "Plane Name", "Origin", "Origin Country", "Destination", "Destination Country", "Departure Time", "Arrival Time", "Status", "Action"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 9;
                }
            };

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("flightNumber"),
                    rs.getString("plane_name"),
                    rs.getString("origin"),
                    rs.getString("origin_country"),
                    rs.getString("destination"),
                    rs.getString("destination_country"),
                    rs.getString("departureTime"),
                    rs.getString("arrivalTime"),
                    rs.getString("status"),
                    "Book Now"
                });
            }
            flights.setModel(model);
            flights.getColumn("Action").setCellRenderer(new ButtonRenderer());
            flights.getColumn("Action").setCellEditor(new ButtonEditor(new javax.swing.JTextField()));

            // Update sorter with new model
            sorter = new TableRowSorter<>((DefaultTableModel) flights.getModel());
            flights.setRowSorter(sorter);
            
            // Add this line to maintain table formatting after filtering
            setupTableDisplay(flights);
            
            // Apply modern table style
            applyModernTableStyle(flights);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLiveSearch() {
        sorter = new TableRowSorter<>((DefaultTableModel) flights.getModel());
        flights.setRowSorter(sorter);
        searchdestination.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
    }

    private void filterTable() {
        String text = searchdestination.getText();
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null); 
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 5));
        }
    }

    

    public String getCurrentUserEmail() {
        return currentUser != null ? currentUser.getEmail() : "";
    }
    
    public String getCurrentUserMembershipId() {
        return currentUser != null ? currentUser.getMembershipId() : "";
    }

    private void setupSlideshow() {
        // Reset the slideshow panel
        slideshow.removeAll();
        slideshow.setLayout(null); // Use null layout for custom drawing
        slideshow.setBackground(Color.WHITE);
        slideshow.setPreferredSize(new Dimension(623, 150));
        slideshow.setMinimumSize(new Dimension(623, 150));
        
        // Preload first image
        currentIcon = loadResizedImage(imageNames[currentImageIndex]);
        
        // Configure timer for slideshow
        if (slideshowTimer != null) {
            slideshowTimer.stop();
        }
        slideshowTimer = new Timer(4000, e -> {
            if (!animating) {
                startSlideAnimation();
            }
        });
        slideshowTimer.start();
        
        // Create a custom panel for painting images directly in the slideshow panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                
                if (animating) {
                    // Draw current image sliding out
                    if (currentIcon != null) {
                        g.drawImage(currentIcon.getImage(), animationX, 0, width, height, null);
                    }
                    
                    // Draw next image sliding in
                    if (nextIcon != null) {
                        g.drawImage(nextIcon.getImage(), width + animationX, 0, width, height, null);
                    }
                } else {
                    // Normal display (not animating)
                    if (currentIcon != null) {
                        g.drawImage(currentIcon.getImage(), 0, 0, width, height, null);
                    } else {
                        // Draw a placeholder if image didn't load
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(0, 0, width, height);
                        g.setColor(Color.BLACK);
                        g.drawString("Image not found", width/2 - 50, height/2);
                    }
                }
            }
        };
        
        // Configure the image panel to fill the slideshow
        imagePanel.setSize(623, 150);
        imagePanel.setPreferredSize(new Dimension(623, 150));
        imagePanel.setLayout(null);
        
        // Add the image panel to the slideshow
        slideshow.setLayout(new BorderLayout());
        slideshow.add(imagePanel, BorderLayout.CENTER);
        
        // This is the key fix - we're replacing the slideshow reference with our image panel
        // so the animations will properly target this panel
        slideshow = imagePanel;
        
       
    }

    private ImageIcon loadResizedImage(String imageName) {
        try {
            System.out.println("Loading image: " + imageName);
            
            // Try multiple locations to find the image
            ImageIcon icon = null;
            
            // First try with resources
            java.net.URL resourceUrl = getClass().getResource("/" + imageName);
            if (resourceUrl != null) {
                icon = new ImageIcon(resourceUrl);
                System.out.println("Found image in resources: " + imageName);
            }
            
            // If not found, try direct file path
            if (icon == null || icon.getIconWidth() <= 0) {
                String[] possiblePaths = {
                    "src/main/resources/" + imageName,
                    "resources/" + imageName,
                    "src/resources/" + imageName,
                    "images/" + imageName,
                    "src/main/resources/images/" + imageName,
                    imageName
                };
                
                for (String path : possiblePaths) {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        icon = new ImageIcon(file.getAbsolutePath());
                        System.out.println("Found image at: " + file.getAbsolutePath());
                        break;
                    }
                }
            }
            
            // Check if the image was loaded successfully
            if (icon == null || icon.getIconWidth() <= 0) {
                System.err.println("Failed to load image: " + imageName);
                return null;
            }
            
            // Resize the image to fit the slideshow
            java.awt.Image img = icon.getImage();
            java.awt.Image resizedImg = img.getScaledInstance(623, 150, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imageName);
            e.printStackTrace();
            return null;
        }
    }

    private void startSlideAnimation() {
        // Prepare next image
        int nextIndex = (currentImageIndex + 1) % imageNames.length;
        nextIcon = loadResizedImage(imageNames[nextIndex]);
        
       
        animationX = 0;
        animating = true;
        
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            
            animationX -= ANIMATION_SPEED;
            
            // Check if animation complete
            if (Math.abs(animationX) >= slideshow.getWidth()) {
                // Animation complete - update current image
                currentImageIndex = nextIndex;
                currentIcon = nextIcon;
                animating = false;
                ((Timer)e.getSource()).stop();
            }
            
            // Repaint with new positions
            slideshow.repaint();
        });
        
        animationTimer.start();
    }

    private void addPaintComponent(JPanel panel, java.util.function.Consumer<Graphics> painter) {
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                panel.repaint();
            }
        });
        
        panel.addHierarchyListener(e -> panel.repaint());
        
        // Override the paintComponent method
        javax.swing.plaf.ComponentUI originalUI = UIManager.getLookAndFeel().getDefaults().getUI(panel);
        javax.swing.plaf.PanelUI customUI = new javax.swing.plaf.PanelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                painter.accept(g);
            }
        };
        panel.setUI(customUI);
    }

    private void applyRoundedButtonStyle(JButton button, Color bgColor, Color textColor) {
        // Keep existing button styling
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Add hover effects (keep existing)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        // THIS IS THE KEY CHANGE - Create and apply a rounded border
        int radius = 15; // Controls how rounded the corners are
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(radius, bgColor),
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding
        ));
    }

    // Add this inner class to your UserFront class
    private class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

  

    private void applyModernTableStyle(JTable table) {
        // Set row height
        table.setRowHeight(32);
        
        // Remove grid lines
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom header renderer
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(0, 102, 204));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Alternating row colors and proper padding
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                // Padding
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Alternating row colors
                if (row % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(new Color(245, 245, 250));
                }
                
                // Selection color
                if (isSelected) {
                    label.setBackground(new Color(232, 242, 254));
                    label.setForeground(Color.BLACK);
                }
                
                // Center alignment for all columns except possibly the action column
                label.setHorizontalAlignment(column == 9 ? JLabel.CENTER : JLabel.CENTER);
                
                return label;
            }
        });
        
        // Set selection color
        table.setSelectionBackground(new Color(232, 242, 254));
        table.setSelectionForeground(Color.BLACK);
        
        // Add some subtle styling to the scroll pane
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new ModernScroll.ScrollPaneWin11();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        flights = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        searchdestination = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        booktotokyo = new javax.swing.JButton();
        booktoseoul = new javax.swing.JButton();
        booktoboracay = new javax.swing.JButton();
        booktohongkong1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        hongkong = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        seoul = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tokyo = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        boracay = new javax.swing.JLabel();
        background = new javax.swing.JPanel();
        slideshow = new javax.swing.JPanel();
        PS = new javax.swing.JPanel();
        SH = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        PS1 = new javax.swing.JPanel();
        ViewUrAccount = new javax.swing.JButton();
        mytransactionhistory = new javax.swing.JButton();
        ViewRealtime = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        SelectCountry = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(null);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(1000, 600));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(500, 600));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 200));
        jScrollPane1.setViewportView(jPanel2);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setMinimumSize(new java.awt.Dimension(1000, 6000));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 1000));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 204));
        jPanel3.setForeground(new java.awt.Color(0, 0, 204));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("© 2025 Philippine Airlines. All rights reserved.");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshot 2025-04-21 142409.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 930, 1020, 60));

        flights.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight Number", "Plane Name", "Origin", "Origin Country", "Destination", "Destination Country", "Departure Time", "Arrival Time", "Status"
            }
        ));
        jScrollPane2.setViewportView(flights);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 710, 900, 220));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Discover the best flights with PAL");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 680, -1, 30));
        jPanel2.add(searchdestination, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 680, 190, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Search");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 660, 60, 70));

        booktotokyo.setBackground(new java.awt.Color(0, 0, 255));
        booktotokyo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        booktotokyo.setForeground(new java.awt.Color(255, 255, 255));
        booktotokyo.setText("BOOK NOW");
        booktotokyo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                booktotokyoActionPerformed(evt);
            }
        });
        jPanel2.add(booktotokyo, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 390, 120, -1));

        booktoseoul.setBackground(new java.awt.Color(0, 0, 255));
        booktoseoul.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        booktoseoul.setForeground(new java.awt.Color(255, 255, 255));
        booktoseoul.setText("BOOK NOW");
        booktoseoul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                booktoseoulActionPerformed(evt);
            }
        });
        jPanel2.add(booktoseoul, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 120, -1));

        booktoboracay.setBackground(new java.awt.Color(0, 0, 255));
        booktoboracay.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        booktoboracay.setForeground(new java.awt.Color(255, 255, 255));
        booktoboracay.setText("BOOK NOW");
        booktoboracay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                booktoboracayActionPerformed(evt);
            }
        });
        jPanel2.add(booktoboracay, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 580, 120, -1));

        booktohongkong1.setBackground(new java.awt.Color(0, 0, 255));
        booktohongkong1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        booktohongkong1.setForeground(new java.awt.Color(255, 255, 255));
        booktohongkong1.setText("BOOK NOW");
        booktohongkong1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                booktohongkong1ActionPerformed(evt);
            }
        });
        jPanel2.add(booktohongkong1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 580, 120, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Unwind at the world-famous island paradise\n");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 520, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Witness the country's colorful streets");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Experience outdoor adventures");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 530, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("HONGKONG");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 440, 240, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Delight in the city's sights and sounds");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 330, -1, -1));

        hongkong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hongkong (2)-modified.png"))); // NOI18N
        jPanel2.add(hongkong, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, 270, 180));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("SEOUL");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, -1, -1));

        seoul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Seoul (3)-modified.png"))); // NOI18N
        jPanel2.add(seoul, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 270, 180));

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("TOKYO");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 250, -1, 60));

        tokyo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tokyo (3)-modified.png"))); // NOI18N
        jPanel2.add(tokyo, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 250, 270, 180));

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("BORACAY");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 440, -1, -1));

        boracay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Boracay (2)-modified.png"))); // NOI18N
        jPanel2.add(boracay, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 270, 180));

        background.setBackground(new java.awt.Color(255, 255, 255));
        background.setForeground(new java.awt.Color(0, 0, 0));
        background.setPreferredSize(new java.awt.Dimension(980, 150));

        slideshow.setBackground(new java.awt.Color(255, 255, 255));
        slideshow.setForeground(new java.awt.Color(255, 255, 255));
        slideshow.setMinimumSize(new java.awt.Dimension(623, 150));

        javax.swing.GroupLayout slideshowLayout = new javax.swing.GroupLayout(slideshow);
        slideshow.setLayout(slideshowLayout);
        slideshowLayout.setHorizontalGroup(
            slideshowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 623, Short.MAX_VALUE)
        );
        slideshowLayout.setVerticalGroup(
            slideshowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        PS.setBackground(new java.awt.Color(0, 0, 204));
        PS.setPreferredSize(new java.awt.Dimension(184, 150));
        PS.setLayout(null);

        SH.setBackground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout SHLayout = new javax.swing.GroupLayout(SH);
        SH.setLayout(SHLayout);
        SHLayout.setHorizontalGroup(
            SHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 184, Short.MAX_VALUE)
        );
        SHLayout.setVerticalGroup(
            SHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addComponent(PS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 622, Short.MAX_VALUE)
                .addComponent(SH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(backgroundLayout.createSequentialGroup()
                    .addGap(0, 183, Short.MAX_VALUE)
                    .addComponent(slideshow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 184, Short.MAX_VALUE)))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(backgroundLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(slideshow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel2.add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 990, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PalLogo-removebg-preview (1).png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -20, -1, -1));

        PS1.setBackground(new java.awt.Color(0, 0, 204));

        ViewUrAccount.setBackground(new java.awt.Color(0, 0, 204));
        ViewUrAccount.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ViewUrAccount.setForeground(new java.awt.Color(255, 255, 255));
        ViewUrAccount.setText("MY ACCOUNT");
        ViewUrAccount.setBorder(null);
        ViewUrAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewUrAccountActionPerformed(evt);
            }
        });

        mytransactionhistory.setBackground(new java.awt.Color(0, 0, 204));
        mytransactionhistory.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        mytransactionhistory.setForeground(new java.awt.Color(255, 255, 255));
        mytransactionhistory.setText("MY TRANSACTION HISTORY");
        mytransactionhistory.setBorder(null);

        ViewRealtime.setBackground(new java.awt.Color(0, 0, 204));
        ViewRealtime.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ViewRealtime.setForeground(new java.awt.Color(255, 255, 255));
        ViewRealtime.setText("VIEW REALTIME FLIGHT");
        ViewRealtime.setBorder(null);
        ViewRealtime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewRealtimeActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("MY BOOKING HISTORY");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("EXIT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PS1Layout = new javax.swing.GroupLayout(PS1);
        PS1.setLayout(PS1Layout);
        PS1Layout.setHorizontalGroup(
            PS1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PS1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(ViewUrAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mytransactionhistory, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(ViewRealtime, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(23, 23, 23))
        );
        PS1Layout.setVerticalGroup(
            PS1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PS1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PS1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewUrAccount)
                    .addComponent(mytransactionhistory)
                    .addComponent(jButton3)
                    .addComponent(ViewRealtime)
                    .addComponent(jButton1))
                .addContainerGap(113, Short.MAX_VALUE))
        );

        jPanel2.add(PS1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, -1));

        SelectCountry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Brunei", "Cambodia", "China", "Hong Kong", "Indonesia", "Japan", "Laos", "Macau", "Malaysia", "Mongolia", "Myanmar", "North Korea", "Philippines", "Singapore", "South Korea", "Taiwan", "Thailand", "Timor-Leste", "Vietnam" }));
        SelectCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectCountryActionPerformed(evt);
            }
        });
        jPanel2.add(SelectCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 680, -1, 30));

        jScrollPane1.setViewportView(jPanel2);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 1000, 630);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 610));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void booktoseoulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booktoseoulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_booktoseoulActionPerformed

    private void booktotokyoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booktotokyoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_booktotokyoActionPerformed

    private void booktoboracayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booktoboracayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_booktoboracayActionPerformed

    private void booktohongkong1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booktohongkong1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_booktohongkong1ActionPerformed

    private void ViewUrAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewUrAccountActionPerformed
          // TODO add your handling code here:
         if (currentUser != null) {
            showUserAccountInfo();
        } else {
            JOptionPane.showMessageDialog(this, 
                "You are not logged in. Please log in to view your account.",
                "Account Information", 
                JOptionPane.INFORMATION_MESSAGE);
         }
    }//GEN-LAST:event_ViewUrAccountActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void ViewRealtimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewRealtimeActionPerformed
           // TODO add your handling code here:
         try {
            Desktop.getDesktop().browse(new URI("https://www.flightaware.com/live/flex_bigmap.rvt?search=-airline%20PAL&time=1745120220&key=de54302b4198f8763d6193c041aa98c5ae5622da&title=Airborne%20Philippine%20Air%20Lines%20&quot;Philippine&quot;%20(PAL)%20Aircraft"));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(this,
                "Unable to open the web browser: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_ViewRealtimeActionPerformed

    private void SelectCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectCountryActionPerformed
        // TODO add your handling code here:
           String selectedCountry = (String) SelectCountry.getSelectedItem();
        if (selectedCountry != null && !selectedCountry.equals("-- Select Destination Country --")) {
            loadFlightsByDestination(selectedCountry);
        } else {
            // Show all flights if default option is selected
            loadFlights();
        }
    }
        private void showUserAccountInfo() {
        // Create a panel with a vertical BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add user information
        JLabel nameLabel = new JLabel("<html><b>Name:</b> " + currentUser.getFirstName() + " " + 
                                      (currentUser.getMiddleName() != null ? currentUser.getMiddleName() + " " : "") + 
                                      currentUser.getLastName() + "</html>");
        JLabel emailLabel = new JLabel("<html><b>Email:</b> " + currentUser.getEmail() + "</html>");
        JLabel membershipLabel = new JLabel("<html><b>Membership ID:</b> " + currentUser.getMembershipId() + "</html>");
        JLabel phoneLabel = new JLabel("<html><b>Phone Number:</b> " + currentUser.getPhoneNumber() + "</html>");
        JLabel pointsLabel = new JLabel("<html><b>Points:</b> " + currentUser.getPoints() + "</html>");
        
        // Add password with masked characters
        String maskedPassword = "*".repeat(currentUser.getPassword().length());
        JLabel passwordLabel = new JLabel("<html><b>Password:</b> " + maskedPassword + "</html>");
        
        // Add components to panel with spacing
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(membershipLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(phoneLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pointsLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(passwordLabel);
        
        // Add edit button at the bottom
        JButton editButton = new JButton("Edit Information");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        
        // Add action to the edit button
        editButton.addActionListener(e -> {
            openEditDialog();
        });
        
        // Show dialog with user information
        JOptionPane.showMessageDialog(this, 
            panel, 
            "Account Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void openEditDialog() {
        // Create dialog components
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create input fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        
        // Create fields with current values
        JTextField firstNameField = new JTextField(currentUser.getFirstName(), 20);
        JTextField middleNameField = new JTextField(currentUser.getMiddleName() != null ? currentUser.getMiddleName() : "", 20);
        JTextField lastNameField = new JTextField(currentUser.getLastName(), 20);
        JTextField phoneField = new JTextField(currentUser.getPhoneNumber(), 20); // Added phone field
        JPasswordField passwordField = new JPasswordField(currentUser.getPassword(), 20);
        JCheckBox showPasswordBox = new JCheckBox("Show Password");
        
        // Non-editable fields for reference
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        
        JTextField membershipField = new JTextField(currentUser.getMembershipId(), 20);
        membershipField.setEditable(false);
        membershipField.setBackground(new Color(240, 240, 240));
        
        // Add fields with labels to panel
        addLabeledField(fieldsPanel, "First Name:", firstNameField);
        addLabeledField(fieldsPanel, "Middle Name:", middleNameField);
        addLabeledField(fieldsPanel, "Last Name:", lastNameField);
        addLabeledField(fieldsPanel, "Phone Number:", phoneField); // Added phone field
        addLabeledField(fieldsPanel, "Password:", passwordField);
        fieldsPanel.add(showPasswordBox);
        fieldsPanel.add(Box.createVerticalStrut(10));
        
        // Add non-editable fields
        addLabeledField(fieldsPanel, "Email (non-editable):", emailField);
        addLabeledField(fieldsPanel, "Membership ID (non-editable):", membershipField);
        
        // Show/hide password functionality
        showPasswordBox.addActionListener(e -> {
            if (showPasswordBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        
        // Create buttons
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add components to main panel
        panel.add(fieldsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create dialog
        JDialog dialog = new JDialog(this, "Edit Account Information", true);
        dialog.setContentPane(panel);
        
        // Button actions
        saveButton.addActionListener(e -> {
            updateUserInfo(
                firstNameField.getText(),
                middleNameField.getText(),
                lastNameField.getText(),
                phoneField.getText(), // Added phone number
                new String(passwordField.getPassword())
            );
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        // Show dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField field) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(180, 25));
        wrapper.add(label);
        wrapper.add(field);
        panel.add(wrapper);
        panel.add(Box.createVerticalStrut(5));
    }

    private void updateUserInfo(String firstName, String middleName, String lastName, String phoneNumber, String password) {
        try {
            // Update in database
            PreparedStatement pst = conn.prepareStatement(
                "UPDATE users SET firstName = ?, middleName = ?, lastName = ?, phoneNumber = ?, password = ? WHERE membershipId = ?"
            );
            pst.setString(1, firstName);
            pst.setString(2, middleName);
            pst.setString(3, lastName);
            pst.setString(4, phoneNumber); // Added phone number
            pst.setString(5, password);
            pst.setString(6, currentUser.getMembershipId());
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                // Update current user object
                currentUser.setFirstName(firstName);
                currentUser.setMiddleName(middleName);
                currentUser.setLastName(lastName);
                currentUser.setPhoneNumber(phoneNumber); // Added phone number
                currentUser.setPassword(password);
                
                // Update displayed info
               
                
                JOptionPane.showMessageDialog(this, 
                    "Your information has been successfully updated.",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to update your information. Please try again.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "An error occurred: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    
    }//GEN-LAST:event_SelectCountryActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UserFront.class.getName()).log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new UserFront().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PS;
    private javax.swing.JPanel PS1;
    private javax.swing.JPanel SH;
    private javax.swing.JComboBox<String> SelectCountry;
    private javax.swing.JButton ViewRealtime;
    private javax.swing.JButton ViewUrAccount;
    private javax.swing.JPanel background;
    private javax.swing.JButton booktoboracay;
    private javax.swing.JButton booktohongkong1;
    private javax.swing.JButton booktoseoul;
    private javax.swing.JButton booktotokyo;
    private javax.swing.JLabel boracay;
    private javax.swing.JTable flights;
    private javax.swing.JLabel hongkong;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton mytransactionhistory;
    private javax.swing.JTextField searchdestination;
    private javax.swing.JLabel seoul;
    private javax.swing.JPanel slideshow;
    private javax.swing.JLabel tokyo;
    // End of variables declaration//GEN-END:variables
}
