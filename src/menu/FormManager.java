package menu;

import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import components.MainForm;
import components.SimpleForm;
import swing.slider.PanelSlider;
import swing.slider.SimpleTransition;
import utils.UndoRedo;

/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */

// FOR ANIMATION??
public class FormManager {

    private static FormManager instance;
    private final JFrame frame;

    private final UndoRedo<SimpleForm> forms = new UndoRedo<>();

    private boolean menuShowing = true;
    private final PanelSlider panelSlider;
    private final MainForm mainForm;
    private final Menu menu;

    public static void install(JFrame frame) {
        instance = new FormManager(frame);
    }

    private FormManager(JFrame frame) {
        this.frame = frame;
        panelSlider = new PanelSlider();
        mainForm = new MainForm();
        menu = new Menu(new MyDrawerBuilder());
        this.frame.getContentPane().add(panelSlider);
    }

    public static void showMenu() {
        instance.menuShowing = true;
        instance.panelSlider.addSlide(instance.menu, SimpleTransition.getShowMenuTransition(instance.menu.getDrawerBuilder().getDrawerWidth()));
    }

    public static void showForm(SimpleForm component) {
        if (isNewFormAble()) {
            instance.forms.add(component);
            if (instance.menuShowing == true) {
                instance.menuShowing = false;
                Image oldImage = instance.panelSlider.createOldImage();
                instance.mainForm.setForm(component);
                instance.panelSlider.addSlide(instance.mainForm, SimpleTransition.getSwitchFormTransition(oldImage, instance.menu.getDrawerBuilder().getDrawerWidth()));
            } else {
                instance.mainForm.showForm(component);
            }
            instance.forms.getCurrent().formInitAndOpen();
        }
    }

    public static void hideMenu() {
        instance.menuShowing = false;
        instance.panelSlider.addSlide(instance.mainForm, SimpleTransition.getHideMenuTransition(instance.menu.getDrawerBuilder().getDrawerWidth()));
    }

    public static void refresh() {
        if (!instance.menuShowing) {
            instance.forms.getCurrent().formRefresh();
        }
    }

    public static UndoRedo<SimpleForm> getForms() {
        return instance.forms;
    }

    public static boolean isNewFormAble() {
        return instance.forms.getCurrent() == null || instance.forms.getCurrent().formClose();
    }

    public static void updateTempFormUI() {
        for (SimpleForm f : instance.forms) {
            SwingUtilities.updateComponentTreeUI(f);
        }
    }
}
