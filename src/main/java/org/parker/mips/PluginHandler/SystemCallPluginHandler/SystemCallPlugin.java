/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.PluginHandler.SystemCallPluginHandler;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.parker.mips.GUI.MainGUI;
import org.parker.mips.Processor.Memory;
import org.parker.mips.Processor.Processor;
import org.parker.mips.Processor.Registers;
import org.parker.mips.Processor.SystemCallHandler;

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
     *
     * @param frame add the frame in a list accesiable in the Main_GUI
     */
    protected final void addFrameToGUI(SystemCallPluginFrame frame) {
        MainGUI.addSysCallFrameToList(this, frame);
    }

    /**
     * NOT YET IMPLEMENTED
     *
     */
    protected final void addInternalExamples() {

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
        SystemCallHandler.logRunTimeSystemCallError(message);
    }

    /**
     *
     * @param message the message that will be logged as a warning
     */
    protected final void logRunTimeSystemCallWarning(String message) {
        SystemCallHandler.logRunTimeSystemCallWarning(message);
    }

    /**
     *
     * @param message the message that will be logged
     */
    protected final void logRunTimeSystemCallMessage(String message) {
        SystemCallHandler.logRunTimeSystemCallMessage(message);
    }

    /**
     * This returns an array of SystemCallData objects that are defined in a
     * json file
     *
     * The json file must be in the same package as the class and have the same
     * name
     *
     * @param classType
     * @return
     */
    protected final SystemCallData[] getSystemCallDataFromClass(Class classType) {
        SystemCallData[] data = null;

        String path = "/" + classType.getCanonicalName().replaceAll("\\.", "/") + ".json";

        try {
            Gson gson = new Gson();
            InputStream is = getClass().getResourceAsStream(path);
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String response = new String();
            try {
                for (String line; (line = br.readLine()) != null; response += line);
            } catch (Exception e) {

            }

            try {
                data = gson.fromJson(response, SystemCallData[].class);
            } catch (Exception e) {
                SystemCallPluginHandler.logPluginHandlerError("There was an error while parcing the SystemCallData: " + e);
            }

        } catch (Exception e) {
            SystemCallPluginHandler.logPluginHandlerError("There was an error while loading the SystemCallData json file");
        }
        return data;
    }

    /**
     *
     * @return
     */
    protected final boolean throwBreakPoint() {

        if (MainGUI.canBreak()) {
            stopProcessor();
            logRunTimeSystemCallMessage("Program has reached a breakpoint");
        }

        return MainGUI.canBreak();
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
    protected final int getWord(int index) {
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
