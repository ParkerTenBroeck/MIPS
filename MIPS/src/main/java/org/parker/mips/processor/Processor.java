/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.processor;

import org.parker.mips.OptionsHandler;
import org.parker.mips.gui.MainGUI;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    private final static Logger LOGGER = Logger.getLogger(Processor.class.getName());

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
        do {
            try {
                runInstruction(Memory.getWord(Registers.pc));
            }catch(Exception e){
                LOGGER.log(RunTimeLevel.RUN_TIME_ERROR, e.getMessage(), e.getCause());
                if (OptionsHandler.breakOnRunTimeError.val()) {
                    Processor.stop();
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

        } while (isRunning);
    }

    public static int getOpCode() {
        return getWord(getPc());
    }

    public static void runSingleStep() {
        if (isRunning) {
            return;
        }
        Processor runnable = new Processor();
        Thread thread = new Thread(runnable);
        thread.setName("Processor");
        isRunning = false;
        thread.start();

        /*
        isRunning = true;
        Thread thread = new Thread(() -> {
            singleStep();
            isRunning = false;
        });
        thread.start();
         */
    }

    private static void singleStep() {


    }

    static long startTime = System.nanoTime();
    static long endTime = System.nanoTime();
//
    static long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

}
