/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor.InternalSystemCallPlugins;

import java.awt.Color;
import org.parker.mips.plugin.SystemCall.SystemCall;
import org.parker.mips.plugin.SystemCall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class ScreenSystemCalls extends SystemCallPlugin {

    private final Screen screen = new Screen();

    public ScreenSystemCalls() {

        //this.addFrameToGUI(screen);
        SystemCall.SystemCallData[] scd = this.getSystemCallDataFromClass(this.getClass());

        this.systemCalls[0] = new SystemCall(scd[0], "SCREEN_IS_KEY_PRESSED", this) {
            @Override
            public void handleSystemCall() {
                setRegister(2, screen.isKeyPressed(getRegister(4)) ? 1 : 0);
            }
        };
        this.systemCalls[1] = new SystemCall(scd[1], "SCREEN_INIT", this) {
            @Override
            public void handleSystemCall() {
                screen.setScreenSize(getRegister(4), getRegister(5));
            }
        };
        this.systemCalls[2] = new SystemCall(scd[2], "SCREEN_SET_PIXEL_X_Y_RGB", this) {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5), getRegister(6));
            }
        };
        this.systemCalls[3] = new SystemCall(scd[3], "SCREEN_SET_PIXEL_INDEX_RGB", this) {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5));
            }
        };
        this.systemCalls[4] = new SystemCall(scd[4], "SCREEN_UPDATE", this) {
            @Override
            public void handleSystemCall() {
                screen.showScreen();
                screen.updateScreen();
            }
        };
        this.systemCalls[5] = new SystemCall(scd[5], "SCREEN_HSV_TO_RGB", this) {
            @Override
            public void handleSystemCall() {
                Color color = new Color(Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0));
                setRegister(4, color.getRed());
                setRegister(5, color.getRed());
                setRegister(6, color.getRed());
            }
        };
        this.systemCalls[6] = new SystemCall(scd[6], "SCREEN_RGB_TO_HSV", this) {
            @Override
            public void handleSystemCall() {
                int colorInt = Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0);
                setRegister(4, colorInt);
            }
        };
        this.systemCalls[7] = new SystemCall(scd[7], "SCREEN_FILL", this) {
            @Override
            public void handleSystemCall() {
                screen.fillScreen(getRegister(4));
            }
        };
    }

    @Override
    public void onLoad() {
        //nothing
    }

    @Override
    public NamedActionListener[] getAllSystemCallFrameNamedActionListeners() {
        return new NamedActionListener[]{new NamedActionListener("Screen", (ae) -> {
            this.screen.setVisible(true);
            this.screen.requestFocus();
        })};
    }

    @Override
    public boolean onUnload() {
        logSystemCallPluginError("Cannot Unload Plugin as its Internal");
        return false;
    }

}
