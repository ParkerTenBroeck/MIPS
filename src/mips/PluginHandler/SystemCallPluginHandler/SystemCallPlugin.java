/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.PluginHandler.SystemCallPluginHandler;

import mips.processor.Memory;
import mips.processor.Processor;
import mips.processor.Registers;
import mips.processor.SystemCallHandler;

/**
 *
 * @author parke
 */
public abstract class SystemCallPlugin {

    final protected SystemCall[] systemCalls;
    final public String PLUGIN_NAME;

    public SystemCallPlugin(int numOfSystemCalls, String pluginName) {
        this.systemCalls = new SystemCall[numOfSystemCalls];
        this.PLUGIN_NAME = pluginName;
    }

    /**
     * This will be call once after class is instantiated use this for anything
     * that can not be done in constructor
     *
     */
    public abstract void init();

    public final SystemCall[] getSystemCalls() {
        return systemCalls;
    }

    /**
     * WARNING errors can halt the program if enabled use Warning if program can
     * continue
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallError(String message) {
        SystemCallHandler.loadRunTimeSystemCallError(message);
    }

    /**
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallWarning(String message) {
        SystemCallHandler.loadRunTimeSystemCallWarning(message);
    }

    /**
     *
     * @param message the message that will be logged
     */
    protected final void logRunTimeSystemCallMessage(String message) {
        SystemCallHandler.loadRunTimeSystemCallMessage(message);
    }

    /**
     * halts the processor the processor can only be started again by the user
     *
     */
    protected final void stopProcessor() {
        Processor.stop();
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the full word (4 bytes) that will be stored at the index
     */
    protected final void setWord(int index, int val) {
        Memory.setWord(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the half word (2 bytes) that will be stored at the index
     */
    protected final void setHelfWord(int index, int val) {
        Memory.setHalfWord(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data is being stored at
     * @param val the byte (1 bytes) that will be stored at the index
     */
    protected final void setByte(int index, int val) {
        Memory.setByte(index, val);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a full word (4 bytes) at the location in memory of index
     */
    protected final int getWorld(int index) {
        return Memory.getWord(index);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a half word (2 bytes) at the location in memory of index
     */
    protected final int getHalfWord(int index) {
        return Memory.getHalfWord(index);
    }

    /**
     *
     * @param index the index in bytes where the data being retrieved is held
     * @return returns a byte (1 bytes) at the location in memory of index
     */
    protected final int getByte(int index) {
        return Memory.getByte(index);
    }

    /**
     *
     * @param reg a value from 0 - 31 that represents the register number
     * @return returns the value held at the register
     */
    protected final int getRegister(int reg) {
        return Registers.getRegister(reg);

    }

    /**
     * sets the specified register to the value of val
     *
     * @param reg a value from 0 - 31 that represents the register number
     * @param val the integer that will be placed in the specified register
     */
    protected final void setRegister(int reg, int val) {
        Registers.setRegister(reg, val);
    }

    /**
     *
     * @return returns the current value of the Program counter
     */
    protected final int getPc() {
        return Registers.getPc();
    }

    /**
     *
     * @param val the value that the Program Counter will be set to
     */
    protected final void setPc(int val) {
        Registers.setPc(val);
    }

    /**
     *
     * @return returns the current value of the HIGH register
     */
    protected final int getHigh() {
        return Registers.getHigh();
    }

    /**
     *
     * @param val the value the HIGH register will be set to
     */
    protected final void setHigh(int val) {
        Registers.setHigh(val);
    }

    /**
     *
     * @return returns the current value of the LOW register
     */
    protected final int getLow() {
        return Registers.getLow();
    }

    /**
     *
     * @param val the value the LOW register will be set to
     */
    protected final void setLow(int val) {
        Registers.setLow(val);
    }

    protected final void throwNonInturuptableIntturupt() { //still in the works

    }

}
