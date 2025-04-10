
package Front;
import com.formdev.flatlaf.FlatLightLaf;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;


public class FrontPage extends javax.swing.JFrame {
    private JFXPanel fxPanel;  
    private MediaPlayer mediaPlayer;

    
    public FrontPage() {
        initComponents();
        initializeVideoPlayer();
    }
    
    private void initializeVideoPlayer() {
    
    fxPanel = new JFXPanel();
    fxPanel.setPreferredSize(fxpanel.getPreferredSize());

    fxpanel.setLayout(new BorderLayout());
    fxpanel.add(fxPanel, BorderLayout.CENTER);

    
    Platform.runLater(() -> {
        
        URL videoUrl = getClass().getResource("/PAL.mp4");
        if (videoUrl == null) {
            System.out.println("Error: Video file not found!");
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

        
        mediaPlayer.setOnEndOfMedia(() -> {
            SwingUtilities.invokeLater(() -> {
                
                new Welcome().setVisible(true);
                dispose();  
            });
        });
    });
}


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fxpanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1000, 563));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fxpanel.setPreferredSize(new java.awt.Dimension(1000, 563));

        javax.swing.GroupLayout fxpanelLayout = new javax.swing.GroupLayout(fxpanel);
        fxpanel.setLayout(fxpanelLayout);
        fxpanelLayout.setHorizontalGroup(
            fxpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        fxpanelLayout.setVerticalGroup(
            fxpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );

        getContentPane().add(fxpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
       public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FrontPage.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    // End of variables declaration//GEN-END:variables
}
