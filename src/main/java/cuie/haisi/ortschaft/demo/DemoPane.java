package cuie.haisi.ortschaft.demo;

import cuie.haisi.ortschaft.OrtschaftControl;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class DemoPane extends BorderPane {
    private OrtschaftControl businessControl;

    private TextField ortField;
    private TextField plzField;
    private CheckBox editableCheckBox;

    private OrtschaftPresentationModel model;

    DemoPane(OrtschaftPresentationModel model) {
        this.model = model;

        initializeControls();
        layoutControls();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        businessControl = new OrtschaftControl();

        ortField = new TextField();
        plzField = new TextField();
        editableCheckBox = new CheckBox();
    }

    private void layoutControls() {
        setCenter(businessControl);
        VBox box = new VBox(10,
                new Label("Business Control Properties"),
                new Label("Ort"), ortField,
                new Label("PLZ"), plzField,
                new Label("Editable"), editableCheckBox);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        plzField.textProperty().bindBidirectional(model.plzProperty());
        ortField.textProperty().bindBidirectional(model.ortProperty());
        editableCheckBox.selectedProperty().bindBidirectional(model.editableProperty());

        businessControl.plzProperty().bindBidirectional(model.plzProperty());
        businessControl.ortProperty().bindBidirectional(model.ortProperty());
        businessControl.editableProperty().bind(model.editableProperty());
    }

}
