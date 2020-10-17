/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processor.InternalSystemCalls;

import GUI.Main_GUI;
import javax.swing.JFrame;
import mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class UserIOSystemCalls extends SystemCallPlugin {

    private static UserIO userIO = new UserIO();

    public UserIOSystemCalls() {
        super(7, "UserIO_System_Calls");

        this.systemCalls[0] = new SystemCall(1, "USERIO_PRINT_INT", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputNumber(getRegister(4));
            }
        };
        this.systemCalls[1] = new SystemCall(4, "USERIO_PRINT_ASCIIZ_STRING", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                int max = 4000;
                int i = 0;
                int index = getRegister(4);
                int step = getRegister(5);
                if (step == 0) {
                    step = 1;
                }
                if (step == 1) {
                    while (getByte(index) != 0 && i <= max) {
                        userIO.outputUnicode(getByte(index));
                        index++;
                        i++;
                    }
                } else if (step == 2) {
                    while (getHalfWord(index * 2) != 0 && i <= max) {
                        userIO.outputUnicode(getHalfWord(index * 2));
                        index++;
                        i++;
                    }
                } else if (step > 2) {
                    while (getWord(index * step) != 0 && i <= max) {
                        userIO.outputUnicode(getWord(index * step));
                        index++;
                        i++;
                    }
                }

                if (i >= max) {
                    logRunTimeSystemCallError("Trap 4 reached limit of " + max + " while printing string possibly non terminated string");
                }
            }
        };
        this.systemCalls[2] = new SystemCall(5, "USERIO_READ_USER_INTEGER", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, UserIO.getInt());
            }
        };
        this.systemCalls[3] = new SystemCall(8, "USERIO_READ_USER_STRING", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                int bufferSize = getRegister(5);
                int memoryOffset = getRegister(4);
                int i = 0;
                if (UserIO.waitForEnter()) {
                    return;
                }
                while (UserIO.hasChar() && i < bufferSize) {
                    setWord(memoryOffset + i * 4, UserIO.getNextChar());
                    i++;
                }
                setWord(memoryOffset + (i + 1) * 4, 0); //terminating zero
            }
        };
        this.systemCalls[4] = new SystemCall(101, "USERIO_PRINT_CHAR", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputUnicode(getRegister(4));
            }
        };
        this.systemCalls[5] = new SystemCall(102, "USERIO_READ_USER_CHAR", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, userIO.getNextChar());
            }
        };
        this.systemCalls[6] = new SystemCall(103, "USERIO_LAST_USER_CHAR", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, userIO.lastChar());
            }
        };
    }

    @Override
    public void init() {
        //nothing
    }

    @Override
    public JFrame getPluginFrame() {
        return userIO;
    }

}
