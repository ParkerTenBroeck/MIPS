/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.PluginHandler.SystemCallPluginHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.parker.mips.Log;
import org.parker.mips.Processor.SystemCallHandler;
import org.parker.mips.ResourceHandler;

/**
 *
 * @author parke
 */
public class SystemCallPluginLoader {

    static ArrayList<SystemCallPlugin> loadedPlugins;

    private static SystemCallPlugin loadInternalPluginFromClassPath(String path) {

        Class<?> pluginClass = null;
        try {
            pluginClass = Class.forName(path); // Load the class
        } catch (ClassNotFoundException ex) {
            logPluginHandlerError("internal Class not found for: " + path);
        }

        SystemCallPlugin plugin = null;
        try {
            plugin = (SystemCallPlugin) pluginClass.newInstance();
        } catch (InstantiationException ex) {
            logPluginHandlerError("Cannot create instance for: " + path);
        } catch (IllegalAccessException ex) {
            logPluginHandlerError("Illegal Access Exeption when instantiating new instance of internal plugin: " + path);
        } catch (Exception e) {
            logPluginHandlerError("The plugin at: " + path + " could not be loaded: " + e);
        }
        if (plugin != null) {
            logPluginHandlerSystemMessage(plugin.PLUGIN_NAME + " was loaded");
            plugin.init();
        } else {
            logPluginHandlerWarning("Plugin at: " + path + " could not be loaded");
        }
        return plugin;
    }

    private static SystemCallPlugin loadExternalPluginFromJarPath(String path) {
        File jarFile = new File(path);

        String className = "";

        try {
            JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile));
            Manifest manifest = jarStream.getManifest();

            // Get Main-Class attribute from MANIFEST.MF
            className = manifest.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
        } catch (FileNotFoundException ex) {
            logPluginHandlerError("File: " + path + " does not exist");
        } catch (IOException ex) {
            logPluginHandlerError("IO exeption whill reading Plugin: " + path);
        } catch (Exception e) {
            logPluginHandlerError("General Exeption while reading manifest" + path + " " + e.getMessage());
        } catch (Error e) {
            logPluginHandlerError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        URLClassLoader classLoader = null;

        try {
            classLoader = new URLClassLoader(
                    new URL[]{new URL("jar:" + jarFile.toURI().toURL() + "!/")}
            );
        } catch (MalformedURLException ex) {
            logPluginHandlerError("Malformed URL whill reading Plugin: " + path);
        } catch (Exception e) {
            logPluginHandlerError("There was an Exception while loading Plugin: " + path + "\n"
                    + "Path does not exist? " + e.getMessage());
        } catch (Error e) {
            logPluginHandlerError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        Class<?> pluginClass = null;
        try {
            pluginClass = classLoader.loadClass(className); // Load the class
        } catch (ClassNotFoundException ex) {
            logPluginHandlerError("Class not found exeption whill reading Plugin: " + path + "\n"
                    + "Not a plugin? plugin not build correctly?");
        } catch (Exception e) {
            logPluginHandlerError("There was an Exeption while loading Plugin: " + path + "\n"
                    + "Outdated Plugin? plugin not build correctly? " + e.getMessage());
        } catch (Error e) {
            logPluginHandlerError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        SystemCallPlugin plugin = null;
        try {
            plugin = (SystemCallPlugin) pluginClass.newInstance();
        } catch (InstantiationException ex) {
            logPluginHandlerError("Instantiation Exeption when loading Plugin: " + path);
        } catch (IllegalAccessException ex) {
            logPluginHandlerError("Illegal Access Exeption when loading Plugin: " + path);
        } catch (Exception e) {
            logPluginHandlerError("There was some Exeption while loading Plugin: " + path + " " + e.getMessage());
        } catch (Error e) {
            logPluginHandlerError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        if (plugin != null) {
            logPluginHandlerSystemMessage(plugin.PLUGIN_NAME + " was loaded");
            plugin.init();
        } else {
            logPluginHandlerWarning("Plugin at: " + path + " could not be loaded");
        }
        return plugin;
    }

    public static void loadDefaultPlugins() {

        SystemCallPlugin spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);

        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.UserIOSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);

        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.ScreenSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);

        File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
        File files[] = file.listFiles();
        for (File f : files) {
            if (f.exists()) {
                SystemCallPlugin scp = loadExternalPluginFromJarPath(f.getAbsolutePath());
                SystemCallHandler.registerSystemCallPlugin(scp);
            }
        }

        SystemCallHandler.regenerateStandardSysCallHeaderFile();
    }

    public static void logPluginHandlerError(String message) {
        Log.logError("[Plugin Handler] " + message);
    }

    public static void logPluginHandlerWarning(String message) {
        Log.logWarning("[Plugin Handler] " + message);
    }

    public static void logPluginHandlerSystemMessage(String message) {
        Log.logSystemMessage("[Plugin Handler] " + message);
    }
}
