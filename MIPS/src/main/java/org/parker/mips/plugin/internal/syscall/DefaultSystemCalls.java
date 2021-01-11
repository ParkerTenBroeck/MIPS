/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.internal.syscall;

import org.parker.mips.OptionsHandler;
import org.parker.mips.plugin.syscall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class DefaultSystemCalls extends SystemCallPlugin {

    public DefaultSystemCalls() {

        registerSystemCall(new PRSystemCall("SYSTEM_HALT_PROGRAM") {
            @Override
            public void handleSystemCall() {
                if (OptionsHandler.resetProcessorOnTrap0.val()) {
                    resetProcessor();
                } else {
                    stopProcessor();
                }
                logRunTimeSystemCallMessage("Halted Processor");
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
    public boolean onUnload() {
        logSystemCallPluginError("Cannot Unload Plugin as its Internal");
        return false;
    }

}
