/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.SystemCall;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * systemCallNum must me a completely unique int between zero and 2^26 this
 * value is what will be used in the compilation process of the assembly if this
 * value conflicts with any other system call registered it will throw warning
 * and will not be used
 *
 * systemCallName is a unique name that describes what the system call does it
 * must be relativly short with no spaces this is what can be used when the
 * standardHeader is used to represent the number of the system call
 *
 *
 *
 * @return
 * @author parke
 */
public abstract class SystemCall {

    public final SystemCallData DATA;
    public final SystemCallPlugin HOST_PLUGIN;

    /**
     *
     * @param data the data that is accociated with this system call
     * @param systemCallName the unique string that represents the system call
     * (this MUST be the same as defined in system call data this is used for
     * verification that the right data is being used with the right system
     * call)
     * @param hostPlugin this is used to keep track of what system call belongs
     * to what plugin
     */
    public SystemCall(SystemCallData data, String systemCallName, SystemCallPlugin hostPlugin) {
        this.DATA = data;
        this.HOST_PLUGIN = hostPlugin;

        if (DATA.SYSTEM_CALL_NAME.contains(" ")) {
            SystemCallPluginLoader.logPluginLoaderError("System Call Name: "
                    + DATA.SYSTEM_CALL_DISCRIPTION + " Cannot contain any space characters");
            throw new IllegalArgumentException("System Call Name: "
                    + DATA.SYSTEM_CALL_DISCRIPTION + " Cannot contain any space characters");
        }
        if (!this.DATA.SYSTEM_CALL_NAME.equals(systemCallName)) {
            SystemCallPluginLoader.logPluginLoaderError("Loaded System Call Data Name: "
                    + this.DATA.SYSTEM_CALL_NAME + " DOES NOT match Entered System Call Name: " + systemCallName);
            throw new IllegalArgumentException("Loaded System Call Data Name: "
                    + this.DATA.SYSTEM_CALL_NAME + " DOES NOT match Entered System Call Name: " + systemCallName);
        }
    }

    public abstract void handleSystemCall();

    public class SystemCallData {

        public final int SYSTEM_CALL_NUMBER;
        public final String SYSTEM_CALL_NAME;
        public final String SYSTEM_CALL_DISCRIPTION;
        public final Integer[] REGISTERS_READ_FROM;
        public final Integer[] REGISTERS_WRITTEN_TO;
        public final boolean PC_REG_READ_FROM;
        public final boolean PC_REG_WRITTEN_TO;
        public final boolean HIGH_REG_READ_FROM;
        public final boolean HIGH_REG_WRITTEN_TO;
        public final boolean LOW_REG_READ_FROM;
        public final boolean LOW_REG_WRITTEN_TO;
        public final boolean MEMORY_READ_FROM;
        public final boolean MEMORY_WRITTEN_TO;

        public SystemCallData(int systemCallNum, String systemCallName, String systemCallDis, Integer[] registerReadFrom, Integer[] registersWrittenTo, boolean pcRegReadFrom, boolean pcRegWrittenTo, boolean highRegReadFrom, boolean highRegWrittenTo, boolean lowRegReadFrom, boolean lowRegWrittenTo, boolean memoryReadFrom, boolean memoryWrittenTo) {
            this.SYSTEM_CALL_NUMBER = systemCallNum;
            this.SYSTEM_CALL_NAME = systemCallName;
            this.SYSTEM_CALL_DISCRIPTION = systemCallDis;
            this.REGISTERS_READ_FROM = registerReadFrom;
            this.REGISTERS_WRITTEN_TO = registersWrittenTo;
            this.PC_REG_READ_FROM = pcRegReadFrom;
            this.PC_REG_WRITTEN_TO = pcRegWrittenTo;
            this.HIGH_REG_READ_FROM = highRegReadFrom;
            this.HIGH_REG_WRITTEN_TO = highRegWrittenTo;
            this.LOW_REG_READ_FROM = lowRegReadFrom;
            this.LOW_REG_WRITTEN_TO = lowRegWrittenTo;
            this.MEMORY_READ_FROM = memoryReadFrom;
            this.MEMORY_WRITTEN_TO = memoryWrittenTo;
        }

    }
}
