/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import org.parker.mips.gui.MainGUI;
import org.parker.mips.gui.OptionsGUI;
import org.parker.mips.gui.theme.ThemeHandler;
import org.parker.mips.plugin.PluginLoader;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.parker.mips.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.8.1";
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

    public static void main(String[] args) {

        if (args.length != 0) {
            if (args[0].equals("Updated")) {
                Log.logSystemMessage("Successful Updated to: " + VERSION);
            }
        }
        
        OptionsHandler.readOptionsFromDefaultFile(); //loads Options from file 

        ResourceHandler.extractResources(); //loads all resorces into documents folder

        ThemeHandler.init();
        MainGUI gui = new MainGUI(); //creates the GUI
        //ThemeHandler.updateUI();

        PluginLoader.loadDefaultPlugins(); //loads all plugins internal and external

        checkForUpdates(); //checks for updates

        for(int i = 0; i < 20; i ++){
            new OptionsGUI();
        }

    }

}
