/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.architectures.mips.syscall;

import org.parker.mips.util.ResourceHandler;
import org.parker.mips.gui.MainGUI;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parke
 */
public class SystemCallPluginHandler {

    private static final ArrayList<SystemCallPlugin> registeredSystemCallPlugins = new ArrayList<>();
    private static final ArrayList<SystemCall> registeredSystemCalls = new ArrayList<>();

    private static final SystemCall[] integerToSystemCall = new SystemCall[500];

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
                            + " was not registered because " + rsc.DATA.SYSTEM_CALL_NAME + ":" + rsc.DATA.SYSTEM_CALL_NUMBER + " was already registered");
                    break;
                }
            }
            if (!conflict) {
                if (sc.DATA.SYSTEM_CALL_NUMBER >= 0) {
                    integerToSystemCall[sc.DATA.SYSTEM_CALL_NUMBER] = sc; //number of system call is known
                    LOGGER.log(Level.CONFIG,"SystemCall: " + sc.DATA.SYSTEM_CALL_NAME + " From Plugin: " + scp.DESCRIPTION.NAME + " was registered with the System_Call_Number: " + sc.DATA.SYSTEM_CALL_NUMBER);
                } else {
                    int i = 0; //number of system call is generated on load
                    while (integerToSystemCall[i] != null) {
                        i++;
                    }
                    LOGGER.log(Level.INFO,"SystemCall: " + sc.DATA.SYSTEM_CALL_NAME + " From Plugin: " + scp.DESCRIPTION.NAME + " was registered with the artificial System_Call_Number: " + i);
                    integerToSystemCall[i] = sc;
                }
                scp.onLoad();
                registeredSystemCalls.add(sc);
                LOGGER.log(Level.CONFIG,"System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + " was successfully registered");
            } else {
                LOGGER.log(Level.WARNING,"System Call " + sc.DATA.SYSTEM_CALL_NAME + ":" + sc.DATA.SYSTEM_CALL_NUMBER + "was not registered because of a conflict");
                //warning system call (name and number) was not registered
            }
        }
        registeredSystemCallPlugins.add(scp);
        if (totalConflicts > 0) {
            LOGGER.log(Level.WARNING, "SystemCall plugin: " + scp.DESCRIPTION.NAME + " was registered with " + totalConflicts + " conflicts");
        } else {
            LOGGER.log(Level.CONFIG, "SystemCall plugin: " + scp.DESCRIPTION.NAME + " was registered with " + totalConflicts + " conflicts" + "\n");
        }

        regenerateStandardSysCallHeaderFile();
        MainGUI.reloadSystemCallPluginLists();

    }

    public static int getSystemCallNumberFromGeneratedNumber(SystemCall sc) {
        for (int i = 0; i < integerToSystemCall.length; i++) {
            if (integerToSystemCall[i] != null) {
                if (integerToSystemCall[i].equals(sc)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void SystemCall(int id) {
        SystemCall sc;

        try {
            sc = integerToSystemCall[id];
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidSystemCallException("Invalid System Call: " + id + " System Call not registered or is null");
        }
        
        if (sc != null) {
            try {
                sc.handleSystemCall();
            } catch (Exception e) {
                throw new SystemCallRunTimeException("System Call (" + sc.DATA.SYSTEM_CALL_NAME
                        + ") from plugin (" + sc.HOST_PLUGIN.DESCRIPTION.NAME + ")", e);
            }
        } else {
            throw new InvalidSystemCallException("Invalid System Call: " + id + " System Call not registered or is null");
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
                                integerToSystemCall[sc.DATA.SYSTEM_CALL_NUMBER] = null;
                            } else {
                                integerToSystemCall[getSystemCallNumberFromGeneratedNumber(sc)] = null;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Could not unregister SystemCall" + sc.DATA.SYSTEM_CALL_NAME, e);
                        wasError = true;
                    }
                }
                if (!wasError) {
                    LOGGER.log(Level.INFO, "Successfully unloaded and unregistered: " + plugin.DESCRIPTION.NAME);
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

    public static void regenerateStandardSysCallHeaderFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(ResourceHandler.SYS_CALL_DEF_HEADER_FILE, "UTF-8");

            for (SystemCallPlugin plugin : registeredSystemCallPlugins) {

                if (plugin != null) {

                    writer.println(";SystemCalls defined withing Plugin: " + plugin.DESCRIPTION.NAME);
                    writer.println("");
                    writer.println(".define " + plugin.DESCRIPTION.NAME);
                    writer.println("");

                    for (SystemCall call : plugin.getSystemCalls()) {

                        int index;
                        String comment;

                        if (call.DATA.SYSTEM_CALL_NUMBER >= 0) {
                            index = call.DATA.SYSTEM_CALL_NUMBER;
                            comment = "";
                        } else {
                            index = getSystemCallNumberFromGeneratedNumber(call);
                            comment = "Real number is: " + call.DATA.SYSTEM_CALL_NUMBER + " Generated: true" + " Index: " + index + " on load";
                        }

                        try {
                            if (integerToSystemCall[index] != null) {
                                writer.println(".define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                                writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                                //writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                            } else {
                                writer.println(";.define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                                writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                                writer.println(";SystemCall was not registered correctly");
                                //writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                            }
                        } catch (Exception e) {
                            writer.println(";.define " + call.DATA.SYSTEM_CALL_NAME + " " + index + " ;" + comment);
                            writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                            writer.println(";SystemCall was not registered correctly");
                            //writer.println(";" + call.DATA.SYSTEM_CALL_DESCRIPTION);
                        }
                        writer.println("");
                    }
                } else {
                    writer.println(";Plugin NULL");
                }
                writer.println();
                writer.println();
            }
            LOGGER.log(Level.CONFIG, "Successfully wrote Standard System Call Header file to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error While writing the Standard System Call Header File to: " + ResourceHandler.SYS_CALL_DEF_HEADER_FILE + "\n", e);
        }
        if(writer != null) {
            writer.close();
        }

    }

    @SuppressWarnings("unchecked")
    public static ArrayList<SystemCallPlugin> getRegisteredSystemCalls() {
        return (ArrayList<SystemCallPlugin>) registeredSystemCallPlugins.clone();
    }

    public static boolean isSystemCallRegistered(SystemCall sc) {
        return registeredSystemCalls.contains(sc);
    }

}
