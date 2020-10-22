/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor;

/**
 *
 * @author parke
 */
public class InterruptHandler {

    private static int maskLevel = 0;

    private static int entryLevel = 1;

    public static boolean hasInterrupt() {
        return false;
    }

    /**
     *
     * @param level level of the interrupt ()
     * @param value
     */
    public static void throwNewInterrupt(int level, int value) {

    }

}
