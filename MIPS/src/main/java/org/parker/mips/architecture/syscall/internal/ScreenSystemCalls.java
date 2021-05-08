/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.parker.mips.architecture.syscall.internal;

import org.parker.mips.architecture.syscall.SystemCallPlugin;
import org.parker.mips.architecture.syscall.UnloadInternalSystemCallException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenSystemCalls extends SystemCallPlugin {

    private final Screen screen = new Screen();

    public ScreenSystemCalls() {

        registerSystemCall(new PRSystemCall("SCREEN_IS_KEY_PRESSED") {
            @Override
            public void handleSystemCall() {
                setRegister(2, screen.isKeyPressed(getRegister(4)) ? 1 : 0);
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_INIT") {
            @Override
            public void handleSystemCall() {
                screen.setScreenSize(getRegister(4), getRegister(5));
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_SET_PIXEL_X_Y_RGB") {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5), getRegister(6));
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_SET_PIXEL_INDEX_RGB") {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5));
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_UPDATE") {
            @Override
            public void handleSystemCall() {
                screen.showScreen();
                screen.updateScreen();
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_HSV_TO_RGB") {
            @Override
            public void handleSystemCall() {
                Color color = new Color(Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0));
                setRegister(4, color.getRed());
                setRegister(5, color.getRed());
                setRegister(6, color.getRed());
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_RGB_TO_HSV") {
            @Override
            public void handleSystemCall() {
                int colorInt = Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0);
                setRegister(4, colorInt);
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_FILL") {
            @Override
            public void handleSystemCall() {
                Screen.fillScreen(getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_GET_PIXEL_X_Y_RGB") {
            @Override
            public void handleSystemCall() {
                setRegister(2, screen.getPixelColor(getRegister(4), getRegister(5)));
            }
        });
        registerSystemCall(new PRSystemCall("SCREEN_GET_PIXEL_INDEX_RGB") {
            @Override
            public void handleSystemCall() {
                setRegister(2, screen.getPixelColor(getRegister(4)));
            }
        });

        registerFrameListeners(new Node("Root",
                new Node[]{
                    new Node("Screen", (ActionListener) (ActionEvent ae) -> {
                        screen.setVisible(true);
                        screen.requestFocus();
                    })}));
    }

    @Override
    public void onLoad() {
        //nothing
    }

    @Override
    public void onUnload() {
        throw new UnloadInternalSystemCallException();
    }

}
