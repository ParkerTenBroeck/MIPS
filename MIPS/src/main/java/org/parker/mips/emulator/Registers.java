/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.emulator;

/**
 *
 * @author parke
 */
public class Registers {

    protected  static int[] registers = new int[32];

    protected  static int low;
    protected  static int high;

    protected  static int pc;

    public static int getRegister(int register) {
        if (register == 0) {
            return 0;
        } else if (register > 32) {
            return -1;
        } else {
            return Registers.registers[register];
        }
    }

    public static int getLow() {
        return Registers.low;
    }

    public static int getHigh() {
        return Registers.high;
    }

    public static int getPc() {
        return Registers.pc;
    }

    public static boolean setRegister(int register, int val) {
        if (register == 0) {
            return false;
        } else if (register > 32) {
            return false;
        } else {
            Registers.registers[register] = val;
            return true;
        }
    }

    public static void setLow(int val) {
        Registers.low = val;
    }

    public static void setHigh(int val) {
        Registers.high = val;
    }

    public static void setPc(int val) {
        Registers.pc = val;
    }

    static void reset() {
        registers = new int[32];
        low = 0;
        high = 0;
        pc = 0;

    }
}
