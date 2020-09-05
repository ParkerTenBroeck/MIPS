/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.Main_GUI;
import java.io.File;
import static mips.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.4";
    public static final String JAR_PATH;
    //public static final String[] INSTRUCTIONS = new String[]{"add", "addu", "addi", "addiu", "and", "andi", "div", "divu", "mult", "multu", "nor", "or", "ori", "sll", "sllv", "sra", "srav", "srl", "srlv", "sub", "subu", "xor", "xori", "lhi", "llo", "slt", "sltu", "slti", "sltiu", "beq", "bgtz", "ble", "bne", "j", "jal", "jalr", "jr", "lb", "lbu", "lh", "lhu", "lw", "sb", "sh", "sw", "mfhi", "mflo", "mthi", "mtlo", "trap"};

    static {
        String tmp;
        try {
            tmp = new File(MIPS.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getAbsolutePath();
        } catch (Exception ex) {
            tmp = "";

        }
        //Log.logMessage(tmp);
        JAR_PATH = tmp;
    }

    public static void main(String[] args) {
        ResourceHandler.extractResources();
        Main_GUI gui = new Main_GUI();
        //static int temp = 0;

        checkForUpdates();

//        try {
//            Thread.sleep(1000);
//            ThemeHandler.setThemeFromName(ThemeHandler.BUTTON_DEFAULT_COLOR_PROPERTY_NAME, Color.yellow, true, true);
//        } catch (Exception e) {
//
//        }
    }

}
