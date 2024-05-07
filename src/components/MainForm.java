package components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import application.Application;
import menu.FormManager;
import swing.slider.PanelSlider;
import swing.slider.SimpleTransition;
import swing.slider.SliderTransition;

/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */
public class MainForm extends JPanel {

    public MainForm() {
        init();
    }

    private void init() {
        if (Application.UNDECORATED) {
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "border:5,5,5,5;"
                    + "arc:30");
        }
        setLayout(new MigLayout("wrap,fillx", "[fill]", ""));
        header = createHeader();
        panelSlider = new PanelSlider();
        JScrollPane scroll = new JScrollPane(panelSlider);
        scroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0");
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "width:10");
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        add(header);
        add(scroll);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");

        cmdMenu = createButton(new FlatSVGIcon("raven/resources/icon/menu.svg"));
        cmdRefresh = createButton(new FlatSVGIcon("raven/resources/icon/refresh.svg"));
        cmdMenu.addActionListener(e -> {
            FormManager.showMenu();
        });
      
        cmdRefresh.addActionListener(e -> {
            FormManager.refresh();
        });

        panel.add(cmdMenu);
        panel.add(cmdRefresh);
        return panel;
    }

    private JButton createButton(Icon icon) {
        JButton button = new JButton(icon);
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Button.toolbar.background;"
                + "arc:10;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0");
        return button;
    }

    public void showForm(Component component, SliderTransition transition) {
        checkButton();
        panelSlider.addSlide(component, transition);
        revalidate();
    }

    public void showForm(Component component) {
        showForm(component, SimpleTransition.getDefaultTransition(false));
    }

    public void setForm(Component component) {
        checkButton();
        panelSlider.addSlide(component, null);
    }

    private void checkButton() {
        cmdRefresh.setEnabled(FormManager.getForms().getCurrent() != null);
    }

    private JPanel header;
    private JButton cmdMenu;
    private JButton cmdRefresh;
    private PanelSlider panelSlider;
}
