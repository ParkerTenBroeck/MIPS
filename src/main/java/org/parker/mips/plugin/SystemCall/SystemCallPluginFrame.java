/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.SystemCall;

import javax.swing.JFrame;
import org.parker.mips.Processor.Processor;

/**
 *
 * @author parke
 */
public class SystemCallPluginFrame extends JFrame {

    public final String FRAME_NAME;

    public SystemCallPluginFrame(String name) {
        this.FRAME_NAME = name;
    }

    /**
     * WARNING errors can halt the program if enabled use Warning if program can
     * continue
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallError(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallError(message);
    }

    /**
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallWarning(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallWarning(message);
    }

    /**
     *
     * @param message the message that will be logged
     */
    protected final void logRunTimeSystemCallMessage(String message) {
        SystemCallPluginHandler.logRunTimeSystemCallMessage(message);
    }

    /**
     * halts the processor the processor can only be started again by the user
     *
     */
    protected final void stopProcessor() {
        Processor.stop();
    }

}
