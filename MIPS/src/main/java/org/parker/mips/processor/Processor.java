/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.processor;

import org.parker.mips.Log;
import org.parker.mips.OptionsHandler;
import org.parker.mips.gui.MainGUI;

import static org.parker.mips.processor.InstructionDecode.runInstruction;
import static org.parker.mips.processor.Memory.getWord;
import static org.parker.mips.processor.Registers.getPc;

/**
 *
 * @author parke
 */
public class Processor implements Runnable {

    private static boolean isRunning = false;
    private static long instructionsRan = 0;
    private static long delay;

    public static long getInstructionsRan() {
        return instructionsRan;
    }

    public static synchronized void stop() {
        isRunning = false;
        MainGUI.stopAutoUpdate();
    }

    public static void reset() {
        stop();
        instructionsRan = 0;
        Registers.reset();
        if (OptionsHandler.reloadMemoryOnReset.val()) {
            Memory.reloadMemory();
        }
        MainGUI.refresh();
    }

    public static void setDelay(long delay) {
        Processor.delay = delay;
    }

    public static synchronized long getDelay() {
        return delay;
    }

    public static synchronized boolean isRunning() {
        return isRunning;
    }

    public static synchronized void start() {
        if (!isRunning) {
            Processor runnable = new Processor();
            Thread thread = new Thread(runnable);
            thread.setName("Processor");
            isRunning = true;
            thread.start();
        }
    }

    @Override
    public void run() {
        while (isRunning()) {
            singleStep();
            delayNano(getDelay());
        }
    }

    private static void delayNano(long time) {
        if (time == 0) {
            return;
        }
        long start = System.nanoTime();
        long end = 0;
        do {
            end = System.nanoTime();
        } while (start + time >= end);
    }

    public static int getOpCode() {
        return getWord(getPc());
    }

    public static void runSingleStep() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        Thread thread = new Thread(() -> {
            singleStep();
            isRunning = false;
        });
        thread.start();
    }

    private static void singleStep() {
//        if (InterruptHandler.hasInterrupt() && !InterruptHandler.isInInterrupt()) {
//            int sp = 29;
//            Memory.setWord(Registers.getRegister(sp), Registers.getPc());
//            Registers.setRegister(sp, Registers.getRegister(sp) + 1);
//
//            Memory.setWord(Registers.getRegister(sp), Registers.getPc());
//            Registers.setRegister(sp, Registers.getRegister(sp) + 1);
//            return;
//        }

        if (!runInstruction(getOpCode())) {

            logRunTimeError("invalid OpCode at " + Registers.getPc());
        }
//        if (instructionsRan == 100000000) {
//            endTime = System.nanoTime();
//            duration = (endTime - startTime);
//
//            System.out.println("TotalTime: " + duration + " Av: " + duration / (double)instructionsRan + " ISP: " + (long)(100000000.0 / (double)duration * 1e+9));
//            instructionsRan = 0;
//            startTime = System.nanoTime();
//        }

//        if (index == temp.length) {
//            index = 0;
//            double av = 0;
//            long highest = 0;
//            long lowest = 999999999;
//            for (int i = 0; temp.length > i; i++) {
//                av += temp[i];
//                if (highest < temp[i]) {
//                    highest = temp[i];
//                }
//                if (lowest > temp[i]) {
//                    lowest = temp[i];
//                }
//            }
//            av = av / temp.length;
//            System.out.println("Average: " + av + " Highest: " + highest + " Lowest: " + lowest);
//        }
        instructionsRan++;
    }

//    static long startTime = System.nanoTime();
//    static long endTime = System.nanoTime();
//
//    static long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

    public static void logRunTimeError(String message) {
        if (OptionsHandler.breakOnRunTimeError.val()) {
            Processor.stop();
            MainGUI.refreshAll();
        }
        Log.logError("[RunTime] " + message);
    }

    public static void logRunTimeWarning(String message) {
        Log.logWarning("[RunTime] " + message);
    }

    public static void logRunTimeMessage(String message) {
        Log.logMessage("[RunTime] " + message);
    }

}
