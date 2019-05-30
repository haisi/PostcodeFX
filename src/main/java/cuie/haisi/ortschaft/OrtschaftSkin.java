package cuie.haisi.ortschaft;

import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

/**
 * @author Hasan Selman Kara
 */
class OrtschaftSkin extends SkinBase<OrtschaftControl> {

    private TextField ortField;
    private Popup ortPopup;
    private ListView<String> ortList;

    private TextField plzField;
    private Popup plzPopup;
    private ListView<String> plzList;

    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "style.css";

    OrtschaftSkin(OrtschaftControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        ortField = new TextField();
        ortList = new ListView<>(getSkinnable().getSortedOrtData());
        ortPopup = new Popup();
        ortPopup.setAutoHide(true);
        ortPopup.getContent().add(ortList);

        plzField = new TextField();
        plzList = new ListView<>(getSkinnable().getSortedPlzData());
        plzPopup = new Popup();
        plzPopup.setAutoHide(true);
        plzPopup.getContent().add(plzList);
    }

    private void layoutParts() {
        var hbox = new HBox(10, ortField, plzField);
        getChildren().add(hbox);
    }

    private void setupEventHandlers() {
        ortField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (newValue) {
                ortPopup.show(ortField.getScene().getWindow());
            } else {
                ortPopup.hide();
            }
        });

        plzField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (newValue) {
                plzPopup.show(plzField.getScene().getWindow());
            } else {
                plzPopup.hide();
            }
        });
    }

    private void setupValueChangedListeners() {
        ortList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    ortField.textProperty().set(newValue);
                });

        plzList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    plzField.textProperty().set(newValue);
                });
    }

    private void setupBindings() {
        ortField.textProperty().bindBidirectional(getSkinnable().ortUserfacingProperty());
        plzField.textProperty().bindBidirectional(getSkinnable().plzUserfacingProperty());
    }
}
