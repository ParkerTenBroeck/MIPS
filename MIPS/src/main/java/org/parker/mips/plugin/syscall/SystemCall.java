/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;


import java.util.ArrayList;
import java.util.Map;

/**
 * systemCallNum must me a completely unique int between zero and 2^26 this
 * value is what will be used in the compilation process of the assembly if this
 * value conflicts with any other system call registered it will throw warning
 * and will not be used
 * <p>
 * systemCallName is a unique name that describes what the system call does it
 * must be relatively short with no spaces this is what can be used when the
 * standardHeader is used to represent the number of the system call
 *
 * @author parke
 */
public abstract class SystemCall {

    public final SystemCallData DATA;
    public final SystemCallPlugin HOST_PLUGIN;

    /**
     * @param data           the data that is accosted with this system call
     * @param systemCallName the unique string that represents the system call
     *                       (this MUST be the same as defined in system call data this is used for
     *                       verification that the right data is being used with the right system
     *                       call)
     * @param hostPlugin     this is used to keep track of what system call belongs
     *                       to what plugin
     */
    public SystemCall(SystemCallData data, String systemCallName, SystemCallPlugin hostPlugin) {
        this.DATA = data;
        this.HOST_PLUGIN = hostPlugin;

        if (DATA == null) {
            throw new IllegalArgumentException("DATA cannot be NULL");
        }
        if ((DATA.SYSTEM_CALL_NAME == null) || (DATA.SYSTEM_CALL_NAME.equals(""))) {
            throw new IllegalArgumentException("SystemCall Name cannot be NULL or Empty");
        }

        if (DATA.SYSTEM_CALL_NAME.contains(" ")) {
            throw new IllegalArgumentException("System Call Name: "
                    + DATA.SYSTEM_CALL_DESCRIPTION + " Cannot contain any space characters");
        }
        if (!this.DATA.SYSTEM_CALL_NAME.equals(systemCallName)) {
            throw new IllegalArgumentException("Loaded System Call Data Name: "
                    + this.DATA.SYSTEM_CALL_NAME + " DOES NOT match Entered System Call Name: " + systemCallName);
        }
    }

    public abstract void handleSystemCall();

    public static class SystemCallData {

        public final int SYSTEM_CALL_NUMBER;
        public final String SYSTEM_CALL_NAME;
        public final String SYSTEM_CALL_DESCRIPTION;
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


        @SuppressWarnings("unchecked")
        public SystemCallData(Map<String, Object> data) {

            try {
                this.SYSTEM_CALL_NAME = (String) data.get("SYSTEM_CALL_NAME");
            } catch (Exception e) {
                throw new InvalidSystemCallException("SYSTEM_CALL_NAME field is missing from the yml");
            }
            try {
                this.SYSTEM_CALL_NUMBER = (int) data.get("SYSTEM_CALL_NUMBER");
            } catch (Exception e) {
                throw new InvalidSystemCallException("SYSTEM_CALL_NUMBER field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.SYSTEM_CALL_DESCRIPTION = (String) data.get("SYSTEM_CALL_DESCRIPTION");

            } catch (Exception e) {
                throw new InvalidSystemCallException("SYSTEM_CALL_DESCRIPTION field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.REGISTERS_READ_FROM = ((ArrayList<Integer>) data.get("REGISTERS_READ_FROM")).toArray(new Integer[0]);
            } catch (Exception e) {
                throw new InvalidSystemCallException("REGISTERS_READ_FROM field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.REGISTERS_WRITTEN_TO = ((ArrayList<Integer>) data.get("REGISTERS_WRITTEN_TO")).toArray(new Integer[0]);
            } catch (Exception e) {
                throw new InvalidSystemCallException("REGISTERS_WRITTEN_TO field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.PC_REG_READ_FROM = (boolean) data.get("PC_REG_READ_FROM");

            } catch (Exception e) {
                throw new InvalidSystemCallException("PC_REG_READ_FROM field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.PC_REG_WRITTEN_TO = (boolean) data.get("PC_REG_WRITTEN_TO");

            } catch (Exception e) {
                throw new InvalidSystemCallException("PC_REG_WRITTEN_TO field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.HIGH_REG_READ_FROM = (boolean) data.get("HIGH_REG_READ_FROM");

            } catch (Exception e) {
                throw new InvalidSystemCallException("HIGH_REG_READ_FROM field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.HIGH_REG_WRITTEN_TO = (boolean) data.get("HIGH_REG_WRITTEN_TO");

            } catch (Exception e) {
                throw new InvalidSystemCallException("HIGH_REG_WRITTEN_TO field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.LOW_REG_READ_FROM = (boolean) data.get("LOW_REG_READ_FROM");

            } catch (Exception e) {
                throw new InvalidSystemCallException("LOW_REG_READ_FROM field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.LOW_REG_WRITTEN_TO = (boolean) data.get("LOW_REG_WRITTEN_TO");

            } catch (Exception e) {
                throw new InvalidSystemCallException("LOW_REG_WRITTEN_TO field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.MEMORY_READ_FROM = (boolean) data.get("MEMORY_READ_FROM");

            } catch (Exception e) {
                throw new InvalidSystemCallException("MEMORY_READ_FROM field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
            try {
                this.MEMORY_WRITTEN_TO = (boolean) data.get("MEMORY_WRITTEN_TO");
            } catch (Exception e) {
                throw new InvalidSystemCallException("MEMORY_WRITTEN_TO field in " + this.SYSTEM_CALL_NAME + " is missing from the yml");
            }
        }

    }
}
