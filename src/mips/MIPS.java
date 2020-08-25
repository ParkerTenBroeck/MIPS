/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.Main_GUI;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class MIPS {

    public static final String VERSION = "0.9";
    public static final String[] INSTRUCTIONS = new String[]{"add", "addu", "addi", "addiu", "and", "andi", "div", "divu", "mult", "multu", "nor", "or", "ori", "sll", "sllv", "sra", "srav", "srl", "srlv", "sub", "subu", "xor", "xori", "lhi", "llo", "slt", "sltu", "slti", "sltiu", "beq", "bgtz", "ble", "bne", "j", "jal", "jalr", "jr", "lb", "lbu", "lh", "lhu", "lw", "sb", "sh", "sw", "mfhi", "mflo", "mthi", "mtlo", "trap"};
    public static final String LATEST_JSON_LINK = "https://api.github.com/repos/ParkerTenBroeck/MIPS/releases";

    public static void main(String[] args) {
        Log.initLogger();
        ResourceHandler.extractResources();
        Main_GUI gui = new Main_GUI();

        readJSONFeed(LATEST_JSON_LINK);
    }

    public static String readJSONFeed(String urls) {
        try {
            URL url = new URL(urls);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            InputStream inputStream = http.getInputStream();

            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            System.out.println(sb.toString());
            return sb.toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MIPS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
