package cuie.haisi.ortschaft;

import java.util.Objects;

/**
 * Value Type for Ortschaft
 *
 * @author Hasan Selman Kara
 */
class OrtschaftData {

    public final String plz;
    public final String ort;

    OrtschaftData(String plz, String ort) {
        Objects.requireNonNull(plz, "PLZ can not be empty!");
        Objects.requireNonNull(ort, "Ort can not be empty!");
        this.plz = plz;
        this.ort = ort;
    }

    @Override
    public String toString() {
        return "('" + plz + "\'', " + ort + "\')";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrtschaftData that = (OrtschaftData) o;

        if (!plz.equals(that.plz)) return false;
        return ort.equals(that.ort);
    }

    @Override
    public int hashCode() {
        int result = plz.hashCode();
        result = 31 * result + ort.hashCode();
        return result;
    }
}
