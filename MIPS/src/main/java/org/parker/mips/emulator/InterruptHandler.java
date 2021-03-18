/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.emulator;

import java.util.ArrayList;

/**
 *
 * @author parke
 */
public class InterruptHandler {

    private static int maskLevel = 0; //default zero (only non maskable inturrupts enabled)

    private static int entryPoint = 1;

    private static ArrayList<Interrupt> interrupts;

    private static boolean inInterrupt = false;
    
    private static boolean vectorFlag = false;

    public static boolean isInInterrupt() {
        return inInterrupt;
    }

    public static void setEntryPoint(int entryPoint) {
        InterruptHandler.entryPoint = entryPoint;
    }

    public static int getEntryPoint() {
        return entryPoint;
    }

    public static boolean hasInterrupt() {
        return false;
        //return !interrupts.isEmpty();
    }

    public static Interrupt getNextInterrupt() {
        Interrupt temp = interrupts.get(0);
        inInterrupt = true;
        return temp;
    }

    /**
     *
     * @param level level of the interrupt ()
     * @param value
     */
    public static void throwNewInterrupt(int level, int value) {
        if (level > maskLevel) {
            return;
        }
        interrupts.add(new Interrupt(level, value));
    }

    public static void setMaskLevel(int level) {
        if (level < 0) {
            level = 0;
        }
        maskLevel = level;
    }

}

class Interrupt {

    public final int level;
    public final int value;

    public Interrupt(int level, int value) {
        this.level = level;
        this.value = value;
    }
}
