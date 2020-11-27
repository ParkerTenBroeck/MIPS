/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor.InternalSystemCallPlugins;

import org.parker.mips.OptionsHandler;
import org.parker.mips.plugin.SystemCall.SystemCall;
import org.parker.mips.plugin.SystemCall.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class DefaultSystemCalls extends SystemCallPlugin {

    public DefaultSystemCalls() {
        System.out.println("thus");
        if (true) {
            return;
        }
        SystemCall.SystemCallData[] scd = this.getSystemCallDataFromClass(this.getClass());

        this.systemCalls[0] = new SystemCall(scd[0], "SYSTEM_HALT_PROGRAM", this) {
            @Override
            public void handleSystemCall() {
                if (OptionsHandler.resetProcessorOnTrap0.value) {
                    resetProcessor();
                } else {
                    stopProcessor();
                }
                logRunTimeSystemCallMessage("Halted Processor");
            }
        };
        this.systemCalls[1] = new SystemCall(scd[1], "SYSTEM_RANDOM_NUM", this) {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) (Math.random() * (getRegister(5) + 1 - getRegister(4))) + getRegister(4));
            }
        };
        this.systemCalls[2] = new SystemCall(scd[2], "SYSTEM_SLEEP_MILLS", this) {
            @Override
            public void handleSystemCall() {
                try {
                    Thread.sleep(getRegister(4));
                } catch (Exception e) {

                }
            }
        };
        this.systemCalls[3] = new SystemCall(scd[3], "SYSTEM_SLEEP_DELTA_MILLS", this) {

            long lastTimeCheck = 0;

            @Override
            public void handleSystemCall() {
                try {
                    Thread.sleep(getRegister(4) - (System.currentTimeMillis() - lastTimeCheck));

                } catch (Exception e) {

                }

                lastTimeCheck = System.currentTimeMillis();

            }
        };
        this.systemCalls[4] = new SystemCall(scd[4], "SYSTEM_BREAK_POINT", this) {
            @Override
            public void handleSystemCall() {
                throwBreakPoint();
            }
        };
        this.systemCalls[5] = new SystemCall(scd[5], "SYSTEM_GET_MILLIS", this) {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) System.currentTimeMillis());
            }
        };
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
