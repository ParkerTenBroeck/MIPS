/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor.InternalSystemCallPlugins;

import javax.swing.JFrame;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class DefaultSystemCalls extends SystemCallPlugin {

    public DefaultSystemCalls() {

        super(6, "Default_System_Calls");

        this.systemCalls[0] = new SystemCall(0, "SYSTEM_HALT_PROGRAM", "Stops the program") {
            @Override
            public void handleSystemCall() {
                stopProcessor();
                logRunTimeSystemCallMessage("Halted Processor");
            }
        };
        this.systemCalls[1] = new SystemCall(99, "SYSTEM_RANDOM_NUM", "Stops the program") {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) (Math.random() * (getRegister(5) + 1 - getRegister(4))) + getRegister(4));
            }
        };
        this.systemCalls[2] = new SystemCall(105, "SYSTEM_SLEEP_MILLS", "Stops the program") {
            @Override
            public void handleSystemCall() {
                try {
                    Thread.sleep(getRegister(4));
                } catch (Exception e) {

                }
            }
        };
        this.systemCalls[3] = new SystemCall(106, "SYSTEM_SLEEP_DELTA_MILLS", "Stops the program") {

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
        this.systemCalls[4] = new SystemCall(111, "SYSTEM_BREAK_POINT", "Stops the program") {
            @Override
            public void handleSystemCall() {
                throwBreakPoint();
            }
        };
        this.systemCalls[5] = new SystemCall(130, "SYSTEM_GET_MILLIS", "Stops the program") {
            @Override
            public void handleSystemCall() {
                setRegister(2, (int) System.currentTimeMillis());
            }
        };
    }

    @Override
    public void init() {

    }

    @Override
    public JFrame getPluginFrame() {
        return null;
    }

}
