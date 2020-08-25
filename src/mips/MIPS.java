/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.Main_GUI;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mips.UpdateHandler.checkForUpdates;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9.1";
    public static final String JAR_PATH;
    public static final String[] INSTRUCTIONS = new String[]{"add", "addu", "addi", "addiu", "and", "andi", "div", "divu", "mult", "multu", "nor", "or", "ori", "sll", "sllv", "sra", "srav", "srl", "srlv", "sub", "subu", "xor", "xori", "lhi", "llo", "slt", "sltu", "slti", "sltiu", "beq", "bgtz", "ble", "bne", "j", "jal", "jalr", "jr", "lb", "lbu", "lh", "lhu", "lw", "sb", "sh", "sw", "mfhi", "mflo", "mthi", "mtlo", "trap"};

    static {
        String tmp;
        try {
            tmp = URLDecoder.decode(MIPS.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            tmp = "";
        }
        JAR_PATH = tmp;
    }

    public static void main(String[] args) {

        Log.initLogger();
        ResourceHandler.extractResources();
        Main_GUI gui = new Main_GUI();

        checkForUpdates();
    }
}
