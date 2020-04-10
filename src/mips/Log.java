/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

import GUI.Main_GUI;
import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author parke
 */
public class Log {
    
    
    public static void clearDisplay(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
         Main_GUI.appendMessageToVirtualConsoleLog("\n\n\n\n\n\n\n\n\n\n\n\n\n", null);
    }

    public static void logError(String message) {
        System.err.println("[Error] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.RED);
        StyleConstants.setBold(att, true);
        Main_GUI.appendMessageToVirtualConsoleLog("[Error] " + message, att);
    }

    public static void logWarning(String message) {
        System.out.println("[Warning] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.YELLOW);
        StyleConstants.setBold(att, true);
        Main_GUI.appendMessageToVirtualConsoleLog("[Warning] " + message, att);
    }

    public static void logMessage(String message) {
        System.out.println("[Message] " + message);

        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, true);
        Main_GUI.appendMessageToVirtualConsoleLog("[Message] " + message, att);
    }
}
