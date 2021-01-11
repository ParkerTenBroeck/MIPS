/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.processor;

import org.parker.mips.OptionsHandler;
import static org.parker.mips.processor.Processor.logRunTimeError;

/**
 *
 * @author parke
 */
public class Memory {

    private static byte[] savedMemory;
    private static byte[] memory = new byte[0];

    public static void setMemory(byte[] memory) {
        if (memory == null) {
            Memory.savedMemory = new byte[0];
        } else {
            Memory.savedMemory = memory;
        }
        reloadMemory();
    }

    public static byte[] getMemory() {
        return memory;
    }

    public static int getWord(int index) {
        if ((index & 3) != 0) {
            logRunTimeError("getWord must be alligned to 4 error at index:" + index);
        }
        if (index + 3 > Memory.memory.length) {
            memoryOutOfBoundsEvent("getWord Memory out of bounds", index);
            return -1;
        } else {

            return superGetWord(index);
        }
    }

    public static int getHalfWord(int index) {
        if ((index & 1) != 0) {
            logRunTimeError("getHalfWord must be alligned to 2 error at index:" + index);
        }
        if (index + 1 > memory.length) {
            memoryOutOfBoundsEvent("getHalfWord Memory out of bounds", index);
            return -1;
        } else {

            return superGetHalfWord(index);
        }
    }

    public static int getByte(int index) {
        if (index > Memory.memory.length - 1) {
            memoryOutOfBoundsEvent("getByte Memory out of bounds", index);
            return -1;
        } else {
            return superGetByte(index);
        }
    }

    public static int superGetWord(int index) { //doesnt throw an error when trying to get memory out of bounds

        int mem4 = 0xCD;
        int mem3 = 0xCD;
        int mem2 = 0xCD;
        int mem1 = 0xCD;
        try {
            mem4 = Memory.memory[index] & 0xFF;
            mem3 = Memory.memory[index + 1] & 0xFF;
            mem2 = Memory.memory[index + 2] & 0xFF;
            mem1 = Memory.memory[index + 3] & 0xFF;

        } catch (Exception e) {

        }
        return mem1 | mem2 << 8 | mem3 << 16 | mem4 << 24;
    }

    public static int superGetHalfWord(int index) { //doesnt throw an error when trying to get memory out of bounts

        int mem1 = 0xCD;
        int mem2 = 0xCD;
        try {
            mem2 = ((int) Memory.memory[index]);
            mem1 = ((int) Memory.memory[index + 1]) & 0xFF;
        } catch (Exception e) {

        }
        return mem1 | mem2 << 8;
    }

    public static int superGetByte(int index) {

        int mem1 = 0xCD;
        try {
            mem1 = Memory.memory[index];
        } catch (Exception e) {

        }
        return mem1;

    }

    public static boolean setWord(int index, int val) {
        if ((index & 3) != 0) {
            logRunTimeError("setWord must be alligned to 4 error at index:" + index);
        }
        if (index + 3 > Memory.memory.length || index < -1) {
            memoryOutOfBoundsEvent("setWord Memory out of bounds", index);
            return false;
        } else {

            byte mem1 = (byte) (val & 0xFF);
            byte mem2 = (byte) ((val >>> 8) & 0xFF);
            byte mem3 = (byte) ((val >>> 16) & 0xFF);
            byte mem4 = (byte) ((val >>> 24) & 0xFF);

            memory[index] = mem4;
            memory[index + 1] = mem3;
            memory[index + 2] = mem2;
            memory[index + 3] = mem1;

            return true;
        }
    }

    public static boolean setHalfWord(int index, int val) {
        if ((index & 1) != 0) {
            logRunTimeError("setHalfWord must be alligned to 2 error at index:" + index);
        }
        if (index + 1 > Memory.memory.length || index < -1) {
            Processor.stop();
            memoryOutOfBoundsEvent("setHalfWord Memory out of bounds", index);
            return false;
        } else {

            byte mem1 = (byte) (val & 255);
            byte mem2 = (byte) ((val >> 8) & 255);

            memory[index] = mem2;
            memory[index + 1] = mem1;

            return true;
        }
    }

    public static boolean setByte(int index, int val) {
        if (index > Memory.memory.length - 1 || index < -1) {
            Processor.stop();
            memoryOutOfBoundsEvent("setByte Memory out of bounds", index);
            return false;
        } else {
            memory[index] = (byte) val;
            return true;
        }
    }

    private static void memoryOutOfBoundsEvent(String message, int currentIndex) {
        if (OptionsHandler.adaptiveMemory.val()) {
            byte[] temp;
            if (currentIndex >= memory.length * 2) {
                temp = new byte[currentIndex + 16];
            } else {
                temp = new byte[memory.length * 2];
            }
            for (int i = 0; i < memory.length; i++) {
                temp[i] = memory[i];
            }
            memory = temp;
        } else {
            logRunTimeError(message + " " + currentIndex);
        }
    }

    public static int getSize() {
        if (memory != null) {
            return memory.length;
        } else {
            return 0;
        }
    }

    public static void reloadMemory() {
        Memory.memory = Memory.savedMemory;
    }

}
