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
public class ConsoleSystemCalls extends SystemCallPlugin {

    public ConsoleSystemCalls() {
        super(new SystemCall[2], "Console_System_Calls");
    }

    @Override
    public void init() {
        //nothing
    }

}
