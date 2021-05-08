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

import org.parker.mips.architecture.MipsArchitecture;
import org.parker.mips.architecture.syscall.SystemCallPlugin;
import org.parker.mips.architecture.syscall.UnloadInternalSystemCallException;


public class DefaultSystemCalls extends SystemCallPlugin {

    public DefaultSystemCalls() {

        registerSystemCall(new PRSystemCall("SYSTEM_HALT_PROGRAM") {
            @Override
            public void handleSystemCall() {
                if (MipsArchitecture.resetProcessorOnTrap0.val()) {
                    resetProcessor();
                } else {
                    stopProcessor();
                }
            }
        });
        registerSystemCall(new PRSystemCall("SYSTEM_RANDOM_NUM") {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) (Math.random() * (getRegister(5) + 1 - getRegister(4))) + getRegister(4));
            }
        });
        registerSystemCall(new PRSystemCall("SYSTEM_SLEEP_MILLS") {
            @Override
            public void handleSystemCall() {
                try {
                    Thread.sleep(getRegister(4));
                } catch (Exception e) {

                }
            }
        });
        registerSystemCall(new PRSystemCall("SYSTEM_SLEEP_DELTA_MILLS") {

            long lastTimeCheck = 0;

            @Override
            public void handleSystemCall() {
                try {
                    Thread.sleep(getRegister(4) - (System.currentTimeMillis() - lastTimeCheck));

                } catch (Exception e) {

                }

                lastTimeCheck = System.currentTimeMillis();

            }
        });
        registerSystemCall(new PRSystemCall("SYSTEM_BREAK_POINT") {
            @Override
            public void handleSystemCall() {
                throwBreakPoint();
            }
        });
        registerSystemCall(new PRSystemCall("SYSTEM_GET_MILLIS") {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) System.currentTimeMillis());
            }
        });
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {
        throw new UnloadInternalSystemCallException();
        //LOGGER.log(Level.SEVERE, "Cannot Unload Plugin as its Internal");
        //return false;
    }

}
