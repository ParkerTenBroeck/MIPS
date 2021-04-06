/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.emulator;

import org.parker.mips.preferences.Preference;
import org.parker.mips.preferences.Preferences;

import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class Memory {

    private static byte[] savedMemory;
    protected static byte[] memory = new byte[0];

    private static final Logger LOGGER = Logger.getLogger(Memory.class.getName());

    private static final Preference<Boolean> adaptiveMemory = Preferences.ROOT_NODE.getNode("system/emulator/runtime").getRawPreference("adaptiveMemory", true);

    private static final boolean bigEndian = true;

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
            throw new RunTimeMemoryException("getWord must be alligned to 4 error at index:" + index);
        }
        if (index + 3 > Memory.memory.length) {
            memoryOutOfBoundsEvent(index);
            return -1;
        } else {

            return superGetWord(index, bigEndian);
        }
    }

    public static int getHalfWord(int index) {
        if ((index & 1) != 0) {
            throw new RunTimeMemoryException("getHalfWord must be alligned to 2 error at index:" + index);
        }
        if (index + 1 > memory.length) {
            memoryOutOfBoundsEvent(index);
            return -1;
        } else {

            return superGetHalfWord(index, bigEndian);
        }
    }

    public static int getByte(int index) {
        if (index > Memory.memory.length - 1) {
            memoryOutOfBoundsEvent(index);
            return -1;
        } else {
            return superGetByte(index, bigEndian);
        }
    }

    public static int superGetWord(int index, Boolean bigEndianness) { //doesnt throw an error when trying to get memory out of bounds

        if(bigEndianness == null){
            bigEndianness = bigEndian;
        }

        int mem4 = 0xCD;
        int mem3 = 0xCD;
        int mem2 = 0xCD;
        int mem1 = 0xCD;
        if (index >= 0 && memory.length > index + 3){
            if(bigEndianness) {
                mem4 = Memory.memory[index] & 0xFF;
                mem3 = Memory.memory[index + 1] & 0xFF;
                mem2 = Memory.memory[index + 2] & 0xFF;
                mem1 = Memory.memory[index + 3] & 0xFF;
            }else{
                mem4 = Memory.memory[index + 3] & 0xFF;
                mem3 = Memory.memory[index + 2] & 0xFF;
                mem2 = Memory.memory[index + 1] & 0xFF;
                mem1 = Memory.memory[index] & 0xFF;
            }
        }
        return mem1 | mem2 << 8 | mem3 << 16 | mem4 << 24;
    }

    public static int superGetHalfWord(int index, Boolean bigEndianness) { //doesnt throw an error when trying to get memory out of bounts

        if(bigEndianness == null){
            bigEndianness = bigEndian;
        }

        int mem1 = 0xCD;
        int mem2 = 0xCD;
        if (index >= 0 && memory.length > index + 1){
            if(bigEndianness) {
                mem2 = ((int) Memory.memory[index]);
                mem1 = ((int) Memory.memory[index + 1]) & 0xFF;
            }else{
                mem2 = ((int) Memory.memory[index + 1]) & 0xFF;
                mem1 = ((int) Memory.memory[index]);
            }
        }
        return mem1 | mem2 << 8;
    }

    public static int superGetByte(int index, Boolean bigEndianness) {

        int mem1 = 0xCD;
        if (index >= 0 && memory.length > index){
            mem1 = Memory.memory[index];
        }
        return mem1;

    }

    public static boolean setWord(int index, int val) {
        if ((index & 3) != 0) {
            throw new RunTimeMemoryException("setWord must be alligned to 4 error at index:" + index);
        }
        if (index + 3 > Memory.memory.length || index < 0) {
            memoryOutOfBoundsEvent(index);
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
            throw new RunTimeMemoryException("setHalfWord must be alligned to 2 error at index:" + index);
        }
        if (index + 1 > Memory.memory.length || index < 0) {
            Emulator.stop();
            memoryOutOfBoundsEvent(index);
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
        if (index > Memory.memory.length - 1 || index < 0) {
            Emulator.stop();
            memoryOutOfBoundsEvent(index);
            return false;
        } else {
            memory[index] = (byte) val;
            return true;
        }
    }

    public static void superSetWord(int index, int val){
        if(index < 0){
            return;
        }
        if(memory.length <= val + 3){
            resizeMemory(index);
        }

        byte mem1 = (byte) (val & 0xFF);
        byte mem2 = (byte) ((val >>> 8) & 0xFF);
        byte mem3 = (byte) ((val >>> 16) & 0xFF);
        byte mem4 = (byte) ((val >>> 24) & 0xFF);

        memory[index] = mem4;
        memory[index + 1] = mem3;
        memory[index + 2] = mem2;
        memory[index + 3] = mem1;
    }

    public static void superSetHalfWord(int index, int val){
        if(index < 0){
            return;
        }
        if(memory.length <= val + 1){
            resizeMemory(index);
        }
        byte mem1 = (byte) (val & 255);
        byte mem2 = (byte) ((val >> 8) & 255);

        memory[index] = mem2;
        memory[index + 1] = mem1;
    }

    public static void superSetByte(int index, int val) {
        if(index < 0){
            return;
        }
        if(memory.length <= index){
            resizeMemory(index);
        }
            memory[index] = (byte) val;
    }

    private static void memoryOutOfBoundsEvent(int currentIndex) {
        if (adaptiveMemory.val()) {
            resizeMemory(currentIndex);
        } else {
           throw new RunTimeMemoryException("Memory out of bounds at: " + currentIndex);
        }
    }

    private static void resizeMemory(int currentIndex){
        byte[] temp;
        if (currentIndex >= memory.length * 2) {
            temp = new byte[currentIndex + 16];
        } else {
            temp = new byte[memory.length * 2];
        }
        System.arraycopy(memory, 0, temp, 0, memory.length);
        memory = temp;
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
