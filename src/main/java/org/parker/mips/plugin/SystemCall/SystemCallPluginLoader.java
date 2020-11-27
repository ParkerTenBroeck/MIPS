/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin.SystemCall;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.parker.mips.Log;
import org.parker.mips.MIPS;
import org.parker.mips.plugin.InvalidPluginException;
import org.parker.mips.plugin.PluginDescription;
import org.yaml.snakeyaml.error.YAMLException;

/**
 *
 * @author parke
 */
public class SystemCallPluginLoader {

    public static final String DEFAULT_YAML_PATH = "plugin.yml";

    public static SystemCallPlugin loadInternalPlugin(String yamlPath) {
        return loadPlugin(new File(MIPS.JAR_PATH), yamlPath);
    }

    public static SystemCallPlugin loadExternalPlugin(File file) {
        return loadPlugin(file, DEFAULT_YAML_PATH);
    }

    private static SystemCallPlugin loadPlugin(File file, String yamlPath) {

        new SystemCallPluginLoader(file, yamlPath);

        if (true == true){
        return null;
        }

        PluginDescription description = null;

        JarFile jar = null;
        InputStream stream = null;

        try {
            if (file.getAbsolutePath() == new File(MIPS.JAR_PATH).getAbsolutePath()) {
                stream = SystemCallPluginLoader.class.getResourceAsStream(yamlPath);
            } else {
                jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry("yamlPath");

                if (entry == null) {
                    //throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
                }

                stream = jar.getInputStream(entry);
            }

            if (stream == null) {
                //throw new InvalidPluginException(yamlPath);
            }

            description = new PluginDescription(stream);

        } catch (IOException ex) {
            //throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            //throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        SystemCallPluginClassLoader current = null;

//        try {
//            //current = new SystemCallPluginClassLoader(file, new SystemCallPluginLoader().getClass().getClassLoader(), description);
//        } catch (InvalidPluginException ex) {
//            Logger.getLogger(SystemCallPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(SystemCallPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
//        }

        return current.plugin;
    }

    public static void main(String[] args) {

    }

    public SystemCallPluginLoader(File file, String yamlPath) {

        PluginDescription description = null;

        JarFile jar = null;
        InputStream stream = null;

        try {
            if (file.getAbsolutePath() == new File(MIPS.JAR_PATH).getAbsolutePath()) {
                stream = SystemCallPluginLoader.class.getResourceAsStream(yamlPath);
            } else {
                jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry("yamlPath");

                if (entry == null) {
                    //throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
                }

                stream = jar.getInputStream(entry);
            }

            if (stream == null) {
                //throw new InvalidPluginException(yamlPath);
            }

            description = new PluginDescription(stream);

        } catch (IOException ex) {
            //throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            //throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        SystemCallPluginClassLoader current = null;

        try {
            current = new SystemCallPluginClassLoader(getClass().getClassLoader(), description, file);
        } catch (InvalidPluginException ex) {
            Logger.getLogger(SystemCallPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SystemCallPluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //return current.plugin;
    }

//    private static SystemCallPlugin loadInternalPluginFromClassPath(String path) {
//
//        Class<?> pluginClass = null;
//        try {
//            pluginClass = Class.forName(path); // Load the class
//        } catch (ClassNotFoundException ex) {
//            logPluginLoaderError("internal Class not found for: " + path);
//        }
//
//        SystemCallPlugin plugin = null;
//        try {
//            plugin = (SystemCallPlugin) pluginClass.newInstance();
//        } catch (InstantiationException ex) {
//            logPluginLoaderError("Cannot create instance for: " + path);
//        } catch (IllegalAccessException ex) {
//            logPluginLoaderError("Illegal Access Exeption when instantiating new instance of internal plugin: " + path);
//        } catch (Exception e) {
//            logPluginLoaderError("The plugin at: " + path + " could not be loaded: " + e);
//        }
//        if (plugin != null) {
//            logPluginLoaderSystemMessage(plugin.PLUGIN_NAME + " was loaded");
//            plugin.onLoad();
//        } else {
//            logPluginLoaderWarning("Plugin at: " + path + " could not be loaded");
//        }
//        return plugin;
//    }
//
//    private static SystemCallPlugin loadExternalPluginFromJarPath(String path) {
//        File jarFile = new File(path);
//
//        String className = "";
//
//        try {
//            JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile));
//            Manifest manifest = jarStream.getManifest();
//
//            // Get Main-Class attribute from MANIFEST.MF
//            className = manifest.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
//        } catch (FileNotFoundException ex) {
//            logPluginLoaderError("File: " + path + " does not exist");
//        } catch (IOException ex) {
//            logPluginLoaderError("IO exeption whill reading Plugin: " + path);
//        } catch (Exception e) {
//            logPluginLoaderError("General Exeption while reading manifest" + path + " " + e.getMessage());
//        } catch (Error e) {
//            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
//        }
//
//        URLClassLoader classLoader = null;
//
//        try {
//            classLoader = new URLClassLoader(
//                    new URL[]{new URL("jar:" + jarFile.toURI().toURL() + "!/")}
//            );
//        } catch (MalformedURLException ex) {
//            logPluginLoaderError("Malformed URL whill reading Plugin: " + path);
//        } catch (Exception e) {
//            logPluginLoaderError("There was an Exception while loading Plugin: " + path + "\n"
//                    + "Path does not exist? " + e.getMessage());
//        } catch (Error e) {
//            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
//        }
//
//        Class<?> pluginClass = null;
//        try {
//            pluginClass = classLoader.loadClass(className); // Load the class
//        } catch (ClassNotFoundException ex) {
//            logPluginLoaderError("Class not found exeption whill reading Plugin: " + path + "\n"
//                    + "Not a plugin? plugin not build correctly?");
//        } catch (Exception e) {
//            logPluginLoaderError("There was an Exeption while loading Plugin: " + path + "\n"
//                    + "Outdated Plugin? plugin not build correctly? " + e.getMessage());
//        } catch (Error e) {
//            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
//        }
//
//        SystemCallPlugin plugin = null;
//        try {
//            plugin = (SystemCallPlugin) pluginClass.newInstance();
//        } catch (InstantiationException ex) {
//            logPluginLoaderError("Instantiation Exeption when loading Plugin: " + path);
//        } catch (IllegalAccessException ex) {
//            logPluginLoaderError("Illegal Access Exeption when loading Plugin: " + path);
//        } catch (Exception e) {
//            logPluginLoaderError("There was some Exeption while loading Plugin: " + path + " " + e.getMessage());
//        } catch (Error e) {
//            logPluginLoaderError("There was some error while loading Plugin: " + path + " " + e.getMessage());
//        }
//
//        if (plugin != null) {
//            logPluginLoaderSystemMessage(plugin.PLUGIN_NAME + " was loaded");
//            plugin.onLoad();
//        } else {
//            logPluginLoaderWarning("Plugin at: " + path + " could not be loaded");
//        }
//        return plugin;
//    }
    public static void loadDefaultPlugins() {

        loadInternalPlugin("/org/parker/mips/Processor/InternalSystemCallPlugins/test.yml");

//        SystemCallPlugin spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.DefaultSystemCalls");
//        SystemCallPluginHandler.registerSystemCallPlugin(spc);
//
//        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.UserIOSystemCalls");
//        SystemCallPluginHandler.registerSystemCallPlugin(spc);
//
//        spc = loadInternalPluginFromClassPath("org.parker.mips.Processor.InternalSystemCallPlugins.ScreenSystemCalls");
//        SystemCallPluginHandler.registerSystemCallPlugin(spc);
//
//        File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
//        File files[] = file.listFiles();
//        for (File f : files) {
//            if (f.exists()) {
//                SystemCallPlugin scp = loadExternalPluginFromJarPath(f.getAbsolutePath());
//                SystemCallPluginHandler.registerSystemCallPlugin(scp);
//            }
//        }
//
//        SystemCallPluginHandler.regenerateStandardSysCallHeaderFile();
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

//    public static void loadExternalPlugin(File chosenFile) {
//        //SystemCallPlugin scp = loadExternalPluginFromJarPath(chosenFile.getAbsolutePath());
//        //SystemCallPluginHandler.registerSystemCallPlugin(scp);
//        //SystemCallPluginHandler.regenerateStandardSysCallHeaderFile();
//    }
}
