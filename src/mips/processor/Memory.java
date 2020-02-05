/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.processor;

import GUI.Main_GUI;

/**
 *
 * @author parke
 */
public class Memory {

    private static byte[] memory = new byte[0];

    public static void setMemory(byte[] memory) {
        if (memory == null) {
            Memory.memory = new byte[0];
        } else {
            Memory.memory = memory;
        }
    }

    public static byte[] getMemory() {
        return memory;
    }

    public static int getWord(int index) {
        if (index + 3 > Memory.memory.length) {
            Main_GUI.stop();
            Main_GUI.infoBox("Error", "getWord Memory out of bounds " + index);
            return -1;
        } else {
            int mem4 = 0;
            int mem3 = 0;
            int mem2 = 0;
            int mem1 = 0;
            try {
                mem4 = Memory.memory[index] & 0xFF;
                mem3 = Memory.memory[index + 1] & 0xFF;
                mem2 = Memory.memory[index + 2] & 0xFF;
                mem1 = Memory.memory[index + 3] & 0xFF;
            } catch (Exception e) {

            }

            return mem1 | mem2 << 8 | mem3 << 16 | mem4 << 24;
        }
    }

    public static int getHalfWord(int index) {
        if (index + 1 > memory.length) {

            Main_GUI.stop();
            Main_GUI.infoBox("Error", "getHalfWord Memory out of bounds " + index);
            return -1;
        } else {

            int mem2 = Memory.memory[index] & 0xFF;
            int mem1 = Memory.memory[index + 1] & 0xFF;

            return mem1 | mem2 << 8;
        }
    }

    public static int getByte(int index) {
        if (index > Memory.memory.length - 1) {
            Main_GUI.stop();
            Main_GUI.infoBox("Error", "getByte Memory out of bounds " + index);
            return -1;
        } else {
            return Memory.memory[index] & 0xFF;
        }
    }

    public static boolean setWord(int index, int val) {
        if (index + 3 > Memory.memory.length) {
            Main_GUI.stop();
            Main_GUI.infoBox("Error", "setWord Memory out of bounds " + index);
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
        if (index + 1 > Memory.memory.length) {
            Main_GUI.stop();
            Main_GUI.infoBox("Error", "setHalfWord Memory out of bounds " + index);
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
        if (index > Memory.memory.length - 1) {
            Main_GUI.stop();
            Main_GUI.infoBox("Error", "setByte Memory out of bounds " + index);
            return false;
        } else {
            memory[index] = (byte) val;
            return true;
        }
    }

    public static int getSize() {
        return memory.length;
    }

}
