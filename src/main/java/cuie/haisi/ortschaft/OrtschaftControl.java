package cuie.haisi.ortschaft;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * <p>
 * Style class: {@code ortschaft-control}
 * <p>
 * JavaFX CSS pseudo classes:
 * <ul>
 * <li>{@code invalid}</li>
 * </ul>
 *
 * @author Hasan Selman Kara
 */
public class OrtschaftControl extends Control {

    private final StringProperty plz = new SimpleStringProperty();
    private final StringProperty plzUserfacing = new SimpleStringProperty();
    private final StringProperty ort = new SimpleStringProperty();
    private final StringProperty ortUserfacing = new SimpleStringProperty();

    private final ObservableList<String> plzData = FXCollections.observableArrayList();
    private final FilteredList<String> filteredPlzData = new FilteredList<>(plzData, p -> true);
    private final SortedList<String> sortedPlzData = new SortedList<>(filteredPlzData);

    private final ObservableList<String> ortData = FXCollections.observableArrayList();
    private final FilteredList<String> filteredOrtData = new FilteredList<>(ortData, p -> true);
    private final SortedList<String> sortedOrtData = new SortedList<>(filteredOrtData);

    // TODO no logic behind yet
    private static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");

    // TODO change me
    private static final String FILE_NAME = "plz_de_dev.csv";
//    private static final String FILE_NAME = "plz_de.csv";
    private static final int MAX_DISTANCE = 10;

    public OrtschaftControl() {
        initSelf();
        readOrtschaftsData();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OrtschaftSkin(this);
    }

    private void initSelf() {
        getStyleClass().add("ortschaft-control");

        ort.addListener((observable, oldValue, newValue) -> {
            setOrtUserfacing(newValue);
            setInvalid(false);
            // TODO for future
//            setConvertible(false);
        });

        ortUserfacing.addListener((observable, oldValue, newValue) -> {
//            setInvalid(); TODO check if invalid, i.e. if Ort exists
//            setConvertible() TODO

            if (!isInvalid()) {
                setOrt(newValue);
            }
        });

    }

    private void addValueChangeListener() {

        plz.addListener((observable, oldValue, searchValue) -> {
            // Must be run on GUI Thread: https://bugs.openjdk.java.net/browse/JDK-8081700
            Platform.runLater(() -> {
                // Filtering PLZ, if search term is the start of a string -> match
                filteredPlzData.setPredicate(plz -> {
                    // If filter text is empty, display all PLZ.
                    if (searchValue == null || searchValue.isBlank()) {
                        // By default sort ASC by year of award
                        sortedPlzData.setComparator(Comparator.comparing(String::toString));
                        return true;
                    }

                    return plz.startsWith(searchValue);
                });
            });
        });

        ort.addListener((observable, oldValue, searchValue) -> {
            // Must be run on GUI Thread: https://bugs.openjdk.java.net/browse/JDK-8081700
            Platform.runLater(() -> {
                // Filtering Ortschaften Namen with "fuzzy string search", i.e. approximate a match with Levenshtein
                filteredOrtData.setPredicate(ort -> {
                    System.out.println(searchValue);
                    // If filter text is empty, display all Orte.
                    if (searchValue == null || searchValue.isBlank()) {
                        // By default sort ASC by year of award
                        sortedOrtData.setComparator(Comparator.comparing(String::toString));
                        return true;
                    }

                    // Sort filtered data by ASC Levenshtein distance
                    String lowerCaseFilter = searchValue.toLowerCase();
                    sortedOrtData.setComparator((o1, o2) -> {
                        int o1Distance = LevenshteinDistance.computeLevenshteinDistance(o1.toLowerCase(), lowerCaseFilter);
                        int o2Distance = LevenshteinDistance.computeLevenshteinDistance(o2.toLowerCase(), lowerCaseFilter);
                        return Integer.compare(o1Distance, o2Distance);
                    });

                    // Only display entries with low Levenshtein distance
                    int distance = LevenshteinDistance.computeLevenshteinDistance(ort.toLowerCase(), lowerCaseFilter);
                    return distance < MAX_DISTANCE;
                });

            });

        });
    }

    private void readOrtschaftsData() {
        // TODO fix this URL & Path mess!
        // TODO maybe load in different thread
        URL url = getClass().getResource(FILE_NAME);
        Path dest = null;
        try {
            dest = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try (Stream<String> stream = Files.lines(dest.toAbsolutePath())) {
//        try (Stream<String> stream = Files.lines(Paths.get(getClass().getResource(FILE_NAME).toExternalForm()))) {
            stream
                    .skip(1)
                    .map(line -> line.split(";"))
                    .forEach(tuple -> {
                        ortData.add(tuple[0]);
                        plzData.add(tuple[1]);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // ==================================================================
    // JavaFX pseudo class ceremony
    // ==================================================================

    private final BooleanProperty invalid = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };

    // ==================================================================
    // Getters & Setters
    // ==================================================================

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

    public String getPlzUserfacing() {
        return plzUserfacing.get();
    }

    public StringProperty plzUserfacingProperty() {
        return plzUserfacing;
    }

    public void setPlzUserfacing(String plzUserfacing) {
        this.plzUserfacing.set(plzUserfacing);
    }

    public String getOrtUserfacing() {
        return ortUserfacing.get();
    }

    public StringProperty ortUserfacingProperty() {
        return ortUserfacing;
    }

    public void setOrtUserfacing(String ortUserfacing) {
        this.ortUserfacing.set(ortUserfacing);
    }

    public ObservableList<String> getPlzData() {
        return plzData;
    }

    public FilteredList<String> getFilteredPlzData() {
        return filteredPlzData;
    }

    public SortedList<String> getSortedPlzData() {
        return sortedPlzData;
    }

    public ObservableList<String> getOrtData() {
        return ortData;
    }

    public FilteredList<String> getFilteredOrtData() {
        return filteredOrtData;
    }

    public SortedList<String> getSortedOrtData() {
        return sortedOrtData;
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

}
