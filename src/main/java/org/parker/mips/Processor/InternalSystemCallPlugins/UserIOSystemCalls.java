/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor.InternalSystemCallPlugins;

import org.parker.mips.plugin.SystemCall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class UserIOSystemCalls extends SystemCallPlugin {

    private final UserIO userIO = new UserIO();

    public UserIOSystemCalls() {

        registerSystemCall(new PRSystemCall("USERIO_PRINT_INT", this) {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputNumber(getRegister(4));
            }
        });

        registerSystemCall(new PRSystemCall("USERIO_PRINT_ASCIIZ_STRING", this) {
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
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_INTEGER", this) {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, UserIO.getInt());
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_STRING", this) {
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
        });
        registerSystemCall(new PRSystemCall("USERIO_PRINT_CHAR", this) {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputUnicode(getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_CHAR", this) {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, userIO.getNextChar());
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_LAST_USER_CHAR", this) {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, userIO.lastChar());
            }
        });
    }

    @Override
    public void onLoad() {
        //nothing
    }

    @Override
    public NamedActionListener[] getAllSystemCallFrameNamedActionListeners() {
        return new NamedActionListener[]{new NamedActionListener("UserIO", (ae) -> {
            this.userIO.setVisible(true);
            this.userIO.requestFocus();
        })};
    }

    @Override
    public boolean onUnload() {
        logSystemCallPluginError("Cannot Unload Plugin as its Internal");
        return false;
    }
}
