/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.processor.SystemCalls;

import mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;

/**
 *
 * @author parke
 */
public class DefaultSystemCalls extends SystemCallPlugin {

    public DefaultSystemCalls() {

        super(new SystemCall[2], "Default_Plugin");

        this.systemCalls[0] = new SystemCall(0, "HALT_PROGRAM", "Stops the program") {
            @Override
            public void handleSystemCall() {
                stopProcessor();
                logRunTimeSystemCallMessage("Halted Processor");
            }
        };
        this.systemCalls[1] = new SystemCall(1, "PRINT_INT", "prints integer to the user output console") {
            @Override
            public void handleSystemCall() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    public void init() {

    }

}
