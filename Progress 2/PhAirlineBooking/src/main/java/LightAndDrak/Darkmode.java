package LightAndDrak;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;

public class Darkmode {
    public static boolean isDark = false;

    public static void setDarkMode() {
        try {
            FlatMacDarkLaf.setup();
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            isDark = true;
        } catch (Exception ex) {
            System.err.println("Failed to apply dark mode: " + ex.getMessage());
        }
    }

    public static void setLightMode() {
        try {
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
            isDark = false;
        } catch (Exception ex) {
            System.err.println("Failed to apply light mode: " + ex.getMessage());
        }
    }
}
