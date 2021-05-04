/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.core;

import org.parker.mips.architectures.ArchitecturePluginLoader;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;
import org.parker.mips.util.ResourceHandler;

import javax.swing.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parker.mips.util.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
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

        org.parker.mips.log.Configurer.init();

        if (args.length != 0) {
            if (args[0].equals("Updated")) {
                LOGGER.log(Level.INFO, "Successfully Updated to: " + VERSION);
            }
        }

        Preferences.readPreferencesFromDefaultFile(); //loads Options from file
        applyStaticPreferences();

        ResourceHandler.extractResources(); //loads all resources into documents folder

        ThemeHandler.init();

        ArchitecturePluginLoader.loadArchitecturePlugin(); //loads the set Computer Architecture

        checkForUpdates(); //checks for updates
    }

    private static Preferences systemPrefs = Preferences.ROOT_NODE.getNode("system");

    private static void applyStaticPreferences(){
        {
            Preference showToolTips = systemPrefs.getNode("gui").getRawPreference("showToolTips", true);
            showToolTips.addObserver((o, arg) -> {
                ToolTipManager.sharedInstance().setEnabled((Boolean)arg);
            });
            ToolTipManager.sharedInstance().setEnabled((Boolean) showToolTips.val());
        }
    }

}
