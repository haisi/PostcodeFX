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

    private CheckBox readOnlyBox;
    private CheckBox mandatoryBox;
    private TextField ortField;
    private TextField plzField;

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

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        ortField = new TextField();
        plzField = new TextField();
    }

    private void layoutControls() {
        setCenter(businessControl);
        VBox box = new VBox(10,
                new Label("Business Control Properties"),
                new Label("Ort"), ortField,
                new Label("PLZ"), plzField,
                new Label("readOnly"), readOnlyBox,
                new Label("mandatory"), mandatoryBox);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
//        ageSlider.valueProperty().bindBidirectional(model.ageProperty());
//        ortField.textProperty().bindBidirectional(model.age_LabelProperty());
//        readOnlyBox.selectedProperty().bindBidirectional(model.age_readOnlyProperty());
//        mandatoryBox.selectedProperty().bindBidirectional(model.age_mandatoryProperty());

        businessControl.plzProperty().bind(model.plzProperty());
        businessControl.ortProperty().bind(model.ortProperty());
    }

}
