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
import org.parker.mips.Processor.SystemCallPluginHandler;
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
            logPluginLoaderError("internal Class not found for: " + path);
        }

        SystemCallPlugin plugin = null;
        try {
            plugin = (SystemCallPlugin) pluginClass.newInstance();
        } catch (InstantiationException ex) {
            logPluginLoaderError("Cannot create instance for: " + path);
        } catch (IllegalAccessException ex) {
            logPluginLoaderError("Illegal Access Exeption when instantiating new instance of internal plugin: " + path);
        } catch (Exception e) {
            logPluginLoaderError("The plugin at: " + path + " could not be loaded: " + e);
        }
        if (plugin != null) {
            logPluginLoaderSystemMessage(plugin.PLUGIN_NAME + " was loaded");
            plugin.init();
        } else {
            logPluginLoaderWarning("Plugin at: " + path + " could not be loaded");
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
            logPluginLoaderError("File: " + path + " does not exist");
        } catch (IOException ex) {
            logPluginLoaderError("IO exeption whill reading Plugin: " + path);
        } catch (Exception e) {
            logPluginLoaderError("General Exeption while reading manifest" + path + " " + e.getMessage());
        } catch (Error e) {
            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        URLClassLoader classLoader = null;

        try {
            classLoader = new URLClassLoader(
                    new URL[]{new URL("jar:" + jarFile.toURI().toURL() + "!/")}
            );
        } catch (MalformedURLException ex) {
            logPluginLoaderError("Malformed URL whill reading Plugin: " + path);
        } catch (Exception e) {
            logPluginLoaderError("There was an Exception while loading Plugin: " + path + "\n"
                    + "Path does not exist? " + e.getMessage());
        } catch (Error e) {
            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        Class<?> pluginClass = null;
        try {
            pluginClass = classLoader.loadClass(className); // Load the class
        } catch (ClassNotFoundException ex) {
            logPluginLoaderError("Class not found exeption whill reading Plugin: " + path + "\n"
                    + "Not a plugin? plugin not build correctly?");
        } catch (Exception e) {
            logPluginLoaderError("There was an Exeption while loading Plugin: " + path + "\n"
                    + "Outdated Plugin? plugin not build correctly? " + e.getMessage());
        } catch (Error e) {
            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        SystemCallPlugin plugin = null;
        try {
            plugin = (SystemCallPlugin) pluginClass.newInstance();
        } catch (InstantiationException ex) {
            logPluginLoaderError("Instantiation Exeption when loading Plugin: " + path);
        } catch (IllegalAccessException ex) {
            logPluginLoaderError("Illegal Access Exeption when loading Plugin: " + path);
        } catch (Exception e) {
            logPluginLoaderError("There was some Exeption while loading Plugin: " + path + " " + e.getMessage());
        } catch (Error e) {
            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
        }

        if (plugin != null) {
            logPluginLoaderSystemMessage(plugin.PLUGIN_NAME + " was loaded");
            plugin.init();
        } else {
            logPluginLoaderWarning("Plugin at: " + path + " could not be loaded");
        }
        return plugin;
    }

    public static void loadDefaultPlugins() {

        SystemCallPlugin spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls");
        SystemCallPluginHandler.registerSystemCallPlugin(spc);

        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.UserIOSystemCalls");
        SystemCallPluginHandler.registerSystemCallPlugin(spc);

        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.ScreenSystemCalls");
        SystemCallPluginHandler.registerSystemCallPlugin(spc);

        File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
        File files[] = file.listFiles();
        for (File f : files) {
            if (f.exists()) {
                SystemCallPlugin scp = loadExternalPluginFromJarPath(f.getAbsolutePath());
                SystemCallPluginHandler.registerSystemCallPlugin(scp);
            }
        }

        SystemCallPluginHandler.regenerateStandardSysCallHeaderFile();
    }

    public static void logPluginLoaderError(String message) {
        Log.logError("[Plugin Loader] " + message);
    }

    public static void logPluginLoaderWarning(String message) {
        Log.logWarning("[Plugin Loader] " + message);
    }

    public static void logPluginLoaderSystemMessage(String message) {
        Log.logSystemMessage("[Plugin Loader] " + message);
    }

    public static void loadExternalPlugin(File chosenFile) {
        SystemCallPlugin scp = loadExternalPluginFromJarPath(chosenFile.getAbsolutePath());
        SystemCallPluginHandler.registerSystemCallPlugin(scp);
        SystemCallPluginHandler.regenerateStandardSysCallHeaderFile();
    }
}
