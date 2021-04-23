/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.architectures.mips.syscall.internal;

import org.parker.mips.architectures.mips.syscall.SystemCallPlugin;
import org.parker.mips.architectures.mips.syscall.UnloadInternalSystemCallException;
import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;


/**
 *
 * @author parke
 */
public class DefaultSystemCalls extends SystemCallPlugin {

    private static final Preference<Boolean> resetProcessorOnTrap0 = Preferences.ROOT_NODE.getNode("system/plugins/systemCalls/Base").getRawPreference("resetProcessorOnTrap0", false);

    public DefaultSystemCalls() {

        registerSystemCall(new PRSystemCall("SYSTEM_HALT_PROGRAM") {
            @Override
            public void handleSystemCall() {
                if (resetProcessorOnTrap0.val()) {
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
