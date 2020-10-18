/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.PluginHandler.SystemCallPluginHandler;

/**
 *
 * @author parke
 */
public class SystemCallData {

    public final int SYSTEM_CALL_NUMBER;
    public final String SYSTEM_CALL_NAME;
    public final String SYSTEM_CALL_DISCRIPTION;
    public final int[] REGISTERS_READ_FROM;
    public final int[] REGISTERS_WRITTEN_TO;
    public final boolean PC_REG_READ_FROM;
    public final boolean PC_REG_WRITTEN_TO;
    public final boolean HIGH_REG_READ_FROM;
    public final boolean HIGH_REG_WRITTEN_TO;
    public final boolean LOW_REG_READ_FROM;
    public final boolean LOW_REG_WRITTEN_TO;
    public final boolean MEMORY_READ_FROM;
    public final boolean MEMORY_WRITTEN_TO;

    public SystemCallData(int systemCallNum, String systemCallName, String systemCallDis, int[] registerReadFrom, int[] registersWrittenTo, boolean pcRegReadFrom, boolean pcRegWrittenTo, boolean highRegReadFrom, boolean highRegWrittenTo, boolean lowRegReadFrom, boolean lowRegWrittenTo, boolean memoryReadFrom, boolean memoryWrittenTo) {
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
