/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.processor;

/**
 *
 * @author parke
 */
public class Registers {

    private static int[] registers = new int[31];

    private static int low;
    private static int high;

    private static int pc;

    public static int getRegister(int register) {
        if (register == 0) {
            return 0;
        } else if (register > 32) {
            return -1;
        } else {
            return Registers.registers[register - 1];
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
            Registers.registers[register - 1] = val;
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
        registers = new int[31];
        low = 0;
        high = 0;
        pc = 0;

    }
}
