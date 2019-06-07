package li.selman.postcodefx;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

/**
 * @author Hasan Selman Kara
 */
class OrtschaftSkin extends SkinBase<OrtschaftControl> {

    private ProgressIndicator progressPlz = new ProgressIndicator();
    private ProgressIndicator progressOrt = new ProgressIndicator();

    private TextField ortField;
    private Label ortLabel;
    private Popup ortPopup;
    private ListView<String> ortList;

    private TextField plzField;
    private Label plzLabel;
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
        ortLabel = new Label();
        ortLabel.getStyleClass().add("ort-label");
        ortList = new ListView<>(getSkinnable().getSortedOrtData());
        ortPopup = new Popup();
        ortPopup.setAutoHide(true);
        ortPopup.getContent().addAll(progressOrt, ortList);

        plzField = new TextField();
        plzField.getStyleClass().add("plz");
        plzField.setPromptText("PLZ");
        plzLabel = new Label();
        plzLabel.getStyleClass().add("plz-label");
        plzList = new ListView<>(getSkinnable().getSortedPlzData());
        plzPopup = new Popup();
        plzPopup.setAutoHide(true);
        plzPopup.getContent().addAll(progressPlz, plzList);
    }

    private void layoutParts() {
        var hbox = new HBox(10, new StackPane(ortField, ortLabel), new StackPane(plzField, plzLabel));

        StackPane.setAlignment(plzLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(ortLabel, Pos.CENTER_LEFT);

        getChildren().add(hbox);
    }

    private void setupEventHandlers() {
        ortField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showPopup(ortPopup, ortField);
            } else {
                ortPopup.hide();
            }
        });

        plzField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                showPopup(plzPopup, plzField);
            } else {
                plzPopup.hide();
            }
        });
    }

    private void showPopup(Popup popup, TextField textField) {
        Point2D txtCoords = textField.localToScene(0.0, 0.0);
        popup.show(textField,
                txtCoords.getX() + textField.getScene().getX() + textField.getScene().getWindow().getX(),
                txtCoords.getY() + textField.getScene().getY() + textField.getScene().getWindow().getY() + textField.getHeight());
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
        // Bind visibility of listview and prgress indicator
        progressOrt.visibleProperty().bind(getSkinnable().doneLoadingProperty().not());
        progressPlz.visibleProperty().bind(getSkinnable().doneLoadingProperty().not());
        ortList.visibleProperty().bind(getSkinnable().doneLoadingProperty());
        plzList.visibleProperty().bind(getSkinnable().doneLoadingProperty());

        // Bind 'editable' visibility
        ortField.visibleProperty().bind(getSkinnable().editableProperty());
        ortLabel.visibleProperty().bind(getSkinnable().editableProperty().not());

        plzField.visibleProperty().bind(getSkinnable().editableProperty());
        plzLabel.visibleProperty().bind(getSkinnable().editableProperty().not());

        // Bind Ort values
        ortField.textProperty().bindBidirectional(getSkinnable().ortUserfacingProperty());
        ortLabel.textProperty().bind(getSkinnable().ortUserfacingProperty());

        // Bind PLZ values
        plzField.textProperty().bindBidirectional(getSkinnable().plzUserfacingProperty());
        plzLabel.textProperty().bind(getSkinnable().plzUserfacingProperty());
    }
}
