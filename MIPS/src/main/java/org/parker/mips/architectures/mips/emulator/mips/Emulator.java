/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.architectures.mips.emulator.mips;

import org.parker.mips.architectures.mips.emulator.RunTimeLevel;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.preferences.Preferences;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parker.mips.architectures.mips.emulator.mips.InstructionDecode.runInstruction;
import static org.parker.mips.architectures.mips.emulator.mips.Memory.getWord;
import static org.parker.mips.architectures.mips.emulator.mips.Registers.getPc;

/**
 *
 * @author parke
 */
public class Emulator implements Runnable {

    private static boolean isRunning = false;
    private static long instructionsRan = 0;
    private static long delay;

    private final static Logger LOGGER = Logger.getLogger(Emulator.class.getName());

    private final static Preferences processorPrefs = Preferences.ROOT_NODE.getNode("system/emulator");

    public static long getInstructionsRan() {
        return instructionsRan;
    }

    public static synchronized void stop() {
        if(isRunning){
            LOGGER.log(Level.INFO, "Processor Halted");
            isRunning = false;
            MainGUI.stopAutoUpdate();
        }
    }

    public static void reset() {
        stop();
        instructionsRan = 0;
        Registers.reset();
        if ((Boolean)processorPrefs.getPreference("reloadMemoryOnReset", false)) {
            Memory.reloadMemory();
        }
        MainGUI.refresh();
    }

    public static void setDelay(long delay) {
        Emulator.delay = delay;
    }

    public static synchronized long getDelay() {
        return delay;
    }

    public static synchronized boolean isRunning() {
        return isRunning;
    }

    public static synchronized void start() {
        if (!isRunning) {
            Emulator runnable = new Emulator();
            Thread thread = new Thread(runnable);
            thread.setName("Processor");
            isRunning = true;
            thread.start();
        }
    }

    @Override
    public void run() {
        do {
            try {
                runInstruction(Memory.getWord(Registers.pc));
            }catch(Exception e){
                LOGGER.log(RunTimeLevel.RUN_TIME_ERROR, e.getMessage(), e.getCause());
                if ((Boolean)processorPrefs.getPreference("breakOnRunTimeError", true)) {
                    Emulator.stop();
                    MainGUI.refreshAll();
                }
            }
            instructionsRan++;

            if(delay != 0){
                long start = System.nanoTime();
                long end = 0;
                do {
                    end = System.nanoTime();
                } while (start + delay >= end);
            }

        if (instructionsRan == 100000000) {
            endTime = System.nanoTime();
            duration = (endTime - startTime);

            System.out.println("TotalTime: " + duration + " Av: " + duration / (double)instructionsRan + " ISP: " + (long)(100000000.0 / (double)duration * 1e+9));
            instructionsRan = 0;
            startTime = System.nanoTime();
        }

        } while (isRunning);

        MainGUI.refreshAll();
    }

    public static int getOpCode() {
        return getWord(getPc());
    }

    public static void runSingleStep() {
        if (isRunning) {
            return;
        }
        Emulator runnable = new Emulator();
        Thread thread = new Thread(runnable);
        thread.setName("Processor");
        isRunning = false;
        thread.start();
    }

    static long startTime = System.nanoTime();
    static long endTime = System.nanoTime();
//
    static long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

}
