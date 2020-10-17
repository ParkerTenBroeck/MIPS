/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processor.InternalSystemCalls;

import java.awt.Color;
import javax.swing.JFrame;
import mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class ScreenSystemCalls extends SystemCallPlugin {

    Screen screen = new Screen();

    public ScreenSystemCalls() {
        super(8, "Screen_System_Calls");

        this.systemCalls[0] = new SystemCall(104, "SCREEN_IS_KEY_PRESSED", "Stops the program") {
            @Override
            public void handleSystemCall() {
                setRegister(2, screen.isKeyPressed(getRegister(4)) ? 1 : 0);
            }
        };
        this.systemCalls[1] = new SystemCall(150, "SCREEN_INIT", "Stops the program") {
            @Override
            public void handleSystemCall() {
                screen.setScreenSize(getRegister(4), getRegister(5));
            }
        };
        this.systemCalls[2] = new SystemCall(151, "SCREEN_SET_PIXEL_X_Y_RGB", "Stops the program") {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5), getRegister(6));
            }
        };
        this.systemCalls[3] = new SystemCall(152, "SCREEN_SET_PIXEL_INDEX_RGB", "Stops the program") {
            @Override
            public void handleSystemCall() {
                screen.setPixelColor(getRegister(4), getRegister(5));
            }
        };
        this.systemCalls[4] = new SystemCall(153, "SCREEN_UPDATE", "Stops the program") {
            @Override
            public void handleSystemCall() {
                screen.showScreen();
                screen.updateScreen();
            }
        };
        this.systemCalls[5] = new SystemCall(154, "SCREEN_HSV_TO_RGB", "Stops the program") {
            @Override
            public void handleSystemCall() {
                Color color = new Color(Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0));
                setRegister(4, color.getRed());
                setRegister(5, color.getRed());
                setRegister(6, color.getRed());
            }
        };
        this.systemCalls[6] = new SystemCall(155, "SCREEN_RGB_TO_HSV", "Stops the program") {
            @Override
            public void handleSystemCall() {
                int colorInt = Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0);
                setRegister(4, colorInt);
            }
        };
        this.systemCalls[7] = new SystemCall(156, "SCREEN_FILL", "Stops the program") {
            @Override
            public void handleSystemCall() {
                screen.fillScreen(getRegister(4));
            }
        };

    }

    @Override
    public void init() {
        //nothing
    }

    @Override
    public JFrame getPluginFrame() {
        return screen;
    }

}
