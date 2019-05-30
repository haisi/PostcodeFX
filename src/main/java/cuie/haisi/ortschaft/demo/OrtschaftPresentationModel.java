package cuie.haisi.ortschaft.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Hasan Selman Kara
 */
public class OrtschaftPresentationModel {

    private final StringProperty plz = new SimpleStringProperty();
    private final StringProperty ort = new SimpleStringProperty();

    public String getPlz() {
        return plz.get();
    }

    public StringProperty plzProperty() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz.set(plz);
    }

    public String getOrt() {
        return ort.get();
    }

    public StringProperty ortProperty() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort.set(ort);
    }
}
