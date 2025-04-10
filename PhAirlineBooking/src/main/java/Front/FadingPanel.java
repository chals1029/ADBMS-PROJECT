package Front;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class FadingPanel extends JPanel {

    private float opacity = 1f; // Default opacity (fully opaque)

    public FadingPanel() {
        super();
    }

    // Set the opacity of the panel
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();  // Repaint the panel when opacity changes
    }

    // Override the paintComponent method to apply opacity
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Apply transparency using Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2d.setComposite(alphaComposite);

        // Continue painting the panel as usual
        super.paintComponent(g);
    }
}
