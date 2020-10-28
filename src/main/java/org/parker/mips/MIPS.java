/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import org.parker.mips.GUI.MainGUI;
import org.parker.mips.GUI.ThemedJFrameComponents.ThemeHandler;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPluginHandler;
import static org.parker.mips.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.7.5";
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
        //Log.logMessage(tmp);
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

        ThemeHandler.loadCurrentTheme(); //loads current theme

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

        SystemCallPluginHandler.loadDefaultPlugins(); //loads all plugins internal and external

        checkForUpdates(); //checks for updates
    }

}
