/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips;

/**
 *
 * @author parke
 */
public class Log {

    public static void logError(String message) {
        System.err.println("[Error] " + message);
    }

    public static void logWarning(String message) {
        System.out.println("[Warning] " + message);
    }

    public static void logMessage(String message) {
        System.out.println("[message] " + message);
    }
}
