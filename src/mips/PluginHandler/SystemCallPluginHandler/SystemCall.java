/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.PluginHandler.SystemCallPluginHandler;

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

    public final int SYSTEM_CALL_NUMBER;
    public final String SYSTEM_CALL_NAME;
    public final String SYSTEM_CALL_DISCRIPTION;

    public SystemCall(int systemCallNum, String systemCallName, String systemCallDis) {
        this.SYSTEM_CALL_NUMBER = systemCallNum;
        this.SYSTEM_CALL_NAME = systemCallName;
        this.SYSTEM_CALL_DISCRIPTION = systemCallDis;
        if (systemCallName.contains(" ")) {
            throw new Error("System Call Name Cannot contain any space characters");
        }
    }

    public abstract void handleSystemCall();

}
