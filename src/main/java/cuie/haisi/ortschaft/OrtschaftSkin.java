package cuie.haisi.ortschaft;

import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

/**
 * @author Hasan Selman Kara
 */
class OrtschaftSkin extends SkinBase<OrtschaftControl> {

    private ProgressIndicator progressPlz = new ProgressIndicator();
    private ProgressIndicator progressOrt = new ProgressIndicator();

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
        ortField.getStyleClass().add("ort");
        ortField.setPromptText("Ortschaft");
        ortList = new ListView<>(getSkinnable().getSortedOrtData());
        ortPopup = new Popup();
        ortPopup.setAutoHide(true);
        ortPopup.getContent().addAll(progressOrt, ortList);

        plzField = new TextField();
        plzField.getStyleClass().add("plz");
        plzField.setPromptText("PLZ");
        plzList = new ListView<>(getSkinnable().getSortedPlzData());
        plzPopup = new Popup();
        plzPopup.setAutoHide(true);
        plzPopup.getContent().addAll(progressPlz, plzList);
    }

    private void layoutParts() {
        var hbox = new HBox(10, ortField, plzField);
        getChildren().add(hbox);
    }

    private void setupEventHandlers() {
        ortField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ortPopup.show(ortField.getScene().getWindow());
            } else {
                ortPopup.hide();
            }
        });

        plzField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                plzPopup.show(plzField.getScene().getWindow());
            } else {
                plzPopup.hide();
            }
        });
    }

    private void setupValueChangedListeners() {
        ortList.setOnKeyPressed(key -> {
            ortList.requestFocus();
            if (key.getCode().equals(KeyCode.ESCAPE)) {
                ortPopup.hide();
            }
        });

        plzList.setOnKeyPressed(key -> {
            plzList.requestFocus();
            if (key.getCode().equals(KeyCode.ESCAPE)) {
                plzPopup.hide();
            }
        });

        ortList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return;
                    }
                    ortField.textProperty().set(newValue);
                });

        plzList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return;
                    }
                    plzField.textProperty().set(newValue);
                });
    }

    private void setupBindings() {
        progressOrt.visibleProperty().bind(getSkinnable().doneLoadingProperty().not());
        progressPlz.visibleProperty().bind(getSkinnable().doneLoadingProperty().not());
        ortList.visibleProperty().bind(getSkinnable().doneLoadingProperty());
        plzList.visibleProperty().bind(getSkinnable().doneLoadingProperty());

        ortField.textProperty().bindBidirectional(getSkinnable().ortUserfacingProperty());
        plzField.textProperty().bindBidirectional(getSkinnable().plzUserfacingProperty());
    }
}
