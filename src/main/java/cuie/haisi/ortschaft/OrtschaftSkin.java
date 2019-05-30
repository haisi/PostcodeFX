package cuie.haisi.ortschaft;

import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;

/**
 * @author Hasan Selman Kara
 */
class OrtschaftSkin extends SkinBase<OrtschaftControl> {

    private ComboBox<String> ortComboBox;
    private ComboBox<String> plzComboBox;

    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "style.css";

    OrtschaftSkin(OrtschaftControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
    }

    private void initializeSelf() {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        ortComboBox = new ComboBox<>(getSkinnable().getSortedOrtData());
        plzComboBox = new ComboBox<>(getSkinnable().getSortedPlzData());
    }

    private void layoutParts() {
        var hbox = new HBox(10, ortComboBox, plzComboBox);
        getChildren().add(hbox);
    }
}
