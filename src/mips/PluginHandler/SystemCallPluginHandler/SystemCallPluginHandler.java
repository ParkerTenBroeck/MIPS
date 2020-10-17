/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.PluginHandler.SystemCallPluginHandler;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import mips.Log;
import mips.ResourceHandler;
import Processor.SystemCallHandler;

/**
 *
 * @author parke
 */
public class SystemCallPluginHandler {
    
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
        } else {
            logPluginHandlerWarning("Plugin at: " + path + " could not be loaded");
        }
        return plugin;
    }
    
    private static SystemCallPlugin loadExternalPluginFromJarPath(String path) {
        File jarFile = new File(path);
        
        String className = "";
        
        try (JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile))) {
            Manifest manifest = jarStream.getManifest();

            // Get Main-Class attribute from MANIFEST.MF
            className = manifest.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
        } catch (FileNotFoundException ex) {
            logPluginHandlerError("File: " + path + " does not exist");
        } catch (IOException ex) {
            logPluginHandlerError("IO exeption whill reading File: " + path);
        }
        
        URLClassLoader classLoader = null;
        
        if (false) {
            try {
                classLoader = new URLClassLoader(
                        new URL[]{new URL("jar:" + jarFile.toURI().toURL() + "!/")}
                );
            } catch (MalformedURLException ex) {
                logPluginHandlerError("Malformed URL whill reading File: " + path);
            }
        }
        
        Class<?> pluginClass = null;
        try {
            pluginClass = classLoader.loadClass(className); // Load the class
        } catch (ClassNotFoundException ex) {
            logPluginHandlerError("Class not found exeption whill reading File: " + path + "\n"
                    + "Not a plugin? plugin not build correctly?");
        }
        
        SystemCallPlugin plugin = null;
        try {
            plugin = (SystemCallPlugin) pluginClass.newInstance();
        } catch (InstantiationException ex) {
            logPluginHandlerError("Instantiation Exeption when loading File: " + path);
        } catch (IllegalAccessException ex) {
            logPluginHandlerError("Illegal Access Exeption when loading File: " + path);
        }
        
        logPluginHandlerSystemMessage(plugin.PLUGIN_NAME + " was loaded");
        
        return plugin;
    }
    
    public static void loadDefaultPlugins() {
        
        SystemCallPlugin spc = loadInternalPluginFromClassPath("Processor.InternalSystemCalls.DefaultSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);
        
        spc = loadInternalPluginFromClassPath("Processor.InternalSystemCalls.UserIOSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);
        
        spc = loadInternalPluginFromClassPath("Processor.InternalSystemCalls.ScreenSystemCalls");
        SystemCallHandler.registerSystemCallPlugin(spc);
        
        File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
        
        File files[] = file.listFiles();
        
        for (File f : files) {
            if (f.exists()) {
                SystemCallPlugin scp = loadExternalPluginFromJarPath(f.getAbsolutePath());
                scp.init();
            }
        }
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
