/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips;

import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import org.parker.mips.GUI.Main_GUI;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPluginHandler;
import static org.parker.mips.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.7";
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
        ResourceHandler.extractResources();

        Main_GUI gui = new Main_GUI();

        try {
            //System.out.println("rubasd");
            URL url = ClassLoader.getSystemClassLoader().getResource("images/logo.png");
            //System.out.println(url);
            ImageIcon icon = new ImageIcon(url);
            //System.out.println(icon);
            gui.setIconImage(icon.getImage());
        } catch (Exception e) {

        }

        SystemCallPluginHandler.loadDefaultPlugins();
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//// Allowing the serialization of static fields    
//
//        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
//        // Creates a Gson instance based on the current configuration
//        Gson gson = gsonBuilder.create();
//        String json = gson.toJson(new MIPS());
//        System.out.println(json);
//
//        //static int temp = 0;
        checkForUpdates();

//        try {
//            Thread.sleep(3000);
//
//            ThemeHandler.setThemeFromName(ThemeHandler.TEXT_COLOR_1_PROPERTY_NAME, Color.BLACK, true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME, Color.green, true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.BACKGROUND_COLOR_1_PROPERTY_NAME, Color.yellow, true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.BACKGROUND_COLOR_2_PROPERTY_NAME, Color.yellow.darker(), true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.BACKGROUND_COLOR_3_PROPERTY_NAME, Color.yellow.darker().darker(), true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.BACKGROUND_COLOR_4_PROPERTY_NAME, Color.blue, true, true);
//
//            ThemeHandler.setThemeFromName(ThemeHandler.TEXT_AREA_BACKGROUND_1_PROPERTY_NAME, new Color(255, 110, 199), true, true);
//            ThemeHandler.setThemeFromName(ThemeHandler.TEXT_AREA_BACKGROUND_2_PROPERTY_NAME, new Color(255, 110, 199).darker(), true, true);
//        } catch (Exception e) {
//
//        }
    }

}
