package Admin;

import Front.Login;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderOutput;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import LightAndDrak.Darkmode;

public class SideBar extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JButton toggleButton;
    private final int SIDEBAR_WIDTH = 200;
    private String activeMenu = null; // Changed: Null for main page
    private boolean isSidebarVisible = true;
    private Timer animationTimer;
    private List<JButton> buttons = new ArrayList<>();
    private AdminUser adminUser;
    private JPanel buttonPanel; // Create a separate field for the button panel
    private JToggleButton darkModeToggle;

    public SideBar() {
        this.adminUser = new AdminUser(0, "Admin");
        setTitle("Admin System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);

        setLayout(new BorderLayout());

        setupToggleButton(); // Create toggle button first
        setupSidebar();
        setupContentPanel();

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser != null ? adminUser : new AdminUser(0, "Admin");
        updateContentPanel(activeMenu);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Title: Admin System (clickable to return to main page)
        JLabel titleLabel = new JLabel("Admin System");
        titleLabel.setForeground(new Color(0x344767));
        titleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeMenu = null;
                updateContentPanel(null);
                updateAllButtonStyles();
                sidebarPanel.revalidate();
                sidebarPanel.repaint();
            }
        });
        sidebarPanel.add(titleLabel);

        try {
            JLabel iconLabel = new JLabel();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 50.0f);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 50.0f);
            String svgPath = "/people-23.svg";
            java.io.InputStream svgStream = getClass().getResourceAsStream(svgPath);
            if (svgStream == null) {
                throw new java.io.IOException("SVG file not found: " + svgPath);
            }
            TranscoderInput input = new TranscoderInput(svgStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);
            transcoder.transcode(input, output);

            BufferedImage image = ImageIO.read(new java.io.ByteArrayInputStream(outputStream.toByteArray()));
            iconLabel.setIcon(new ImageIcon(image));

            sidebarPanel.add(iconLabel);
        } catch (Exception e) {
            System.err.println("Failed to load SVG (people-23.svg): " + e.getMessage());
            e.printStackTrace();
            JLabel errorLabel = new JLabel("SVG Error");
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebarPanel.add(errorLabel);
        }

        String[] menuItems = {"Profile", "Dashboard", "Manage Plane", "Manage Customer"};
        for (String item : menuItems) {
            JButton button = createSidebarButton(item);
            buttons.add(button);
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebarPanel.add(Box.createVerticalGlue());

        JButton logoutButton = createSidebarButton("Logout");
        buttons.add(logoutButton);
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private ImageIcon loadSvgIcon(String svgPath, float width, float height) {
        try {
            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);
            java.io.InputStream svgStream = getClass().getResourceAsStream(svgPath);
            if (svgStream == null) {
                throw new java.io.IOException("SVG file not found: " + svgPath);
            }
            TranscoderInput input = new TranscoderInput(svgStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);
            transcoder.transcode(input, output);

            BufferedImage image = ImageIO.read(new java.io.ByteArrayInputStream(outputStream.toByteArray()));
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Failed to load SVG (" + svgPath + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);

        updateButtonStyle(button, text);

        ImageIcon icon = null;
        if (text.equals("Dashboard")) {
            icon = loadSvgIcon("/dashboard.svg", 24.0f, 24.0f);
        } else if (text.equals("Manage Plane")) {
            icon = loadSvgIcon("/airplane.svg", 24.0f, 24.0f);
        } else if (text.equals("Manage Customer")) {
            icon = loadSvgIcon("/manypeople.svg", 24.0f, 24.0f);
        } else if (text.equals("Profile")) {
            icon = loadSvgIcon("/images.svg", 24.0f, 24.0f);
        }

        if (icon != null) {
            button.setIcon(icon);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setIconTextGap(10);
        }

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.addActionListener(e -> {
            activeMenu = text;
            updateContentPanel(text);
            updateAllButtonStyles();
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
        });

        return button;
    }

    private void updateButtonStyle(JButton button, String text) {
        boolean dark = Darkmode.isDark;
        if (text.equals(activeMenu)) {
            button.setBackground(new Color(0xFF69B4)); // Pink for active
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(dark ? new Color(34, 34, 34) : Color.WHITE);
            button.setForeground(dark ? Color.WHITE : new Color(0x344767));
        }
    }

    private void updateAllButtonStyles() {
        for (JButton button : buttons) {
            updateButtonStyle(button, button.getText());
        }
    }

    private JPanel createMainPagePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Admin System, " + adminUser.getUsername() + "!");
        welcomeLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel warningLabel = new JLabel("<html>You are responsible for anything you do here, so ensure your actions are accurate and problem-free.</html>");
        warningLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        warningLabel.setPreferredSize(new Dimension(600, 50));
        mainPanel.add(warningLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel rulesTitleLabel = new JLabel("Admin Rules:");
        rulesTitleLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 18));
        rulesTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(rulesTitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea rulesArea = new JTextArea(
            "• Ensure all data entered is accurate and verified.\n" +
            "• Protect user privacy and sensitive information.\n" +
            "• Verify actions before saving or submitting.\n" +
            "• Report any issues or errors to the system administrator immediately."
        );
        rulesArea.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        rulesArea.setEditable(false);
        rulesArea.setOpaque(false);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setLineWrap(true);
        rulesArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        rulesArea.setMaximumSize(new Dimension(600, 200));
        mainPanel.add(rulesArea);

        return mainPanel;
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        // Add the button panel
        contentPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add the appropriate panel based on the current active menu
        updateContentPanel(activeMenu);
    }

    private void setupToggleButton() {
        toggleButton = new JButton(isSidebarVisible ? "<-" : "->");
        toggleButton.setBackground(Color.WHITE);
        toggleButton.setForeground(new Color(0x344767));
        toggleButton.setOpaque(true);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toggleButton.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));

        // --- Dark mode toggle ---
        darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setFocusPainted(false);
        darkModeToggle.setBackground(Color.WHITE);
        darkModeToggle.setForeground(new Color(0x344767));
        darkModeToggle.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        darkModeToggle.addActionListener(e -> toggleDarkMode());

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(toggleButton);
        buttonPanel.add(darkModeToggle); // Add the dark mode toggle button

        toggleButton.addActionListener(e -> toggleSidebar());
    }

    private void toggleSidebar() {
        if (animationTimer != null && animationTimer.isRunning()) {
            return;
        }

        final int steps = 20;
        final int delay = 10;
        final int targetWidth = isSidebarVisible ? 0 : SIDEBAR_WIDTH;
        final int startWidth = isSidebarVisible ? SIDEBAR_WIDTH : 0;

        animationTimer = new Timer(delay, new ActionListener() {
            int step = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                float progress = (float) step / steps;
                int newWidth = (int) (startWidth + (targetWidth - startWidth) * progress);

                sidebarPanel.setPreferredSize(new Dimension(newWidth, sidebarPanel.getHeight()));
                sidebarPanel.revalidate();

                if (step >= steps) {
                    ((Timer)e.getSource()).stop();
                    isSidebarVisible = !isSidebarVisible;
                    toggleButton.setText(isSidebarVisible ? "<-" : "->");
                    sidebarPanel.setVisible(isSidebarVisible || newWidth > 0);
                }
            }
        });

        sidebarPanel.setVisible(true);
        animationTimer.start();
    }

    private void toggleDarkMode() {
        try {
            boolean dark = darkModeToggle.isSelected();
            if (dark) {
                Darkmode.setDarkMode(); // Use your global dark mode
                darkModeToggle.setText("Light Mode");
            } else {
                Darkmode.setLightMode(); // Use your global light mode
                darkModeToggle.setText("Dark Mode");
            }
            // Update all components to the new look and feel
            SwingUtilities.updateComponentTreeUI(this);

            // Update custom material colors
            updateMaterialColors(dark);

            // Update sidebar button styles for dark mode
            updateAllButtonStyles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to switch theme: " + ex.getMessage());
        }
    }

    private void updateMaterialColors(boolean darkMode) {
        updateComponentTreeColors(this, darkMode);
    }

    private void updateComponentTreeColors(Component comp, boolean darkMode) {
        Color panelBg = darkMode ? new Color(34, 34, 34) : Color.WHITE;
        Color materialFg = darkMode ? Color.WHITE : new Color(52, 71, 103);
        Color materialBg = darkMode ? new Color(34, 34, 34) : Color.WHITE;

        if (comp instanceof JPanel) {
            comp.setBackground(panelBg);
        } else if (comp instanceof JTable) {
            comp.setBackground(panelBg);
            comp.setForeground(materialFg);
        } else if (comp instanceof JButton || comp instanceof JToggleButton) {
            comp.setBackground(materialBg);
            comp.setForeground(materialFg);
        } else if (comp instanceof JLabel) {
            comp.setForeground(materialFg);
        } else if (comp instanceof JScrollPane) {
            comp.setBackground(panelBg);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentTreeColors(child, darkMode);
            }
        }
    }

    private void updateContentPanel(String menuItem) {
        // Remove everything except the button panel at the top
        Component topComponent = null;
        if (contentPanel.getComponentCount() > 0) {
            topComponent = contentPanel.getComponent(0); // Save the top panel with toggle button
        }

        contentPanel.removeAll();

        // Add back the button panel
        if (topComponent != null) {
            contentPanel.add(topComponent, BorderLayout.NORTH);
        }

        // Add the appropriate content based on menu selection
        if (menuItem == null) {
            contentPanel.add(createMainPagePanel(), BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        } else if (menuItem.equals("Dashboard")) {
            Dashboard dashboardPanel = new Dashboard();
            contentPanel.add(dashboardPanel, BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        } else if (menuItem.equals("Logout")) {
            this.dispose();
            new Login().setVisible(true);
            return; // Exit early since we're disposing this window
        } else if (menuItem.equals("Manage Customer")) {
            ManageCustomers customersPanel = new ManageCustomers();
            customersPanel.setAdminId(adminUser.getAdminId());
            contentPanel.add(customersPanel, BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        } else if (menuItem.equals("Manage Plane")) {
            ManagePlane planePanel = new ManagePlane();
            contentPanel.add(planePanel, BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        } else if (menuItem.equals("Profile")) {
            AdminProfile profilePanel = new AdminProfile();
            profilePanel.setAdminId(adminUser.getAdminId());
            contentPanel.add(profilePanel, BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        } else {
            JLabel contentLabel = new JLabel(menuItem + " Page", SwingConstants.CENTER);
            contentLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
            contentPanel.add(contentLabel, BorderLayout.CENTER);
            updateComponentTreeColors(contentPanel, Darkmode.isDark);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("theme");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            SideBar sidebar = new SideBar();
            sidebar.setAdminUser(new AdminUser(1, "Jinwoo"));
            sidebar.setVisible(true);
        });
    }
}