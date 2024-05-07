package components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JPanel;

/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */



public class SimpleForm extends JPanel {

    // KUNG SA DJANGO ITO UNG VIEWS.PY ??? BASTA ITO INEEXTEND PARA SA MGA FORMS
    public SimpleForm() {
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "background:null");
    }

    public void formInitAndOpen() {

    }

    public void formOpen() {

    }

    public void formRefresh() {

    }

    public boolean formClose() {
        return true;
    }
}
