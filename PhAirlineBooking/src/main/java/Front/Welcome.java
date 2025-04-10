
package Front;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Welcome extends javax.swing.JFrame {

    
    public Welcome() {
        initComponents();
        welcome.setText("<html><div style='text-align: center; width: 400px;'>"
    + "Magandang araw! Welcome to Philippine Airlines' Booking System, your gateway to seamless travel across the Philippines and beyond. "
    + "Whether you're soaring to the pristine beaches of Palawan, the vibrant streets of Manila, or international destinations, we’re here to make your journey unforgettable. "
    + "Book your flight, manage your trip, and explore exclusive offers—all with the warmth and hospitality of PAL. "
    + "Let’s fly the skies together—mabuhay!"
    + "</div></html>");
        fadeIn();

    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        welcome = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(null);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("START EXPLORING");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(430, 440, 160, 31);

        welcome.setFont(new java.awt.Font("Microsoft New Tai Lue", 1, 14)); // NOI18N
        welcome.setForeground(new java.awt.Color(0, 0, 0));
        welcome.setText("WELCOME");
        jPanel1.add(welcome);
        welcome.setBounds(250, 270, 570, 170);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PALLL.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 600);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            UIManager.setLookAndFeel(new FlatLightLaf()); 
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Welcome().setVisible(true);
            }
        });
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables
}
