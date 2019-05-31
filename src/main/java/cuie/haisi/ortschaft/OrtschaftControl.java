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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * <p>
 * Style class: {@code ortschaft-control}
 * <p>
 * JavaFX CSS pseudo classes:
 * <ul>
 * <li>{@code invalid}</li>
 * <li>{@code invalid-ort}</li>
 * <li>{@code invalid-plz}</li>
 * </ul>
 *
 * @author Hasan Selman Kara
 */
public class OrtschaftControl extends Control {

    private final BooleanProperty doneLoading = new SimpleBooleanProperty(false);

    private final StringProperty plz = new SimpleStringProperty();
    private final StringProperty plzUserfacing = new SimpleStringProperty();
    private final StringProperty ort = new SimpleStringProperty();
    private final StringProperty ortUserfacing = new SimpleStringProperty();

    private Map<String, List<String>> plz2ort = new HashMap<>();
    private final ObservableList<String> plzData = FXCollections.observableArrayList();
    private final FilteredList<String> filteredPlzData = new FilteredList<>(plzData, p -> true);
    private final SortedList<String> sortedPlzData = new SortedList<>(filteredPlzData);

    private Map<String, List<String>> ort2plz = new HashMap<>();
    private final ObservableList<String> ortData = FXCollections.observableArrayList();
    private final FilteredList<String> filteredOrtData = new FilteredList<>(ortData, p -> true);
    private final SortedList<String> sortedOrtData = new SortedList<>(filteredOrtData);

    private static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");
    private static final PseudoClass INVALID_ORT_CLASS = PseudoClass.getPseudoClass("invalid-ort");
    private static final PseudoClass INVALID_PLZ_CLASS = PseudoClass.getPseudoClass("invalid-plz");

    // TODO change me
    private static final String FILE_NAME = "plz_de_dev.csv";
    //    private static final String FILE_NAME = "plz_de.csv";
    private static final int MAX_DISTANCE = 5;

    public OrtschaftControl() {
        initSelf();
        readOrtschaftsData()
                .whenComplete((tuple, throwable) -> Platform.runLater(() -> {
                    if (throwable == null) {
                        ort2plz = tuple.ort2plz;
                        plz2ort = tuple.plz2ort;

                        ortData.setAll(tuple.ort2plz.keySet());
                        plzData.setAll(tuple.plz2ort.keySet());
                        doneLoading.set(true);
                    } else {
                        throwable.printStackTrace();
                    }
                }));
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
            setInvalid_ort(false);
        });

        ortUserfacing.addListener((observable, oldValue, newValue) -> {
            setInvalid_ort(!ortData.contains(newValue));
            setInvalid(isInvalid_ort() || isInvalid_plz());

            if (!isInvalid()) {
                setOrt(newValue);
            }
        });

        plz.addListener((observable, oldValue, newValue) -> {
            setPlzUserfacing(newValue);
            setInvalid_plz(false);
        });

        plzUserfacing.addListener((observable, oldValue, newValue) -> {
            setInvalid_plz(!plzData.contains(newValue));
            setInvalid(isInvalid_ort() || isInvalid_plz());

            if (!isInvalid()) {
                setPlz(newValue);
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
                // Limit possible postcodes for the selected location
                if (ort2plz.containsKey(searchValue)) {
                    final var validPlzs = ort2plz.get(searchValue);
                    filteredPlzData.setPredicate(plz -> {
                        return validPlzs.contains(plz);
                    });
                }

                // Filtering Ortschaften Namen with "fuzzy string search", i.e. approximate a match with Levenshtein
                filteredOrtData.setPredicate(ort -> {
                    // If filter text is empty, display all Orte.
                    if (searchValue == null || searchValue.isBlank()) {
                        // By default sort ASC by year of award
                        sortedOrtData.setComparator(Comparator.comparing(String::toString));
                        return true;
                    } else if (searchValue.length() < 4) {
                        // Fuzzy search performs bad with short search terms
                        return ort.toLowerCase().startsWith(searchValue.toLowerCase());
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

    private CompletableFuture<Tuple> readOrtschaftsData() {
        return CompletableFuture.supplyAsync(() -> {
            URL url = getClass().getResource(FILE_NAME);
            final Path dest;
            try {
                dest = Paths.get(url.toURI());
            } catch (URISyntaxException e) {
                return new Tuple(new HashMap<>(), new HashMap<>());
            }

            final Map<String, List<String>> _ort2plz = new HashMap<>(15939);
            final Map<String, List<String>> _plz2ort = new HashMap<>(8255);

            try (Stream<String> stream = Files.lines(dest.toAbsolutePath())) {
                stream
                        .skip(1)
                        .map(line -> line.split(";"))
                        .forEach(tuple -> {
                            String ort = tuple[0];
                            String plz = tuple[1];

                            createMapping(_ort2plz, ort, plz);
                            createMapping(_plz2ort, plz, ort);
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Tuple(_ort2plz, _plz2ort);
        });
    }

    private static void createMapping(Map<String, List<String>> map, String key, String listItem) {
        map.compute(key, (it, list) -> {
            if (list == null) {
                list = new ArrayList<>(1);
            }
            list.add(listItem);
            return list;
        });
    }

    private static final class Tuple {
        final Map<String, List<String>> ort2plz;
        final Map<String, List<String>> plz2ort;

        private Tuple(Map<String, List<String>> ort2plz, Map<String, List<String>> plz2ort) {
            this.ort2plz = ort2plz;
            this.plz2ort = plz2ort;
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

    private final BooleanProperty invalid_ort = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_ORT_CLASS, get());
        }
    };

    private final BooleanProperty invalid_plz = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_PLZ_CLASS, get());
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

    public boolean isInvalid_ort() {
        return invalid_ort.get();
    }

    public BooleanProperty invalid_ortProperty() {
        return invalid_ort;
    }

    public void setInvalid_ort(boolean invalid_ort) {
        this.invalid_ort.set(invalid_ort);
    }

    public boolean isInvalid_plz() {
        return invalid_plz.get();
    }

    public BooleanProperty invalid_plzProperty() {
        return invalid_plz;
    }

    public void setInvalid_plz(boolean invalid_plz) {
        this.invalid_plz.set(invalid_plz);
    }

    public boolean isDoneLoading() {
        return doneLoading.get();
    }

    public BooleanProperty doneLoadingProperty() {
        return doneLoading;
    }

    public void setDoneLoading(boolean doneLoading) {
        this.doneLoading.set(doneLoading);
    }
}
