package application;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import components.Background;
import forms.DashboardForm;
import menu.FormManager;

/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */
public class Application extends JFrame {
    public static final boolean UNDECORATED = !true;

    public Application() {
        init();
    }

    private void init() {
        if (UNDECORATED) {
            setUndecorated(UNDECORATED);
            setBackground(new Color(0, 0, 0, 0));
        } else {
            getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIScale.scale(new Dimension(1366, 768)));
        setLocationRelativeTo(null);
        setContentPane(new Background());
        // applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        FormManager.install(this);
        FormManager.showForm(new DashboardForm());
        // applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacLightLaf.setup();
        EventQueue.invokeLater(() -> new Application().setVisible(true));
    }
}
