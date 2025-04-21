package Front;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.geometry.Pos;

public class FrontPage extends javax.swing.JFrame {
    private JFXPanel fxPanel;  
    private MediaPlayer mediaPlayer;
    private ExecutorService executorService;
    private CompletableFuture<Media> mediaLoadFuture;
    
    public FrontPage() {
        // Create thread pool
        executorService = Executors.newFixedThreadPool(2);
        
        // Start preloading video in background
        preloadVideo();
        
        // Initialize UI components
        initComponents();
        
        // Apply fade-in effect
        fadeIn();
        
        // Initialize video player with preloaded video
        initializeVideoPlayer();
    }
    
    private void preloadVideo() {
        mediaLoadFuture = CompletableFuture.supplyAsync(() -> {
            try {
                // Initialize JavaFX toolkit in advance
                initializeJavaFX();
                
                URL videoUrl = getClass().getResource("/PAL.mp4");
                if (videoUrl == null) {
                    System.out.println("Error: Video file not found!");
                    return null;
                }
                
                String videoPath = videoUrl.toExternalForm();
                return new Media(videoPath);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, executorService);
    }
    
    private void initializeJavaFX() {
        // Initialize JavaFX toolkit
        new JFXPanel(); // This initializes JavaFX platform
    }
    
    private void initializeVideoPlayer() {
        // Create FX panel
        fxPanel = new JFXPanel(); // This also initializes JavaFX if not already done
        fxPanel.setPreferredSize(fxpanel.getPreferredSize());

        fxpanel.setLayout(new BorderLayout());
        fxpanel.add(fxPanel, BorderLayout.CENTER);

        // Load and play video in a background thread
        executorService.submit(() -> {
            try {
                // Ensure JavaFX is initialized
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {});

                // Load media on JavaFX thread
                Platform.runLater(() -> {
                    try {
                        URL videoUrl = getClass().getResource("/PAL.mp4");
                        if (videoUrl == null) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Error: Video file not found!");
                                transitionToWelcomeScreen();
                            });
                            return;
                        }
                        String videoPath = videoUrl.toExternalForm();
                        Media media = new Media(videoPath);
                        mediaPlayer = new MediaPlayer(media);
                        MediaView mediaView = new MediaView(mediaPlayer);

                        mediaView.setPreserveRatio(true);
                        mediaView.setFitWidth(fxPanel.getPreferredSize().width);
                        mediaView.setFitHeight(fxPanel.getPreferredSize().height);

                        StackPane root = new StackPane(mediaView);
                        root.setAlignment(Pos.CENTER);
                        Scene scene = new Scene(root, fxPanel.getPreferredSize().width, fxPanel.getPreferredSize().height);

                        fxPanel.setScene(scene);

                        mediaPlayer.setAutoPlay(true);
                        mediaPlayer.setOnEndOfMedia(this::transitionToWelcomeScreen);
                        mediaPlayer.setRate(1.0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(this::transitionToWelcomeScreen);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(this::transitionToWelcomeScreen);
            }
        });
    }
    
    private void transitionToWelcomeScreen() {
        // Ensure this runs on JavaFX thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::transitionToWelcomeScreen);
            return;
        }
        
        // Clean up resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        
        // Switch to welcome screen on Swing EDT
        SwingUtilities.invokeLater(() -> {
            new Welcome().setVisible(true);
            cleanupResources();
            dispose();
        });
    }

    private void cleanupResources() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fxpanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fxpanel.setPreferredSize(new java.awt.Dimension(1000, 563));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("SKIP ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fxpanelLayout = new javax.swing.GroupLayout(fxpanel);
        fxpanel.setLayout(fxpanelLayout);
        fxpanelLayout.setHorizontalGroup(
            fxpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fxpanelLayout.createSequentialGroup()
                .addContainerGap(893, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        fxpanelLayout.setVerticalGroup(
            fxpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fxpanelLayout.createSequentialGroup()
                .addContainerGap(475, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(61, 61, 61))
        );

        getContentPane().add(fxpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new Welcome().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("theme");
        UIManager.put("defeaultFont", new Font(FlatRobotoFont.FAMILY,Font.PLAIN,13));
        FlatLightLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            new FrontPage().setVisible(true);
        });
    }

    private void fadeIn() {
        setOpacity(0f);

        int fadeDuration = 540; 
        int interval = 30; 
        int steps = fadeDuration / interval; 
        float opacityIncrement = 1f / steps;

        Timer fadeTimer = new Timer(interval, new ActionListener() {
            private float opacity = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1f) {
                    opacity += opacityIncrement;  
                    if (opacity > 1f) {
                        opacity = 1f;  
                    }
                    setOpacity(opacity);  
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        fadeTimer.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel fxpanel;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
