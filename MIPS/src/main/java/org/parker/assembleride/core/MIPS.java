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
package org.parker.assembleride.core;

import org.parker.assembleride.architecture.ArchitecturePluginHandler;
import org.parker.assembleride.gui.theme.ThemeHandler;
import org.parker.assembleride.preferences.Preferences;
import org.parker.assembleride.util.SystemResources;

import javax.swing.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parker.assembleride.util.UpdateHandler.checkForUpdates;


public class MIPS {

    public static final String VERSION = "0.9.8.2.3";
    public static final String JAR_PATH;

    static {
        String tmp;
        try {
            tmp = new File(MIPS.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getAbsolutePath();
        } catch (Exception ex) {
            tmp = "";
        }
        JAR_PATH = tmp;
    }

    private final static Logger LOGGER = Logger.getLogger(MIPS.class.getName());

    public static void main(String[] args) {

        org.parker.assembleride.log.Configurer.init();

        if (args.length != 0) {
            if (args[0].equals("Updated")) {
                LOGGER.log(Level.INFO, "Successfully Updated to: " + VERSION);
            }
        }

        Preferences.readPreferencesFromDefaultFile(); //loads Options from file
        applyStaticPreferences();

        SystemResources.extractResources(); //loads all resources into documents folder

        ThemeHandler.init();

        ArchitecturePluginHandler.loadArchitecturePlugin(); //loads the set Computer Architecture

        checkForUpdates(); //checks for updates
    }

    private static void applyStaticPreferences(){
        {

            SystemPreferences.showToolTips.addObserver((o, arg) -> {
                ToolTipManager.sharedInstance().setEnabled((Boolean)arg);
            });
            ToolTipManager.sharedInstance().setEnabled(SystemPreferences.showToolTips.val());
        }
    }

}
