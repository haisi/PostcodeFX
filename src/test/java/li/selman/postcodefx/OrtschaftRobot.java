/*
 * Copyright (c) 2019 by Hasan Selman Kara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package li.selman.postcodefx;

import javafx.scene.control.TextField;
import org.testfx.api.FxRobot;

/**
 * Base on the Robot UI-Testing pattern.
 *
 * @see <a href="https://academy.realm.io/posts/kau-jake-wharton-testing-robots/">Talk by Jake Wharton on the subject</a>
 *
 * @author Hasan Selman Kara
 */
public class OrtschaftRobot extends BaseTestRobot {

    private final FxRobot fxRobot;

    public OrtschaftRobot(FxRobot fxRobot) {
        super(fxRobot);
        this.fxRobot = fxRobot;
    }

    public OrtschaftRobot typeCity(String city) {
        writeTextfield(".ort", city);
        return this;
    }

    public OrtschaftRobot setCity(String city) {
        setTextfield(".ort", city);
        return this;
    }

    public OrtschaftRobot typePostcode(String postcode) {
        writeTextfield(".ort", postcode);
        return this;
    }

    public OrtschaftRobot setPostcode(String postcode) {
        setTextfield(".ort", postcode);
        return this;
    }
}
