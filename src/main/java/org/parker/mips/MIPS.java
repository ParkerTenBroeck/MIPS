/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.parker.mips.GUI.MainGUI;
import org.parker.mips.GUI.ThemedJFrameComponents.ThemeHandler;
import org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls.DefaultSystemCalls;
import static org.parker.mips.UpdateHandler.checkForUpdates;
import org.parker.mips.plugin.PluginLoader;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.7.7";
    public static final String JAR_PATH;
    //public static final String[] INSTRUCTIONS = new String[]{"add", "addu", "addi", "addiu", "and", "andi", "div", "divu", "mult", "multu", "nor", "or", "ori", "sll", "sllv", "sra", "srav", "srl", "srlv", "sub", "subu", "xor", "xori", "lhi", "llo", "slt", "sltu", "slti", "sltiu", "beq", "bgtz", "ble", "bne", "j", "jal", "jalr", "jr", "lb", "lbu", "lh", "lhu", "lw", "sb", "sh", "sw", "mfhi", "mflo", "mthi", "mtlo", "trap"};

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

        try {
            //System.out.println(MIPS.class.getClassLoader().getParent());

            Thread.currentThread().setContextClassLoader(new br());
        } catch (MalformedURLException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        }
//        new DefaultSystemCalls();

        if (args.length != 0) {
            if (args[0].equals("Updated")) {
                Log.logSystemMessage("Successful Updated to: " + VERSION);

                try {
                    File file;
                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "GUI");
                        Files.deleteIfExists(file.toPath());
                    } catch (Exception e) {

                    }

                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "Syntax");
                        file.delete();
                    } catch (Exception e) {

                    }

                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "GUI_");
                        file.delete();
                    } catch (Exception e) {

                    }

                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "Syntax_");
                        file.delete();
                    } catch (Exception e) {

                    }
                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "GUI_Themes");
                        file.delete();
                    } catch (Exception e) {

                    }

                    try {
                        file = new File(ResourceHandler.THEME_PATH + ResourceHandler.FILE_SEPERATOR + "Syntax_Themes");
                        file.delete();
                    } catch (Exception e) {

                    }
                } catch (Exception e) {

                }
            }
        }

        OptionsHandler.readOptionsFromDefaultFile(); //loads Options from file 

        ResourceHandler.extractResources(); //loads all resorces into documents folder

        ThemeHandler.loadCurrentTheme(); //loads current theme

        PluginLoader.loadDefaultPlugins(); //loads all plugins internal and external

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MainGUI gui = new MainGUI(); //creates the GUI

                try {                                   //loads the Icon for the JFrame
                    //System.out.println("rubasd");
                    URL url = ClassLoader.getSystemClassLoader().getResource("images/logo3.png");
                    //System.out.println(url);
                    ImageIcon icon = new ImageIcon(url);
                    //System.out.println(icon);
                    gui.setIconImage(icon.getImage());
                } catch (Exception e) {

                }
            }
        };
        runnable.run();

        checkForUpdates(); //checks for updates
    }

}
