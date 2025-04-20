package Front;
import Admin.AdminUser;
import Admin.Dashboard;
import Admin.SideBar;
import Front.UserFront;
import LoginUser.UserLogin;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import connectors.dbconnect;

// Gmail API Imports
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

// JavaMail (JakartaMail)
import jakarta.mail.*;
import jakarta.mail.internet.*;

// GUI Libraries
import javax.swing.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


// Utilities and Logging
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.border.AbstractBorder;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends javax.swing.JFrame {
 private static final String CLIENT_ID = "409809683263-f39qbejpmggi5grcmh0oa7lj98a5vd40.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final List<String> SCOPES = Arrays.asList(
        "https://www.googleapis.com/auth/userinfo.profile", 
        "https://www.googleapis.com/auth/userinfo.email"
    );
    
    
 

    private Connection conn;

    
    public Login() {
        initComponents();
        dbconnect dbc = new dbconnect();
        conn = dbc.getConnection();
        showDateDayAndTime();
        fadeIn();
        showGifOnMainPanel();
        Panelto.setOpaque(false); 
        Panelto.setBackground(new Color(255, 255, 255, 120));
        Panelto.setBorder(new RoundedBorder(50)); 
        exit.setBackground(new Color(0, 0, 0, 0));
        exit.setOpaque(false); 
        exit.setBorder(BorderFactory.createEmptyBorder());
       
        
        showpass.setBackground(new Color(0, 0, 0, 0));
        showpass.setOpaque(false); 
        showpass.setBorder(BorderFactory.createEmptyBorder());
        
    }
    
    
    public void showGifOnMainPanel() {
    URL gifUrl = getClass().getResource("/Gif1.gif");  

    if (gifUrl != null) {  
        ImageIcon gifIcon = new ImageIcon(gifUrl);

       
        JLabel gifLabel = new JLabel(gifIcon);

       
        gifLabel.setBounds(0, 0, gifIcon.getIconWidth(), gifIcon.getIconHeight());
       
        mainPanel.setLayout(null);
        mainPanel.add(gifLabel);
    } else {
        System.out.println("Resource not found: /main/resources/Gif1.gif");
    }
}



      
     private void showDateDayAndTime() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

                java.util.Date now = new java.util.Date();
                day.setText("Date: " + dateFormat.format(now) + " | Day: " + dayFormat.format(now) + " | Time: " + timeFormat.format(now));
            }
        });
        timer.start();
    }
     private void fadeIn() {
    setOpacity(0f);
    
   
    int fadeDuration = 500; 
    int timerInterval = 10;  
    float opacityIncrement = 1f * timerInterval / fadeDuration;  

    Timer fadeTimer = new Timer(timerInterval, new ActionListener() {
        private float opacity = 0f;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (opacity < 1f) {
                opacity += opacityIncrement;  
                setOpacity(Math.min(opacity, 1f));  
            } else {
                ((Timer) e.getSource()).stop();  
            }
        }
    });

    fadeTimer.start();
}

  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        day = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        Panelto = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        login = new javax.swing.JButton();
        donthaveacount = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        showpass = new javax.swing.JCheckBox();
        loginusinggoogle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setMaximumSize(new java.awt.Dimension(1000, 600));
        mainPanel.setMinimumSize(new java.awt.Dimension(1000, 600));
        mainPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        mainPanel.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/488523983_1749071255983391_734912844889370193_n.jpg"))); // NOI18N
        mainPanel.add(jLabel1);
        jLabel1.setBounds(0, 30, 1060, 200);

        day.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        day.setForeground(new java.awt.Color(255, 255, 255));
        day.setText("TIME AND DATE");
        mainPanel.add(day);
        day.setBounds(680, -10, 320, 50);

        exit.setBackground(new java.awt.Color(255, 255, 255));
        exit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 0, 0));
        exit.setText("EXIT");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        mainPanel.add(exit);
        exit.setBounds(0, -10, 76, 50);

        Panelto.setBackground(new java.awt.Color(204, 204, 204));
        Panelto.setMinimumSize(new java.awt.Dimension(680, 310));
        Panelto.setPreferredSize(new java.awt.Dimension(680, 310));
        Panelto.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("LOG IN TO PROCEED");
        Panelto.add(jLabel5);
        jLabel5.setBounds(252, 6, 177, 25);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Email");
        Panelto.add(jLabel6);
        jLabel6.setBounds(150, 50, 59, 20);

        email.setBackground(new java.awt.Color(255, 255, 255));
        email.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Panelto.add(email);
        email.setBounds(140, 70, 407, 26);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Password");
        Panelto.add(jLabel7);
        jLabel7.setBounds(150, 110, 80, 20);

        password.setBackground(new java.awt.Color(255, 255, 255));
        password.setForeground(new java.awt.Color(0, 0, 0));
        Panelto.add(password);
        password.setBounds(140, 130, 410, 26);

        login.setBackground(new java.awt.Color(0, 204, 0));
        login.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        login.setText("LOG IN");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        Panelto.add(login);
        login.setBounds(290, 180, 130, 27);

        donthaveacount.setBackground(new java.awt.Color(204, 204, 204));
        donthaveacount.setForeground(new java.awt.Color(0, 0, 0));
        donthaveacount.setText("Don't Have Account Yet? Click here to register");
        donthaveacount.setBorder(null);
        donthaveacount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                donthaveacountActionPerformed(evt);
            }
        });
        Panelto.add(donthaveacount);
        donthaveacount.setBounds(220, 270, 270, 27);

        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("*Password is case sensitive");
        Panelto.add(jLabel8);
        jLabel8.setBounds(150, 160, 140, 16);

        showpass.setBackground(new java.awt.Color(204, 204, 204));
        showpass.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        showpass.setForeground(new java.awt.Color(0, 0, 0));
        showpass.setText("Show Password");
        showpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpassActionPerformed(evt);
            }
        });
        Panelto.add(showpass);
        showpass.setBounds(430, 160, 120, 20);

        loginusinggoogle.setBackground(new java.awt.Color(255, 255, 255));
        loginusinggoogle.setForeground(new java.awt.Color(0, 0, 0));
        loginusinggoogle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/7123025_logo_google_g_icon (3).png"))); // NOI18N
        loginusinggoogle.setText("LOG IN USING GOOGLE ACCOUNT");
        loginusinggoogle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginusinggoogleActionPerformed(evt);
            }
        });
        Panelto.add(loginusinggoogle);
        loginusinggoogle.setBounds(210, 220, 280, 40);

        mainPanel.add(Panelto);
        Panelto.setBounds(160, 240, 680, 310);

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed

    private void loginusinggoogleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginusinggoogleActionPerformed

       
    try {
        // Google OAuth flow
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            jsonFactory, new InputStreamReader(getClass().getResourceAsStream("/client_secret_409809683263-f39qbejpmggi5grcmh0oa7lj98a5vd40.apps.googleusercontent.com.json"))
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, clientSecrets, SCOPES)
        .setAccessType("offline")
        .build();

        String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI.create(authorizationUrl));
        } else {
            JOptionPane.showMessageDialog(null, "Please manually visit: " + authorizationUrl);
        }

        String authCode = JOptionPane.showInputDialog(null, "Enter the authorization code:");

        if (authCode != null && !authCode.trim().isEmpty()) {
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(authCode)
                .setRedirectUri(redirectUri)
                .execute();

            GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientSecrets)
                .build()
                .setFromTokenResponse(tokenResponse);

            String accessToken = credential.getAccessToken();

            // Fetch user info
            URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            String userEmail = jsonResponse.has("email") ? jsonResponse.get("email").getAsString() : null;
            String fullName = jsonResponse.has("name") ? jsonResponse.get("name").getAsString() : null;
            String firstName = jsonResponse.has("given_name") ? jsonResponse.get("given_name").getAsString() : null;
            String lastName = jsonResponse.has("family_name") ? jsonResponse.get("family_name").getAsString() : null;
            String middleName = null; // Not provided by Google API
            String phone = null; // Not available from basic userinfo endpoint

            if (userEmail == null) {
                JOptionPane.showMessageDialog(null, "Unable to retrieve email. Cannot proceed.");
                return;
            }

            if (!isEmailExists(userEmail)) {
                // Ask only for password
                JPasswordField passwordField = new JPasswordField();
                JCheckBox showPassCheckBox = new JCheckBox("Show Password");

                showPassCheckBox.addActionListener(e -> {
                    if (showPassCheckBox.isSelected()) {
                        passwordField.setEchoChar((char) 0);
                    } else {
                        passwordField.setEchoChar('•');
                    }
                });

                Object[] fields = {
                    "Password:", passwordField,
                    showPassCheckBox
                };

                int result = JOptionPane.showConfirmDialog(null, fields, "Complete Your Profile", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String password = new String(passwordField.getPassword());

                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty.");
                        return;
                    }

                    String membershipId = generateMembershipId();
                    insertNewUser(membershipId, firstName, middleName, lastName, userEmail, phone, password);

                    String userEmailFromDb = getUserEmailFromDatabase(userEmail);
                    if (userEmailFromDb != null) {
                        sendWelcomeEmail(userEmailFromDb, firstName != null ? firstName : "User");
                        JOptionPane.showMessageDialog(null, "Welcome, " + (firstName != null ? firstName : userEmail) + "!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registration canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Welcome back, " + userEmail + "!");
                email.setText(userEmail);
                password.setText(getPasswordByEmail(userEmail));
            }
        } else {
            JOptionPane.showMessageDialog(null, "No authorization code provided.");
        }
    } catch (IOException | GeneralSecurityException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error during Google login: " + e.getMessage());
    }

        }

        private String getEmailFromDatabase(String email) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT email FROM customers WHERE email = ?")) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("email");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private boolean isEmailExists(String email) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT email FROM customers WHERE email = ?")) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        private String generateMembershipId() {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder randomChars = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * characters.length());
                randomChars.append(characters.charAt(index));
            }

            return "PAL-" + randomChars.toString();
        }

        private void insertNewUser(String membershipId, String firstName, String middleName, String lastName, String email, String phone, String password) {
            PreparedStatement stmt = null;

            try {
                conn.setAutoCommit(false);

                stmt = conn.prepareStatement("INSERT INTO customers (membership_id, first_name, middle_name, last_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, membershipId);
                stmt.setString(2, firstName);
                stmt.setString(3, middleName);
                stmt.setString(4, lastName);
                stmt.setString(5, email);
                stmt.setString(6, phone);
                stmt.setString(7, password);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();

                    String userEmail = getUserEmailFromDatabase(membershipId);

                    if (userEmail != null) {
                        sendWelcomeEmail(userEmail, firstName);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Unable to retrieve user email from database.");
                    }
                } else {
                    conn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getPasswordByEmail(String email) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT password FROM customers WHERE email = ?")) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void sendWelcomeEmail(String userEmail, String firstName) {
            String fromEmail = "charlessamotanez24@gmail.com";
            String fromPassword = "awsu bhoe cbia gmwr";
            String subject = "Welcome to Philippines Airline!";
            String body = "Dear " + firstName + ",\n\nWelcome to our Philippines Airline Booking Sytem! We are glad to have you here.\n\nBest regards,\nCharles";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });
            session.setDebug(true);

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("Welcome email sent successfully to " + userEmail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        private String getUserEmailFromDatabase(String membershipId) {
            String email = null;
            String query = "SELECT email FROM customers WHERE membership_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, membershipId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    email = rs.getString("email");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return email;

    }//GEN-LAST:event_loginusinggoogleActionPerformed

    private void showpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpassActionPerformed
        if (showpass.isSelected()) {
            password.setEchoChar((char) 0);
        } else {
            password.setEchoChar('•');
        }
    }//GEN-LAST:event_showpassActionPerformed

    private void donthaveacountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_donthaveacountActionPerformed

        JFrame frame = new JFrame("Registration Form");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel registrationFormLabel = new JLabel("Register New Account");
        registrationFormLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.1;
        panel.add(registrationFormLabel, gbc);

        JLabel firstNameLabel = new JLabel("📝 First Name:");
        JTextField firstNameField = new JTextField(20);
        firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First Name");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(firstNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(firstNameField, gbc);

        JLabel middleNameLabel = new JLabel("📝 Middle Name:");
        JTextField middleNameField = new JTextField(20);
        middleNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Middle Name");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(middleNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(middleNameField, gbc);

        JLabel lastNameLabel = new JLabel("📝 Last Name:");
        JTextField lastNameField = new JTextField(20);
        lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last Name");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(lastNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(lastNameField, gbc);

        JLabel emailLabel = new JLabel("📧 Email:");
        JTextField emailField = new JTextField(20);
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "example@email.com");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(emailField, gbc);

        JLabel phoneLabel = new JLabel("📱 Phone Number:");
        JTextField phoneField = new JTextField(20);
        phoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Phone Number");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 1;
        panel.add(phoneField, gbc);

        JLabel passwordLabel = new JLabel("🔒 Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1;
        panel.add(passwordField, gbc);

        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('?');
            }
        });
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST;
        panel.add(showPasswordCheckbox, gbc);

        JButton submitButton = new JButton("Register");
        submitButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.addActionListener(e -> {

            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());

            String membershipId = generateMembershipId();

            String authCode = generateAuthenticationCode();
            boolean isAuthCodeSent = sendAuthenticationCodeEmail(email, authCode);

            if (isAuthCodeSent) {
                String userAuthCode = JOptionPane.showInputDialog("Enter the authentication code sent to your email:");

                if (authCode.equals(userAuthCode)) {

                    insertNewUser(membershipId, firstName, middleName, lastName, email, phone, password);

                    sendWelcomeEmail(email, firstName);
                    JOptionPane.showMessageDialog(frame, "Registration successful!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Authentication failed. The code entered is incorrect.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to send authentication code.");
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        frame.setVisible(true);
        }

        private String generateAuthenticationCode() {
            return String.format("%06d", (int) (Math.random() * 1000000));
        }

        private boolean sendAuthenticationCodeEmail(String userEmail, String authCode) {
            String subject = "Authentication Code for Registration";
            String body = "Your authentication code is: " + authCode + "\n\nIgnore this if you are not the one who resgiter";
            return sendEmail(userEmail, subject, body);
        }

        private boolean sendEmail(String userEmail, String subject, String body) {
            String fromEmail = "charlessamotanez24@gmail.com";
            String fromPassword = "awsu bhoe cbia gmwr";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("Email sent successfully to " + userEmail);
                return true;
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }

    }//GEN-LAST:event_donthaveacountActionPerformed

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {                                      
    String emailOrUsername = email.getText();
    String Password = new String(password.getPassword());

    // Admin login
    AdminUser admin = getAdminByUsername(emailOrUsername, Password);
    if (admin != null) {
        logAdminLogin(admin.getAdminId());
        switchToAdminDashboard(admin);
    } 
    else if (isValidUser(emailOrUsername, Password)) {
        // Customer login
        UserLogin user = getUserByUsernameOrEmail(emailOrUsername);
        
        // Set the current user in the UserLogin class so it can be accessed elsewhere
        UserLogin.setCurrentUser(user);
        
        switchToUserFront();
    } 
    else {
        JOptionPane.showMessageDialog(null, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private AdminUser getAdminByUsername(String username, String password) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    AdminUser admin = null;
    try {
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Database connection is not initialized or closed");
        }
        String query = "SELECT admin_id, username FROM admin WHERE username = ? AND password = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        rs = stmt.executeQuery();
        if (rs.next()) {
            int adminId = rs.getInt("admin_id");
            String adminUsername = rs.getString("username");
            admin = new AdminUser(adminId, adminUsername);
            System.out.println("Admin found: ID=" + adminId + ", Username=" + adminUsername);
        } else {
            System.out.println("No admin found for username: " + username);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return admin;
}

private void logAdminLogin(int adminId) {
    PreparedStatement stmt = null;
    try {
        String query = "INSERT INTO admin_login_history (admin_id) VALUES (?)";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, adminId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void switchToAdminDashboard(AdminUser admin) {
    this.setVisible(false);
    SideBar sidebar = new SideBar();
    sidebar.setAdminUser(admin);
    sidebar.setVisible(true);
}

private boolean isValidUser(String emailOrUsername, String password) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
        String query = "SELECT * FROM customers WHERE email = ? AND password = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, emailOrUsername);
        stmt.setString(2, password);
        rs = stmt.executeQuery();
        return rs.next();  // Returns true if a customer with matching credentials is found
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return false;
}

private boolean isAdmin(String emailOrUsername, String password) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, emailOrUsername); // Check against admin username
        stmt.setString(2, password);
        rs = stmt.executeQuery();
        return rs.next();  // Returns true if an admin with matching credentials is found
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return false;
}

private UserLogin getUserByUsernameOrEmail(String emailOrUsername) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UserLogin user = null;
    try {
        String query = "SELECT * FROM customers WHERE email = ? ";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, emailOrUsername);
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            String membershipId = rs.getString("membership_id");
            String firstName = rs.getString("first_name");
            String middleName = rs.getString("middle_name");
            String lastName = rs.getString("last_name");
            String phoneNumber = rs.getString("phone_number");
            String password = rs.getString("password");
            int points = rs.getInt("points");
            user = new UserLogin(membershipId, emailOrUsername, firstName, middleName, lastName, phoneNumber, password, points);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return user;
}


private void switchToUserFront() {
    this.setVisible(false);
    UserFront userFront = new UserFront();
    userFront.setVisible(true);


    }                                     

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("theme");
        UIManager.put("defeaultFont", new Font(FlatRobotoFont.FAMILY,Font.PLAIN,13));
        FlatLightLaf.setup();
        
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
    
   
   



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panelto;
    private javax.swing.JLabel day;
    private javax.swing.JButton donthaveacount;
    private javax.swing.JTextField email;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton login;
    private javax.swing.JButton loginusinggoogle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox showpass;
    // End of variables declaration//GEN-END:variables

    private class RoundedBorder extends AbstractBorder {
        private int radius;
    
        RoundedBorder(int radius) {
            this.radius = radius;
        }
    
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // First draw the background with transparency
            g2d.setColor(new Color(255, 255, 255, 120)); // White with transparency
            g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            
            // Then draw the border with a slightly more visible white
            g2d.setColor(new Color(255, 255, 255, 180)); // Border color - more visible
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            
            g2d.dispose();
        }
    
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius / 2, this.radius / 2, this.radius / 2, this.radius / 2);
        }
    
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius / 2;
            return insets;
        }
    }
}
