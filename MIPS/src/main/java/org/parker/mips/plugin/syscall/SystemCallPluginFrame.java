/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;

import org.parker.mips.emulator.Emulator;

import javax.swing.*;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class SystemCallPluginFrame extends JFrame {

    public final String FRAME_NAME;

    protected final static Logger LOGGER = Logger.getLogger(SystemCallPluginFrame.class.getName());

    public SystemCallPluginFrame(String name) {
        this.FRAME_NAME = name;
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("images/project.png");
            ImageIcon icon = new ImageIcon(url);
            this.setIconImage(icon.getImage());
        } catch (Exception e) {

        }
    }

    /**
     * halts the processor the processor can only be started again by the user
     *
     */
    protected final void stopProcessor() {
        Emulator.stop();
    }

}
