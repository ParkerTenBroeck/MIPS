/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.syscall;

import org.parker.mips.ResourceHandler;
import org.parker.mips.gui.MainGUI;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.parker.mips.processor.Processor.*;

/**
 *
 * @author parke
 */
public class SystemCallPluginHandler {

    private static final ArrayList<SystemCallPlugin> registeredSystemCallPlugins = new ArrayList();
    private static final ArrayList<SystemCall> registeredSystemCalls = new ArrayList();

    private static SystemCall[] idrkWhat = new SystemCall[500];

    private final static Logger LOGGER = Logger.getLogger(SystemCallPluginHandler.class.getName());

    public static void registerSystemCallPlugin(SystemCallPlugin scp) {

        if (scp == null) {
            return;
        }
        if (scp.getSystemCalls() == null) {
            LOGGER.log(Level.SEVERE, "System Calls is null in" + scp.DESCRIPTION.NAME + " skipping for now");
            return;
        }

        SystemCall[] scl = scp.getSystemCalls();

        int totalConflicts = 0;

        for (SystemCall sc : scl) {
            if (sc == null) {
                totalConflicts++;
                LOGGER.log(Level.SEVERE,"System Call in " + scp.DESCRIPTION.NAME + " is null skipping for now");
                continue;
            }
            boolean conflict = false;
            for (SystemCall rsc : registeredSystemCalls) {
                if ((sc.DATA.SYSTEM_CALL_NAME.equals(rsc.DATA.SYSTEM_CALL_NAME)) || (sc.DATA.SYSTEM_CALL_NUMBER == rsc.DATA.SYSTEM_CALL_NUMBER && sc.DATA.SYSTEM_CALL_NUMBER >= 0)) {
                    conflict = true;
                    totalConflicts++;
                    LOGGER.log(Level.SEVERE,"System Call Conflict " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER
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
                    LOGGER.log(Level.INFO,"SystemCall: " + sc.DATA.SYSTEM_CALL_NAME + " From Plugin: " + scp.DESCRIPTION.NAME + " was registered with the System_Call_Number: " + i);
                    idrkWhat[i] = sc;
                }
                registeredSystemCalls.add(sc);
                LOGGER.log(Level.CONFIG,"System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + " was successfully registered");
            } else {
                LOGGER.log(Level.WARNING,"System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + "was not registered because of a conflict");
                //warning system call (name and number) was not registered
            }
        }
        registeredSystemCallPlugins.add(scp);
        if (totalConflicts > 0) {
            LOGGER.log(Level.WARNING, "SystemCall plugin: " + scp.DESCRIPTION.NAME + " was reigstered with " + totalConflicts + " conflicts");
        } else {
            LOGGER.log(Level.CONFIG, "SystemCall plugin: " + scp.DESCRIPTION.NAME + " was reigstered with " + totalConflicts + " conflicts" + "\n");
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
            throw new InvalidSystemCallException("Invalid System Call: " + id);
        } catch (Exception e) {

        }
        if (sc != null) {
            try {
                sc.handleSystemCall();
            } catch (Exception e) {
                throw new SystemCallRunTimeExcpetion("System Call: " + sc.DATA.SYSTEM_CALL_NAME
                        + " from plugin: " + sc.HOST_PLUGIN.DESCRIPTION.NAME, e);
                //logRunTimeSystemCallError("System Call: " + sc.DATA.SYSTEM_CALL_NAME
                //        + " from plugin: " + sc.HOST_PLUGIN.DESCRIPTION.NAME + "\n" + LogFrame.getFullExceptionMessage(e));
            }
        } else {
            throw new InvalidSystemCallException("Invalid System Call: " + id);
        }
    }


    public static void unRegisterSystemCallPlugin(SystemCallPlugin plugin) {
        boolean wasError = false;

        if (plugin != null) {
            try {
                plugin.onUnload();

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
                        LOGGER.log(Level.SEVERE, "Could not unregister SystemCall" + sc.DATA.SYSTEM_CALL_NAME, e);
                        wasError = true;
                    }
                }
                if (!wasError) {
                    LOGGER.log(Level.INFO, "Succcessfully unloaded and unregistered: " + plugin.DESCRIPTION.NAME);
                } else {
                    LOGGER.log(Level.WARNING, "Unloaded and unregistered: " + plugin.DESCRIPTION.NAME + " With some Errors");
                }
                MainGUI.reloadSystemCallPluginLists();
                regenerateStandardSysCallHeaderFile();
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "There was an error unloading the plugin", e);
            }
        }
    }

    /*
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
        LogFrame.logError("[System Call] " + message);
    }

    public static void logSystemCallWarning(String message) {
        LogFrame.logWarning("[System Call] " + message);
    }

    public static void logSystemCallSystemMessage(String message) {
        LogFrame.logSystemMessage("[System Call] " + message);
    }

    //handler
    public static void logSystemCallPluginHandlerError(String message) {
        LogFrame.logError("[Plugin Handler] " + message);
    }

    public static void logSystemCallPluginHandlerWarning(String message) {
        LogFrame.logWarning("[Plugin Handler] " + message);
    }

    public static void logSystemCallPluginHandlerSystemMessage(String message) {
        LogFrame.logSystemMessage("[Plugin Handler] " + message);
    }
*/

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
            LOGGER.log(Level.INFO, "Successfully wrote Standard System Call Header file to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error While writting the Standard System Call Header File to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n\n", e);
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
