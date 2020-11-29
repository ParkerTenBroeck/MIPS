/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;

import java.io.PrintWriter;
import java.util.ArrayList;
import org.parker.mips.gui.MainGUI;
import org.parker.mips.Log;
import org.parker.mips.plugin.syscall.SystemCall;
import org.parker.mips.plugin.syscall.SystemCallPlugin;
import static org.parker.mips.processor.Processor.logRunTimeError;
import static org.parker.mips.processor.Processor.logRunTimeMessage;
import static org.parker.mips.processor.Processor.logRunTimeWarning;
import org.parker.mips.ResourceHandler;

/**
 *
 * @author parke
 */
public class SystemCallPluginHandler {

    private static final ArrayList<SystemCallPlugin> registeredSystemCallPlugins = new ArrayList();
    private static final ArrayList<SystemCall> registeredSystemCalls = new ArrayList();

    private static SystemCall[] idrkWhat = new SystemCall[500];

    public static void registerSystemCallPlugin(SystemCallPlugin scp) {

        if (scp == null) {
            return;
        }
        if (scp.getSystemCalls() == null) {
            logSystemCallPluginHandlerError("System Calls is null in" + scp.DESCRIPTION.NAME + " skipping for now");
            return;
        }

        SystemCall[] scl = scp.getSystemCalls();

        int totalConflicts = 0;

        for (SystemCall sc : scl) {
            if (sc == null) {
                totalConflicts++;
                logSystemCallPluginHandlerError("System Call in " + scp.DESCRIPTION.NAME + " is null skipping for now");
                continue;
            }
            boolean conflict = false;
            for (SystemCall rsc : registeredSystemCalls) {
                if ((sc.DATA.SYSTEM_CALL_NAME.equals(rsc.DATA.SYSTEM_CALL_NAME)) || (sc.DATA.SYSTEM_CALL_NUMBER == rsc.DATA.SYSTEM_CALL_NUMBER && sc.DATA.SYSTEM_CALL_NUMBER >= 0)) {
                    conflict = true;
                    totalConflicts++;
                    logSystemCallPluginHandlerError("System Call Conflict " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER
                            + " was not registered because " + rsc.DATA.SYSTEM_CALL_NAME + ":" + rsc.DATA.SYSTEM_CALL_NUMBER + " was already reigstered");
                    break;
                }
            }
            if (!conflict) {
                if (sc.DATA.SYSTEM_CALL_NUMBER >= 0) {
                    idrkWhat[sc.DATA.SYSTEM_CALL_NUMBER] = sc; //number of system call is known
                } else {
                    int i = 0; //number of system call is generated on load
                    while (idrkWhat[i] != null) {
                        i++;
                    }
                    logSystemCallPluginHandlerSystemMessage("SystemCall: " + sc.DATA.SYSTEM_CALL_NAME + " From Plugin: " + scp.DESCRIPTION.NAME + " was registered with the System_Call_Number: " + i);
                    idrkWhat[i] = sc;
                }
                registeredSystemCalls.add(sc);
                logSystemCallPluginHandlerSystemMessage("System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + " was successfully registered");
            } else {
                logSystemCallPluginHandlerWarning("System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + "was not registered because of a conflict");
                //warning system call (name and number) was not registered
            }
        }
        registeredSystemCallPlugins.add(scp);
        if (totalConflicts > 0) {
            logSystemCallPluginHandlerWarning("SystemCall plugin: " + scp.DESCRIPTION.NAME + " was reigstered with " + totalConflicts + " conflicts");
        } else {
            logSystemCallPluginHandlerSystemMessage("SystemCall plugin: " + scp.DESCRIPTION.NAME + " was reigstered with " + totalConflicts + " conflicts" + "\n");
        }
        
        MainGUI.reloadSystemCallPluginLists();

    }

    public static int getSystemCallNumberFromGeneratedNumber(SystemCall sc) {
        for (int i = 0; i < idrkWhat.length; i++) {
            if (idrkWhat[i] != null) {
                if (idrkWhat[i].equals(sc)) {
                    return i;
                }
            }
        }
        return -1;
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
                        + " from plugin: " + sc.HOST_PLUGIN.DESCRIPTION.NAME + " generated exeption: " + e.getMessage());
            }
        } else {
            logRunTimeSystemCallError("Invalid System Call either does not exist or was not registered ID: " + id);
        }
    }


    public static void unRegisterSystemCallPlugin(SystemCallPlugin plugin) {
        boolean wasError = false;

        if (plugin != null) {
            if (plugin.onUnload()) {
                registeredSystemCallPlugins.remove(plugin);
                for (SystemCall sc : plugin.getSystemCalls()) {
                    try {
                        if (isSystemCallRegistered(sc)) {
                            registeredSystemCalls.remove(sc);
                            if (sc.DATA.SYSTEM_CALL_NUMBER >= 0) {
                                idrkWhat[sc.DATA.SYSTEM_CALL_NUMBER] = null;
                            } else {
                                idrkWhat[getSystemCallNumberFromGeneratedNumber(sc)] = null;
                            }
                        }
                    } catch (Exception e) {
                        logSystemCallPluginHandlerError("Could not unregister SystemCall" + sc.DATA.SYSTEM_CALL_NAME + " Error: " + e.toString());
                        wasError = true;
                    }
                }
                if (!wasError) {
                    logSystemCallPluginHandlerSystemMessage("Succcessfully unloaded and unregistered: " + plugin.DESCRIPTION.NAME);
                } else {
                    logSystemCallPluginHandlerWarning("Succcessfully unloaded and unregistered: " + plugin.DESCRIPTION.NAME + " With some Errors");
                }
                MainGUI.reloadSystemCallPluginLists();
                regenerateStandardSysCallHeaderFile();
            } else {
                logSystemCallPluginHandlerError("There was an error unloading the plugin");
            }
        }
    }

    //runtime systemcall
    public static void logRunTimeSystemCallError(String message) {
        logRunTimeError("[System Call] " + message);
    }

    public static void logRunTimeSystemCallWarning(String message) {
        logRunTimeWarning("[System Call] " + message);
    }

    public static void logRunTimeSystemCallMessage(String message) {
        logRunTimeMessage("[System Call] " + message);
    }

    //system call
    public static void logSystemCallError(String message) {
        Log.logError("[System Call] " + message);
    }

    public static void logSystemCallWarning(String message) {
        Log.logWarning("[System Call] " + message);
    }

    public static void logSystemCallSystemMessage(String message) {
        Log.logSystemMessage("[System Call] " + message);
    }

    //handler
    public static void logSystemCallPluginHandlerError(String message) {
        Log.logError("[Plugin Handler] " + message);
    }

    public static void logSystemCallPluginHandlerWarning(String message) {
        Log.logWarning("[Plugin Handler] " + message);
    }

    public static void logSystemCallPluginHandlerSystemMessage(String message) {
        Log.logSystemMessage("[Plugin Handler] " + message);
    }

    public static void regenerateStandardSysCallHeaderFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(ResourceHandler.SYS_CALL_DEF_HEADER_FILE, "UTF-8");

            for (SystemCallPlugin plugin : registeredSystemCallPlugins) {

                if (plugin != null) {

                    writer.println(";SystemCalls defined withing Plugin: " + plugin.DESCRIPTION.NAME);
                    writer.println("");

                    for (SystemCall call : plugin.getSystemCalls()) {

                        int index;
                        boolean generated;
                        String comment;

                        if (call.DATA.SYSTEM_CALL_NUMBER >= 0) {
                            index = call.DATA.SYSTEM_CALL_NUMBER;
                            generated = false;
                            comment = "";
                        } else {
                            index = getSystemCallNumberFromGeneratedNumber(call);
                            generated = true;
                            comment = "Real number is: " + call.DATA.SYSTEM_CALL_NUMBER + " Generated: " + index + " on load";
                        }

                        try {
                            if (idrkWhat[index] != null) {
                                writer.println("#define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                                writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                                //writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                            } else {
                                writer.println(";#define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                                writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                                writer.println(";SystemCall was not registered correctly");
                                //writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                            }
                        } catch (Exception e) {
                            writer.println(";#define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                            writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                            writer.println(";SystemCall was not registered correctly");
                            //writer.println(";" + call.DATA.SYSTEM_CALL_DISCRIPTION);
                        }
                        writer.println("");
                    }
                } else {
                    writer.println(";Plugin NULL");
                }
                writer.println();
                writer.println();
            }
            logSystemCallSystemMessage("Successfully wrote Standard System Call Header file to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n\n");
        } catch (Exception e) {
            logSystemCallError("There was some Exception e:" + e + " While writting the Standard System Call Header File to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n\n");
        }
        try {
            writer.close();
        } catch (Exception e) {

        }

    }

    public static ArrayList<SystemCallPlugin> getRegisteredSystemCalls() {
        return (ArrayList<SystemCallPlugin>) registeredSystemCallPlugins.clone();
    }

    public static boolean isSystemCallRegistered(SystemCall sc) {
        return registeredSystemCalls.contains(sc);
    }

}
