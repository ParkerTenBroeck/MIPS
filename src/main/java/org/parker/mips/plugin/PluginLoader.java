/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.parker.mips.Log;
import org.parker.mips.MIPS;
import org.parker.mips.ResourceHandler;
;
import org.parker.mips.plugin.syscall.SystemCallPlugin;
import org.parker.mips.plugin.syscall.SystemCallPluginHandler;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 *
 * @author parke
 */


public class PluginLoader {

    public static final String DEFAULT_YAML_PATH = "plugin.yml";

    private static Plugin loadInternalPlugin(String yamlPath) throws InvalidDescriptionException, InvalidPluginException, MalformedURLException, NoSuchFieldException, IOException {
        return PluginLoader.loadPlugin(new File(MIPS.JAR_PATH), yamlPath);
    }

    public static Plugin loadPluginWithCustomYAML(File file, String yamlPath) throws InvalidDescriptionException, InvalidPluginException, MalformedURLException, NoSuchFieldException, IOException {
        return PluginLoader.loadPlugin(file, yamlPath);
    }

    public static Plugin loadPlugin(File file) {
        try {
            return PluginLoader.loadPlugin(file, DEFAULT_YAML_PATH);
        } catch (Exception e) {
            logPluginLoaderError(e.toString());
        }
        return null;
    }

    private static Plugin loadPlugin(File file, String yamlPath) throws InvalidDescriptionException, InvalidPluginException, MalformedURLException, NoSuchFieldException, IOException {

        Map<String, Object> loadedYaml = null;

        JarFile jar = null;
        JarEntry entry = null;
        InputStream stream = null;

        try {
            if (file.getAbsolutePath().equals(new File(MIPS.JAR_PATH).getAbsolutePath())) {
                stream = PluginLoader.class.getResourceAsStream(yamlPath);
            } else {

                try {
                    jar = new JarFile(file);
                    entry = jar.getJarEntry(yamlPath);
                } catch (IOException e) {
                    throw new FileNotFoundException("Could not load Plugin at: " + file.getAbsolutePath() + " Error: " + e.toString());
                }

                if (entry == null) {
                    throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
                }

                stream = jar.getInputStream(entry);
            }

            if (stream == null) {
                throw new InvalidPluginException(yamlPath);
            }
            loadedYaml = (Map<String, Object>) new Yaml().load(stream);

            if (loadedYaml == null) {
                throw new InvalidDescriptionException("loaded YAML null");
            }

        } catch (IOException ex) {
            throw ex;
        } catch (YAMLException ex) {
            ex.printStackTrace();
            throw new InvalidDescriptionException(ex.toString());
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
                    logPluginLoaderError(e.toString());
                }
            }
        }

        PluginClassLoader current = new PluginClassLoader(file, loadedYaml, PluginLoader.class.getClassLoader());
        if (current != null) {
            if (!(current.plugin instanceof SystemCallPlugin)) {
                throw new InvalidPluginException("Plugin not a SystemCall Plugin: " + file.getPath());
            } else {
                return (SystemCallPlugin) current.plugin;
            }
        } else {
            //error
            throw new InvalidPluginException("Plugin null: " + file.getPath());
        }
    }

    public static void loadDefaultPlugins() {
        try {

            SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) loadInternalPlugin("/org/parker/mips/plugin/internal/syscall/default.yml"));
            SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) loadInternalPlugin("/org/parker/mips/plugin/internal/syscall/screen.yml"));
            SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) loadInternalPlugin("/org/parker/mips/plugin/internal/syscall/userio.yml"));

            SystemCallPluginHandler.registerSystemCallPlugin((SystemCallPlugin) loadPlugin(new File("C:\\GitHub\\MIPS\\examples\\exampleSystemCallPlugin\\dist\\exampleSystemCallPlugin.jar")));
            if (true) {
                return;
            }

            File file = new File(ResourceHandler.SYS_CALLS_PLUGIN_PATH);
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.exists()) {
                    try {
                        SystemCallPlugin scp = (SystemCallPlugin) loadPlugin(f);
                        SystemCallPluginHandler.registerSystemCallPlugin(scp);
                    } catch (Exception e) {
                        logPluginLoaderError("Failed to load: " + f.getAbsolutePath() + " " + e.toString());
                    }
                }
            }

        } catch (Exception e) {
            logPluginLoaderError(e.toString());
            e.printStackTrace();
        }
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

}
