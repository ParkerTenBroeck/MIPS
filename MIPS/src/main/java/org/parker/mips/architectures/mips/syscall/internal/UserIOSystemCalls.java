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
package org.parker.mips.architectures.mips.syscall.internal;

import org.parker.mips.architectures.mips.syscall.SystemCallPlugin;
import org.parker.mips.architectures.mips.syscall.SystemCallRunTimeException;
import org.parker.mips.architectures.mips.syscall.UnloadInternalSystemCallException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

public class UserIOSystemCalls extends SystemCallPlugin {

    private final UserIO userIO = new UserIO();

    public UserIOSystemCalls() {

        registerSystemCall(new PRSystemCall("USERIO_PRINT_INT") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputNumber(getRegister(4));
            }
        });

        registerSystemCall(new PRSystemCall("USERIO_PRINT_ASCIIZ_STRING") {
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
                    throw new SystemCallRunTimeException("Trap 4 reached limit of " + max + " while printing string possibly non terminated string");
                }
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_INTEGER") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, UserIO.getInt());
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_STRING") {
            @Override
            public void handleSystemCall() {
                int bufferSize = getRegister(5);
                int memoryOffset = getRegister(4);

                String input = UserIO.getString();
                ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
                int i = 0;

                while (in.available() > 0 && i < bufferSize) {
                   setWord(memoryOffset + i * 4, in.read());
                   i++;
                }

                setWord(memoryOffset + (i + 1) * 4, 0); //terminating zero
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_PRINT_CHAR") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                userIO.outputUnicode(getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("USERIO_READ_USER_CHAR") {
            @Override
            public void handleSystemCall() {
                userIO.openUserIO();
                setRegister(2, userIO.getNextChar());
            }
        });
        //registerSystemCall(new PRSystemCall("USERIO_LAST_USER_CHAR") {
        //    @Override
        //    public void handleSystemCall() {
        //        userIO.openUserIO();
        //        setRegister(2, userIO.lastChar());
        //    }
        //});

        registerFrameListeners(new Node("Root",
                new Node[]{
                    new Node("UserIO", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            userIO.setVisible(true);
                            userIO.requestFocus();
                        }
                    })}));
    }

    @Override
    public void onLoad() {
        //nothing
    }

//    @Override
//    public NamedActionListener[] getAllSystemCallFrameNamedActionListeners() {
//        return new NamedActionListener[]{new NamedActionListener("UserIO", (ae) -> {
//            this.userIO.setVisible(true);
//            this.userIO.requestFocus();
//        })};
//    }

    @Override
    public void onUnload() {
        throw new UnloadInternalSystemCallException();
    }
}
