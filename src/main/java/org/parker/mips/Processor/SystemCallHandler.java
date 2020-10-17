/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor;

import java.util.ArrayList;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPluginHandler;
import static org.parker.mips.Processor.Processor.logRunTimeError;
import static org.parker.mips.Processor.Processor.logRunTimeMessage;
import static org.parker.mips.Processor.Processor.logRunTimeWarning;

/**
 *
 * @author parke
 */
public class SystemCallHandler {

    private static final ArrayList<SystemCallPlugin> registeredSystemCallPlugins = new ArrayList();
    private static final ArrayList<SystemCall> registeredSystemCalls = new ArrayList();

    private static SystemCall[] idrkWhat = new SystemCall[200];

    public static void registerSystemCallPlugin(SystemCallPlugin scp) {

        if (scp == null) {
            return;
        }
        if (scp.getSystemCalls() == null) {
            SystemCallPluginHandler.logPluginHandlerError("System Calls is null in" + scp.PLUGIN_NAME + " skipping for now");
            return;
        }

        SystemCall[] scl = scp.getSystemCalls();

        int totalConflicts = 0;

        for (SystemCall sc : scl) {
            if (sc == null) {
                totalConflicts++;
                SystemCallPluginHandler.logPluginHandlerError("System Call in " + scp.PLUGIN_NAME + " is null skipping for now");
                continue;
            }
            boolean conflict = false;
            for (SystemCall rsc : registeredSystemCalls) {
                if ((sc.SYSTEM_CALL_NAME.equals(rsc.SYSTEM_CALL_NAME)) || sc.SYSTEM_CALL_NUMBER == rsc.SYSTEM_CALL_NUMBER) {
                    conflict = true;
                    totalConflicts++;
                    SystemCallPluginHandler.logPluginHandlerError("System Call Conflict " + sc.SYSTEM_CALL_NAME + ":" + sc.SYSTEM_CALL_NUMBER
                            + " was not registered because " + rsc.SYSTEM_CALL_NAME + ":" + rsc.SYSTEM_CALL_NUMBER + " was already reigstered");
                    break;
                }
            }
            if (!conflict) {
                registeredSystemCalls.add(sc);
                idrkWhat[sc.SYSTEM_CALL_NUMBER] = sc;
                SystemCallPluginHandler.logPluginHandlerSystemMessage("System Call " + sc.SYSTEM_CALL_NAME + ":" + sc.SYSTEM_CALL_NUMBER + " was successfully registered");
            } else {
                SystemCallPluginHandler.logPluginHandlerWarning("System Call " + sc.SYSTEM_CALL_NAME + ":" + sc.SYSTEM_CALL_NUMBER + "was not registered because of a conflict");
                //warning system call (name and number) was not registered
            }
        }
        registeredSystemCallPlugins.add(scp);
        if (totalConflicts > 0) {
            SystemCallPluginHandler.logPluginHandlerWarning("SystemCall plugin: " + scp.PLUGIN_NAME + " was reigstered with " + totalConflicts + " conflicts");
        } else {
            SystemCallPluginHandler.logPluginHandlerSystemMessage("SystemCall plugin: " + scp.PLUGIN_NAME + " was reigstered with " + totalConflicts + " conflicts" + "\n");
        }

    }

    public static void SystemCall(int id) {
        try {
            idrkWhat[id].handleSystemCall();
        } catch (Exception e) {

        }
    }

    /*
    public static void SystemCallo(int id) {

        try {

            switch (id) {

                case 0:   //stops the program
                    Main_GUI.stop();
                    //Processor.reset();
                    //Main_GUI.infoBox("Stop", "program has been halted");
                    logRunTimeMessage("Program has been halted");
                    break;

                case 1:  //outputs a number to output
                    openUserIO();
                    outputNumber(Registers.getRegister(4));
                    break;

                case 4: //Print the ASCIIZ string to console starting at the address in $4
                {
                    openUserIO();
                    int max = 4000;
                    int i = 0;
                    int index = getRegister(4);
                    int step = getRegister(5);
                    if (step == 0) {
                        step = 1;
                    }
                    if (step == 1) {
                        while (Memory.getByte(index) != 0 && i <= max) {
                            outputUnicode(Memory.getByte(index));
                            index++;
                            i++;
                        }
                    } else if (step == 2) {
                        while (Memory.getHalfWord(index * 2) != 0 && i <= max) {
                            outputUnicode(Memory.getHalfWord(index * 2));
                            index++;
                            i++;
                        }
                    } else if (step > 2) {
                        while (Memory.getWord(index * step) != 0 && i <= max) {
                            outputUnicode(Memory.getWord(index * step));
                            index++;
                            i++;
                        }
                    }

                    if (i >= max) {
                        Processor.logRunTimeError("Trap 4 reached limit of " + max + " while printing string possibly non terminated string");
                    }
                }
                break;

                case 5: //gets an integer from the user
                    openUserIO();
                    setRegister(2, UserIO.getInt());
                    break;

                case 8: //Read a string from the user console and store at the address in $4.Length of buffer in $5* Need to allocate one more word than used (for the terminating 0 in theASCIIZ string)
                {
                    int bufferSize = getRegister(5);
                    int memoryOffset = getRegister(4);
                    int i = 0;
                    if (UserIO.waitForEnter()) {
                        return;
                    }
                    while (UserIO.hasChar() && i < bufferSize) {
                        Memory.setWord(memoryOffset + i * 4, UserIO.getNextChar());
                        i++;
                    }
                    Memory.setWord(memoryOffset + (i + 1) * 4, 0); //terminating zero
                }
                Registers.setRegister(2, UserIO.getNextChar());
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
                    } else {
                        //logRunTimeMessage("Program has attempted to break");
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

                case 154: //hsv 0 - 255, h $4, s $5, v $6 - returns rgb values into register 4, 5, 6
                {
                    Color color = new Color(Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0));
                    setRegister(4, color.getRed());
                    setRegister(5, color.getRed());
                    setRegister(6, color.getRed());
                }
                break;

                case 155: //hsv 0 - 255, h $4, s $5, v $6 - returns color int $4
                {
                    int colorInt = Color.HSBtoRGB((float) (getRegister(4) / 255.0), (float) getRegister(5) / (float) 255.0, (float) getRegister(6) / (float) 255.0);
                    setRegister(4, colorInt);
                }
                break;
                case 156:
                    Screen.fillScreen(getRegister(4));
                    break;

                default:
                    logRunTimeError("invalid trap command");
                    break;
            }
        } catch (Exception e) {

        }
    }

     */
    public static void logRunTimeSystemCallError(String message) {
        logRunTimeError("[System Call] " + message);
    }

    public static void logRunTimeSystemCallWarning(String message) {
        logRunTimeWarning("[System Call] " + message);
    }

    public static void logRunTimeSystemCallMessage(String message) {
        logRunTimeMessage("[System Call] " + message);
    }

}
