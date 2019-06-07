package li.selman.postcodefx;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import li.selman.postcodefx.demo.OrtschaftPresentationModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hasan Selman Kara
 */
@ExtendWith(ApplicationExtension.class)
class OrtschaftControlIntTest {

    private OrtschaftControl ortschaftControl;

    @BeforeAll
    static void headless() {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Start
    private void start(Stage stage) {
        ortschaftControl = new OrtschaftControl();
        ortschaftControl.setEditable(true);

        stage.setScene(new Scene(new StackPane(ortschaftControl), 500, 500));
        stage.show();
    }

    @Test
    @DisplayName("Invalid city names test")
    void invalid_city_name_test(FxRobot fxRobot) {
        //given
        var ortRobot = new OrtschaftRobot(fxRobot);
        String invalidCity = "1234";

        //when
        ortRobot.setCity(invalidCity);
//        ortRobot.typeCity(invalidCity);

        //then
        Assertions.assertThat(fxRobot.lookup(".ort").queryAs(TextField.class)).hasText(invalidCity);
        assertThat(ortschaftControl.isInvalid_ort()).as("Ort must be invalid").isTrue();
        assertThat(ortschaftControl.isInvalid()).as("Whole control must be invalid").isTrue();
    }

}