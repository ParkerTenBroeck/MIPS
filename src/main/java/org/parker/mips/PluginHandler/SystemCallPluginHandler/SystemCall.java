/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.PluginHandler.SystemCallPluginHandler;

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
     * @param hostPlugin this is used to keep track of what system call belongs to what plugin
     */
    public SystemCall(SystemCallData data, String systemCallName, SystemCallPlugin hostPlugin) {
        this.DATA = data;
        this.HOST_PLUGIN = hostPlugin;

        if (DATA.SYSTEM_CALL_NAME.contains(" ")) {
            SystemCallPluginHandler.logPluginHandlerError("System Call Name: "
                    + DATA.SYSTEM_CALL_DISCRIPTION + " Cannot contain any space characters");
            throw new IllegalArgumentException("System Call Name: "
                    + DATA.SYSTEM_CALL_DISCRIPTION + " Cannot contain any space characters");
        }
        if (!this.DATA.SYSTEM_CALL_NAME.equals(systemCallName)) {
            SystemCallPluginHandler.logPluginHandlerError("Loaded System Call Data Name: "
                    + this.DATA.SYSTEM_CALL_NAME + " DOES NOT match Entered System Call Name: " + systemCallName);
            throw new IllegalArgumentException("Loaded System Call Data Name: "
                    + this.DATA.SYSTEM_CALL_NAME + " DOES NOT match Entered System Call Name: " + systemCallName);
        }
    }

    public abstract void handleSystemCall();

}
