/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.processor;

import GUI.Main_GUI;
import static GUI.Main_GUI.openUserIO;
import GUI.Screen;
import GUI.UserIO;
import static GUI.UserIO.outputNumber;
import static GUI.UserIO.outputUnicode;
import java.awt.Color;
import static mips.processor.Processor.logRunTimeError;
import static mips.processor.Processor.logRunTimeMessage;
import static mips.processor.Registers.getRegister;
import static mips.processor.Registers.setRegister;

/**
 *
 * @author parke
 */
public class SystemCall {

    private static long lastTimeCheck = 0;

    public static void SystemCall(int id) {

        try {

            switch (id) {

                case 0:   //stops the program
                    Main_GUI.stop();
                    Processor.reset();
                    Main_GUI.infoBox("Stop", "program has been halted");
                    logRunTimeMessage("Program has been halted");
                    break;

                case 1:  //outputs a number to output
                    openUserIO();
                    outputNumber(Registers.getRegister(4));
                    break;

                case 4:
                    break;

                case 5: //gets an integer from the user
                    openUserIO();
                    setRegister(2, UserIO.getInt());
                    break;

                case 99:  //sets a random number between register 4 and 5
                    setRegister(2, (int) (Math.random() * (getRegister(5) + 1 - getRegister(4))) + getRegister(4));
                    break;

                case 101:  //outputs a char to console
                    openUserIO();
                    outputUnicode(Registers.getRegister(4));
                    break;

                case 102: //waits for the user to press enter then return the frist char and shift everything over by one returns into register 2
                    openUserIO();
                    Registers.setRegister(2, UserIO.getNextChar());
                    break;

                case 103: // returns the last char the user pressed 0 if its empty  returns into register 2

                    openUserIO();
                    Registers.setRegister(2, UserIO.lastChar());

                    break;

                case 104:  //
                    setRegister(2, Screen.isKeyPressed(getRegister(4)) ? 1 : 0);
                    break;

                case 105: //sleeps number of millis in register 4
                    try {
                        Thread.sleep(getRegister(4));
                    } catch (Exception e) {

                    }
                    break;

                case 106: //sleeps number of millis in register 4 munus the time difference from the last call
                    System.out.println(getRegister(4) - (System.currentTimeMillis() - lastTimeCheck));
                    try {
                        Thread.sleep(getRegister(4) - (System.currentTimeMillis() - lastTimeCheck));
                        
                    } catch (Exception e) {

                    }
                    
                    lastTimeCheck = System.currentTimeMillis();
                 
                    break;

                case 111: // breaks the program if break is enabled
                    if (Main_GUI.canBreak()) {
                        Main_GUI.stop();
                        logRunTimeMessage("Program has reached a breakpoint");
                    }else{
                        logRunTimeMessage("Program has attempted to break");
                    }
                    break;

                case 130: // gives the lower int of system.millies in register 2
                    setRegister(2, (int) System.currentTimeMillis());
                    break;

                case 150: //sets screen size
                    Screen.setScreenSize(getRegister(4), getRegister(5));
                    break;

                case 151:  //sets pixel color at x $4, y $5, and color $6
                    Screen.setPixelColor(getRegister(4), getRegister(5), getRegister(6));
                    break;

                case 152:  // sets pixel color at index $4, and color $6
                    Screen.setPixelColor(getRegister(4), getRegister(5));
                    break;

                case 153: //updates screen with new image
                    Main_GUI.showScreen();
                    Screen.updateScreen();
                    break;

                case 154:   //hsv 0 - 255, h $4, s $5, v $6 - returns rgb values into register 4, 5, 6
                    Color color = new Color(Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0));
                    setRegister(4, color.getRed());
                    setRegister(5, color.getRed());
                    setRegister(6, color.getRed());
                    break;

                case 155: //hsv 0 - 255, h $4, s $5, v $6 - returns color int $4
                    int colorInt = Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0);
                    setRegister(4, colorInt);
                    break;

                default:
                    logRunTimeError("invalid trap command");
                    break;
            }
        } catch (Exception e) {

        }
    }

}
