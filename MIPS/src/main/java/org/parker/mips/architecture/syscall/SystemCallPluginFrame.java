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
package org.parker.mips.architecture.syscall;

import org.parker.mips.architecture.emulator.mips.Emulator;

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
            URL url = ClassLoader.getSystemClassLoader().getResource("Images/Icons/SVG/project.png");
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
