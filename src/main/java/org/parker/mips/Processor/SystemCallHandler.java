/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.Processor;

import java.util.ArrayList;
import org.parker.mips.GUI.Main_GUI;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCall;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPlugin;
import org.parker.mips.PluginHandler.SystemCallPluginHandler.SystemCallPluginHandler;
import static org.parker.mips.Processor.Processor.logRunTimeError;
import static org.parker.mips.Processor.Processor.logRunTimeMessage;
import static org.parker.mips.Processor.Processor.logRunTimeWarning;

/**
 *
 * @author parke
 */
public class SystemCallHandler {

    private static final ArrayList<SystemCallPlugin> registeredSystemCallPlugins = new ArrayList();
    private static final ArrayList<SystemCall> registeredSystemCalls = new ArrayList();

    private static SystemCall[] idrkWhat = new SystemCall[500];

    public static void registerSystemCallPlugin(SystemCallPlugin scp) {

        if (scp == null) {
            return;
        }
        if (scp.getSystemCalls() == null) {
            SystemCallPluginHandler.logPluginHandlerError("System Calls is null in" + scp.PLUGIN_NAME + " skipping for now");
            return;
        }

        SystemCall[] scl = scp.getSystemCalls();

        int totalConflicts = 0;

        for (SystemCall sc : scl) {
            if (sc == null) {
                totalConflicts++;
                SystemCallPluginHandler.logPluginHandlerError("System Call in " + scp.PLUGIN_NAME + " is null skipping for now");
                continue;
            }
            boolean conflict = false;
            for (SystemCall rsc : registeredSystemCalls) {
                if ((sc.DATA.SYSTEM_CALL_NAME.equals(rsc.DATA.SYSTEM_CALL_NAME)) || sc.DATA.SYSTEM_CALL_NUMBER == rsc.DATA.SYSTEM_CALL_NUMBER) {
                    conflict = true;
                    totalConflicts++;
                    SystemCallPluginHandler.logPluginHandlerError("System Call Conflict " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER
                            + " was not registered because " + rsc.DATA.SYSTEM_CALL_NAME + ":" + rsc.DATA.SYSTEM_CALL_NUMBER + " was already reigstered");
                    break;
                }
            }
            if (!conflict) {
                registeredSystemCalls.add(sc);
                idrkWhat[sc.DATA.SYSTEM_CALL_NUMBER] = sc;
                SystemCallPluginHandler.logPluginHandlerSystemMessage("System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + " was successfully registered");
            } else {
                SystemCallPluginHandler.logPluginHandlerWarning("System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + "was not registered because of a conflict");
                //warning system call (name and number) was not registered
            }
        }
        registeredSystemCallPlugins.add(scp);
        if (totalConflicts > 0) {
            SystemCallPluginHandler.logPluginHandlerWarning("SystemCall plugin: " + scp.PLUGIN_NAME + " was reigstered with " + totalConflicts + " conflicts");
        } else {
            SystemCallPluginHandler.logPluginHandlerSystemMessage("SystemCall plugin: " + scp.PLUGIN_NAME + " was reigstered with " + totalConflicts + " conflicts" + "\n");
        }

    }

    public static void SystemCall(int id) {

        SystemCall sc = null;

        try {
            sc = idrkWhat[id];
        } catch (IndexOutOfBoundsException e) {
            logRunTimeSystemCallError("Invalid System Call either does not exist or was not registered ID: " + id);
        } catch (Exception e) {

        }
        if (sc != null) {
            try {
                sc.handleSystemCall();
            } catch (Exception e) {
                logRunTimeSystemCallError("System Call: " + sc.DATA.SYSTEM_CALL_NAME
                        + " from plugin: " + sc.HOST_PLUGIN.PLUGIN_NAME + " generated exeption: " + e.getMessage());
            }
        } else {
            logRunTimeSystemCallError("Invalid System Call either does not exist or was not registered ID: " + id);
        }
    }

    public static void logRunTimeSystemCallError(String message) {
        logRunTimeError("[System Call] " + message);
    }

    public static void logRunTimeSystemCallWarning(String message) {
        logRunTimeWarning("[System Call] " + message);
    }

    public static void logRunTimeSystemCallMessage(String message) {
        logRunTimeMessage("[System Call] " + message);
    }

}
